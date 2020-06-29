package me.apex.hades.check.impl.combat.badrotations;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.packetevents.AttackEvent;
import me.apex.hades.user.User;
import me.apex.hades.util.MathUtil;

import java.util.Deque;
import java.util.LinkedList;

@CheckInfo(name = "BadRotations", type = "A")
public class BadRotationsA extends Check {

    private final Deque<Double> diffs = new LinkedList<>();
    private double average = 100;

    @Override
    public void onHandle(PacketEvent e, User user) {
        if (e instanceof AttackEvent) {
            double diff = Math.abs(user.getDeltaYaw() - user.getLastDeltaYaw());
            if (diff > 0.0) diffs.add(diff);

            if (diffs.size() >= 5) {
                double deviation = MathUtil.getStandardDeviation(diffs.stream().mapToDouble(d -> d).toArray());

                average = ((average * 19) + deviation) / 20;

                if (average < 5) {
                    if (++preVL > 2) {
                        flag(user, "low average deviation, a: " + average);
                    }
                } else preVL *= 0.75;

                diffs.clear();
            }
        }
    }

}
