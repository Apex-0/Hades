package me.apex.hades.check.impl.combat.autoclicker;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.packetevents.FlyingEvent;
import me.apex.hades.event.impl.packetevents.SwingEvent;
import me.apex.hades.user.User;

@CheckInfo(name = "AutoClicker", type = "B")
public class AutoClickerB extends Check {

    @Override
    public void init() {
        dev = true;
    }

    private int ticks;
    private double clicksPerSecond;

    @Override
    public void onHandle(PacketEvent e, User user) {
        if(e instanceof SwingEvent) {
            if(user.isDigging() || ticks > 10) {
                ticks = 0;
                return;
            }

            double speed = 1000 / ((ticks * 50.0) > 0 ? (ticks * 50.0) : 1); //Says infinite maybe because ticks * 50.0 is 0 sometimes?
            clicksPerSecond = ((clicksPerSecond * 19) + speed) / 20;

            //This sometimes says Infinity?
            //Bukkit.broadcastMessage("" + clicksPerSecond");

            ticks = 0;
        }else if(e instanceof FlyingEvent) {
            ticks++;
        }
    }
}
