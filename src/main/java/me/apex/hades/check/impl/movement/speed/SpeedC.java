package me.apex.hades.check.impl.movement.speed;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.packetevents.FlyingEvent;
import me.apex.hades.user.User;

@CheckInfo(name = "Speed", type = "C")
public class SpeedC extends Check {

    boolean lastGround;

    @Override
    public void onHandle(PacketEvent e, User user) {
        if (e instanceof FlyingEvent) {
            double prediction = user.getLastDeltaXZ() * 0.91F + 0.025999999F;
            double diff = user.getDeltaXZ() - prediction;

            if (diff > 1E-12 &&
                    !user.isOnGround() &&
                    !lastGround &&
                    user.getLocation().getY() != user.getLastLocation().getY()
                    && elapsed(user.getTick(), user.getLiquidTick()) > 20
                    && elapsed(user.getTick(), user.getClimbableTick()) > 20
                    && elapsed(user.getTick(), user.getFlyingTick()) > 40) {
                if (++preVL > 0) {
                    flag(user, "invalid predicted dist, d: " + diff);
                }
            } else preVL *= 0.75;

            lastGround = user.isOnGround();
        }
    }
}
