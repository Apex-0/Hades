package me.apex.hades.check.impl.combat;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.packetevents.AttackEvent;
import me.apex.hades.user.User;
import me.apex.hades.util.MathUtil;

import java.util.Deque;
import java.util.LinkedList;

@CheckInfo(name = "Pattern")
public class Pattern extends Check {

    private final Deque<Double> diffs = new LinkedList<>();
    private double average = 100;

    private double preVLA;

    @Override
    public void onHandle(PacketEvent e, User user) {
        if (e instanceof AttackEvent) {
            double diff = Math.abs(user.getDeltaYaw() - user.getLastDeltaYaw());
            if (diff > 0.0) diffs.add(diff);

            if (diffs.size() >= 5) {
                double deviation = MathUtil.getStandardDeviation(diffs.stream().mapToDouble(d -> d).toArray());

                average = ((average * 19) + deviation) / 20;

                if (average < 5) {
                    if (++preVLA > 2) {
                        flag(user, "Jitter","low average deviation, a: " + average);
                    }
                } else preVLA *= 0.75;

                diffs.clear();
            }
        }
    }

}
