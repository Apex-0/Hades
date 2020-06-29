package me.apex.hades.check;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.HadesConfig;
import me.apex.hades.HadesPlugin;
import me.apex.hades.user.User;
import me.apex.hades.util.TaskUtil;
import me.apex.hades.util.text.ChatUtil;
import me.apex.hades.util.text.LogUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public abstract class Check implements Listener {

    //Check Data
    public long lastFlag;
    public double vl, maxVL;
    public boolean enabled, punishable, dev;

    public Check() {
        Bukkit.getServer().getPluginManager().registerEvents(this, HadesPlugin.getInstance());

        //Register Data
        maxVL = HadesConfig.MAX_VIOLATIONS.get(this.getClass().getSimpleName());
        enabled = HadesConfig.ENABLED_CHECKS.get(this.getClass().getSimpleName());
        punishable = HadesConfig.PUNISHABLE_CHECKS.get(this.getClass().getSimpleName());

        //Call Post-Init
        init();
    }

    public String getName() {
        return CheckManager.getCheckInfo(this).name();
    }

    public void init() {
    }

    public abstract void onHandle(PacketEvent e, User user);

    protected void flag(User user, String type, String information) {
        assert user != null;
        vl++;
        lastFlag = time();
        TaskUtil.task(() -> {
            String alertFormatColor = HadesConfig.FLAG_FORMAT.replace("&", "§");
            String devFormatColor = HadesConfig.DEV_FLAG_FORMAT.replace("&", "§");

            //Alert Message
            TextComponent alertMessage = new TextComponent(alertFormatColor.replace("%prefix%", HadesConfig.PREFIX).replace("%player%", user.getPlayer().getName()).replace("%check%", getName()).replace("%checktype%", type).replace("%vl%", String.valueOf(vl)).replace("%info%", information).replace("%ping%", user.ping() + "").replace("%tps%", PacketEvents.getCurrentServerTPS() + "") + (dev ? " §7(Exp)" : ""));
            alertMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + user.getPlayer()));
            alertMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + user.getPlayer().getName()));
            alertMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7(Click to teleport)").create()));

            //Dev Message
            TextComponent devMessage = new TextComponent(devFormatColor.replace("%prefix%", HadesConfig.PREFIX).replace("%player%", user.getPlayer().getName()).replace("%check%", getName()).replace("%checktype%", type).replace("%vl%", String.valueOf(vl)).replace("%info%", information).replace("%ping%", user.ping() + "").replace("%tps%", PacketEvents.getCurrentServerTPS() + "") + (dev ? " §7(Exp)" : ""));
            devMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + user.getPlayer()));
            devMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + user.getPlayer().getName()));
            devMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Info:\n§7* §c" + information + "\n§7\n§7(Click to teleport)").create()));

            ChatUtil.informStaff(alertMessage, devMessage, (int)vl);

            if (HadesConfig.LOG_TO_FILE)
                LogUtils.logToFile(user.getLogFile(), HadesConfig.LOG_FORMAT.replace("%player%", user.getPlayer().getName()).replace("%check%", getName()).replace("%checktype%", type).replace("%vl%", String.valueOf(vl)).replace("%info%", information));
            if (HadesConfig.LOG_TO_CONSOLE) {
                Bukkit.getLogger().info("(!) " + HadesConfig.LOG_FORMAT.replace("%player%", user.getPlayer().getName()).replace("%check%", getName()).replace("%checktype%", type).replace("%vl%", String.valueOf(vl)).replace("%info%", information).replace("[%time%]", ""));
            }
            if (vl >= maxVL) {
                if (punishable && !HadesConfig.TEST_MODE) {
                    if (HadesConfig.LOG_TO_FILE)
                        LogUtils.logToFile(user.getLogFile(), "%time% > [ACTION] " + HadesConfig.PUNISH_COMMANDS.get(this.getClass().getSimpleName()).replace("%player%", user.getPlayer().getName()).replace("%check%", getName()).replace("%checktype%", type));
                    if (HadesConfig.BROADCAST_PUNISHMENTS)
                        Bukkit.broadcastMessage(ChatUtil.color(HadesConfig.PUNISHMENT_BROADCAST_MESSAGE.replace("%prefix%", HadesConfig.PREFIX).replace("%player%", user.getPlayer().getName()).replace("%check%", getName()).replace("%newline%", "\n")));
                    TaskUtil.task(() -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), HadesConfig.PUNISH_COMMANDS.get(this.getClass().getSimpleName()).replace("%prefix%", HadesConfig.PREFIX).replace("%player%", user.getPlayer().getName()).replace("%check%", getName()).replace("%checktype%", type)));
                    vl = 0;
                }
            }
        });
    }

    //Time Util
    public long time() {
        return System.nanoTime() / 1000000;
    }

    public int elapsed(int now, int start) {
        return now - start;
    }

    public long elapsed(long now, long start) {
        return now - start;
    }
}
