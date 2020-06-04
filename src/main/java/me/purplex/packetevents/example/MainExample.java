package me.purplex.packetevents.example;

import me.purplex.packetevents.PacketEvents;
import org.bukkit.plugin.java.JavaPlugin;

public class MainExample extends JavaPlugin {
    @Override
    public void onEnable() {

        //Should the ServerTickEvent ever get called?
        final boolean serverTickEventEnabled = true;

        /**
         * Starts the servertickevent repeating task (if enabled)
         * and registers onJoin and onQuit events to inject and uninject players.
         * The first argument is an instance to your Main Class,
         * The second argument is a boolean whether the servertickevent should ever get called
         */
        PacketEvents.setup(this, serverTickEventEnabled);


        /**
         * Register TestExample's PacketListener
         */
        PacketEvents.getEventManager().registerListener(new RegisteredListener());
    }

    @Override
    public void onDisable() {
        /**
         * Stops server tick event task if enabled and unregister all listeners
         */
        PacketEvents.cleanup();

    }

}
