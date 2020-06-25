package me.apex.hades.listener;

import me.apex.hades.HadesPlugin;
import me.apex.hades.event.impl.bukkitevents.InteractEvent;
import me.apex.hades.event.impl.bukkitevents.ItemConsumeEvent;
import me.apex.hades.user.User;
import me.apex.hades.user.UserManager;
import me.apex.hades.util.TaskUtil;
import me.apex.hades.util.reflection.ReflectionUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerRegisterChannelEvent;
import org.bukkit.event.player.PlayerUnregisterChannelEvent;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class BukkitListener implements Listener {

    @EventHandler
    public void onRegister(PlayerRegisterChannelEvent e) {
        if (e.getChannel().equals("Lunar-Client")) {
            try {
                HadesPlugin.getInstance().getLunarClientAPI().performEmote(e.getPlayer(), 5, false);
                HadesPlugin.getInstance().getLunarClientAPI().performEmote(e.getPlayer(), -1, false);

                Object nmsPlayer = e.getPlayer().getClass().getMethod("getHandle").invoke(e.getPlayer());
                Object packet = ReflectionUtil.getNmsClass("PacketPlayOutEntityStatus")
                        .getConstructor(ReflectionUtil.getNmsClass("Entity"), byte.class)
                        .newInstance(nmsPlayer, (byte) 2);
                ReflectionUtil.sendPacket(e.getPlayer(), packet);
            } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException | NoSuchFieldException | ClassNotFoundException | IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @EventHandler
    public void onUnregisterChannel(PlayerUnregisterChannelEvent e) {
        net.mineaus.lunar.api.user.User user = HadesPlugin.getInstance().getLunarClientAPI().getUserManager().getPlayerData(e.getPlayer().getUniqueId());
        if (e.getChannel().equals("Lunar-Client")) {
            user.setLunarClient(false);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        User user = UserManager.getUser(e.getPlayer());
        if (user != null) {
            TaskUtil.taskAsync(() -> {
                user.getExecutorService().execute(() -> user.getChecks().stream().filter(check -> check.enabled).forEach(check -> check.onHandle(new InteractEvent(e.getAction(), e.getItem(), e.getClickedBlock(), e.getBlockFace(), e.useItemInHand(), e.useInteractedBlock()), user)));
            });
        }
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent e) {
        User user = UserManager.getUser(e.getPlayer());
        if (user != null) {
            TaskUtil.taskAsync(() -> {
                user.getExecutorService().execute(() -> user.getChecks().stream().filter(check -> check.enabled).forEach(check -> check.onHandle(new ItemConsumeEvent(e.getItem()), user)));
            });
        }
    }
}
