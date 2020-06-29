package me.apex.hades.check.impl.player.timer;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.packetevents.FlyingEvent;
import me.apex.hades.user.User;
import me.apex.hades.util.MathUtil;

import java.util.Deque;
import java.util.LinkedList;

@CheckInfo(name = "Timer", type = "A")
public class TimerA extends Check {

    private final Deque<Long> flyingDeque = new LinkedList<>();
    private double lastDeviation;

    @Override
    public void onHandle(PacketEvent e, User user) {
        if (e instanceof FlyingEvent) {
            flyingDeque.add(System.currentTimeMillis());

            if (flyingDeque.size() == 50) {
                double deviation = MathUtil.getStandardDeviation(flyingDeque.stream().mapToLong(l -> l).toArray());

                if (deviation <= 710 && (Math.abs(deviation - lastDeviation) < 20)) {
                    if (preVL++ > 1)
                        flag(user, "deviation = " + deviation);
                } else preVL = 0;
                this.lastDeviation = deviation;
                flyingDeque.clear();
            }
        }
    }
}
