package me.apex.hades;

import io.github.retrooper.packetevents.PacketEvents;
import lombok.Getter;
import me.apex.hades.check.CheckManager;
import me.apex.hades.command.CommandManager;
import me.apex.hades.listener.BukkitListener;
import me.apex.hades.listener.NetworkListener;
import me.apex.hades.util.lunar.BufferUtils;
import me.apex.hades.util.lunar.implementation.LunarClientImplementation;
import me.apex.hades.util.reflection.ReflectionUtil;
import me.apex.hades.util.reflection.VersionUtil;
import net.mineaus.lunar.api.LunarClientAPI;
import net.mineaus.lunar.api.event.impl.AuthenticateEvent;
import net.mineaus.lunar.api.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Getter
public class HadesPlugin extends JavaPlugin {
    @Getter private static HadesPlugin instance;

    private VersionUtil versionUtil;
    public static String bukkitVersion;

    private ScheduledExecutorService executorService;

    private LunarClientAPI lunarClientAPI;

    @Override
    public void onLoad() { PacketEvents.load(); }

    @Override
    public void onEnable() {
        //Register Instance
        instance = this;

        //Version
        bukkitVersion = Bukkit.getServer().getClass().getPackage().getName().substring(23);

        //Register Fields
        executorService = Executors.newSingleThreadScheduledExecutor();

        //Utils
        new ReflectionUtil();
        versionUtil = new VersionUtil();

        //Save Config
        saveDefaultConfig();

        //Register Commands
        CommandManager.setup(this);

        //Register Config
        HadesConfig.updateSettings();

        //Register Listeners
        Bukkit.getPluginManager().registerEvents(new BukkitListener(), this);

        //Register Checks
        CheckManager.registerChecks();

        //Register PacketEvents
        PacketEvents.getSettings().setIdentifier("hades_handler");
        PacketEvents.start(this);
        PacketEvents.getAPI().getEventManager().registerListener(new NetworkListener());

        //Register Lunar Client API
        lunarClientAPI = new LunarClientImplementation(this);
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "Lunar-Client");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "Lunar-Client", (channel, player, bytes) -> {
            if (bytes[0] == 26) {
                final UUID outcoming = BufferUtils.getUUIDFromBytes(Arrays.copyOfRange(bytes, 1, 30));

                // To stop server wide spoofing.
                if (!outcoming.equals(player.getUniqueId())) {
                    return;
                }

                User user = getLunarClientAPI().getUserManager().getPlayerData(player);

                if (user != null && !user.isLunarClient()) {
                    user.setLunarClient(true);
                    new AuthenticateEvent(player).call(this);
                }

                for (Player other : Bukkit.getOnlinePlayers()) {
                    if (getLunarClientAPI().isAuthenticated(other)) {
                        other.sendPluginMessage(this, channel, bytes);
                    }
                }
            }
        });
    }

    @Override
    public void onDisable() {
        PacketEvents.stop();
        executorService.shutdownNow();
    }

    public boolean reloadPlugin(){
        try {
            reloadConfig();
            HadesConfig.updateSettings();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
