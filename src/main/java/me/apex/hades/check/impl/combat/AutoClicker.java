package me.apex.hades.check.impl.combat;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.HadesPlugin;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.packetevents.FlyingEvent;
import me.apex.hades.event.impl.packetevents.SwingEvent;
import me.apex.hades.user.User;
import me.apex.hades.util.MathUtil;

import java.util.Deque;
import java.util.LinkedList;

@CheckInfo(name = "AutoClicker")
public class AutoClicker extends Check {

    private final Deque<Long> ticks = new LinkedList<>();
    private double lastDeviation;

    private int flyingTicks;
    private double clicksPerSecond;

    private double preVLA;

    @Override
    public void onHandle(PacketEvent e, User user) {
        if (e instanceof SwingEvent) {
            if (!user.isDigging()) ticks.add((long) (user.getTick() * 50.0));
            if (ticks.size() >= 10) {
                double deviation = MathUtil.getStandardDeviation(ticks.stream().mapToLong(d -> d).toArray());
                double lastDeviation = this.lastDeviation;
                this.lastDeviation = deviation;

                double diff = Math.abs(deviation - lastDeviation);

                if (diff < 10) {
                    if (++preVLA > 4) {
                        flag(user, "Consistency","low deviation difference, d: " + diff,false);
                    }
                } else preVLA *= 0.75;

                ticks.clear();
            }

            if(user.isDigging() || flyingTicks > 10) {
                flyingTicks = 0;
                return;
            }

            double speed = 1000 / ((flyingTicks * 50.0) > 0 ? (flyingTicks * 50.0) : 1);
            clicksPerSecond = ((clicksPerSecond * 19) + speed) / 20;

            if(clicksPerSecond > HadesPlugin.getInstance().getConfig().getInt("Max-CPS")) {
                flag(user, "ClickSpeed","CPS: " + clicksPerSecond, false);
            }

            flyingTicks = 0;
        }else if(e instanceof FlyingEvent) {
            flyingTicks++;
        }
    }

}
