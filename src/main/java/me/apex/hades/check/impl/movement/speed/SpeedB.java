package me.apex.hades.check.impl.movement.speed;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.packetevents.FlyingEvent;
import me.apex.hades.user.User;
import me.apex.hades.util.MathUtil;

@CheckInfo(name = "Speed", type = "B")
public class SpeedB extends Check {

    @Override
    public void onHandle(PacketEvent e, User user) {
        if (e instanceof FlyingEvent) {
            double max = MathUtil.getBaseSpeed(user.getPlayer());

            if (elapsed(user.getTick(), user.getIceTick()) < 40 || elapsed(user.getTick(), user.getSlimeTick()) < 40)
                max += 0.34;
            if (elapsed(user.getTick(), user.getUnderBlockTick()) < 40) max += 0.91;
            if (elapsed(user.getTick(), user.getVelocityTick()) < 100) max += 0.21;

            if (user.getDeltaXZ() > max
                    && elapsed(user.getTick(), user.getTeleportTick()) > 40
                    && elapsed(user.getTick(), user.getFlyingTick()) > 40) {
                if (++preVL > 7) {
                    flag(user, "breached limit, s: " + user.getDeltaXZ());
                }
            } else preVL *= 0.75;
        }
    }

}
