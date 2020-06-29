package me.apex.hades.check.impl.combat.autoclicker;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.packetevents.SwingEvent;
import me.apex.hades.user.User;
import me.apex.hades.util.MathUtil;

import java.util.Deque;
import java.util.LinkedList;

@CheckInfo(name = "AutoClicker", type = "A")
public class AutoClickerA extends Check {

    @Override
    public void init() {
        dev = true;
    }

    private final Deque<Long> ticks = new LinkedList<>();
    private double lastDeviation;

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
                    if (++preVL > 4) {
                        flag(user, "low deviation difference, d: " + diff);
                    }
                } else preVL *= 0.75;

                ticks.clear();
            }
        }
    }

}
