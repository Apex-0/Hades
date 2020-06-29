package me.apex.hades.check.impl.movement.speed;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.packetevents.FlyingEvent;
import me.apex.hades.user.User;
import me.apex.hades.util.MathUtil;

@CheckInfo(name = "Speed", type = "A")
public class SpeedA extends Check {

    @Override
    public void onHandle(PacketEvent e, User user) {
        if (e instanceof FlyingEvent) {
            double max = MathUtil.getBaseSpeed(user.getPlayer());
            double diff = user.getDeltaXZ() - user.getLastDeltaXZ();
            if (diff == 0.0 && user.getDeltaXZ() > max
                    && elapsed(user.getTick(), user.getFlyingTick()) > 40
                    && elapsed(user.getTick(), user.getVelocityTick()) > 100
                    && elapsed(user.getTick(), user.getTeleportTick()) > 20) {
                flag(user, "consistent speed, diff: " + diff);
            }
        }
    }

}
