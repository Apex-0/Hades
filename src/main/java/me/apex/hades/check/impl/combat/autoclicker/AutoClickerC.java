package me.apex.hades.check.impl.combat.autoclicker;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.HadesConfig;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.packetevents.FlyingEvent;
import me.apex.hades.event.impl.packetevents.SwingEvent;
import me.apex.hades.user.User;

@CheckInfo(name = "AutoClicker", type = "C")
public class AutoClickerC extends Check {

    private int ticks;
    private double clicksPerSecond;

    @Override
    public void onHandle(PacketEvent e, User user) {
        if(e instanceof SwingEvent) {
            if(user.isDigging() || ticks > 10) {
                ticks = 0;
                return;
            }

            double speed = 1000 / ((ticks * 50.0) > 0 ? (ticks * 50.0) : 1);
            clicksPerSecond = ((clicksPerSecond * 19) + speed) / 20;

            if(clicksPerSecond > HadesConfig.MAX_CPS) {
                flag(user, "CPS: " + user.getCPS());
            }

            ticks = 0;
        }else if(e instanceof FlyingEvent) {
            ticks++;
        }
    }
}
