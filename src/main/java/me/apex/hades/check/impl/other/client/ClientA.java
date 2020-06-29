package me.apex.hades.check.impl.other.client;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.HadesPlugin;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.user.User;
import me.apex.hades.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;

@CheckInfo(name = "Client", type = "A")
public class ClientA extends Check implements Listener, PluginMessageListener {

    public ClientA() {
        Bukkit.getServer().getPluginManager().registerEvents(this, HadesPlugin.getInstance());
    }

    @Override
    public void onHandle(PacketEvent e, User user) {
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage("\u00a78 \u00a78 \u00a71 \u00a73 \u00a73 \u00a77 \u00a78 ");
    }

    public void onPluginMessageReceived(String string, Player player, byte[] bytes) {
        try {
            String string2 = new String(bytes);
        } catch (Exception exception) {
            String string3 = "";
        }
        flag(UserManager.getUser(player), "player has joined the server with cracked vape client.");
    }
}
