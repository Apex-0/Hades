package me.apex.hades.check.impl.movement;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.packetevents.FlyingEvent;
import me.apex.hades.user.User;
import me.apex.hades.util.MathUtil;

@CheckInfo(name = "Speed")
public class Speed extends Check {
    private boolean lastGround;

    private double preVLA, preVLB;

    @Override
    public void onHandle(PacketEvent e, User user) {
        if (e instanceof FlyingEvent) {
            double limit = MathUtil.getBaseSpeed(user.getPlayer());
            double diff = user.getDeltaXZ() - user.getLastDeltaXZ();
            if (diff == 0.0 && user.getDeltaXZ() > limit
                    && elapsed(user.getTick(), user.getFlyingTick()) > 40
                    && elapsed(user.getTick(), user.getTeleportTick()) > 20
                    && elapsed(user.getTick(), user.getVelocityTick()) > user.getMaxVelocityTicks()) {
                flag(user, "Consistency","consistent speed, diff: " + diff, false);
            }

            if (elapsed(user.getTick(), user.getIceTick()) < 40 || elapsed(user.getTick(), user.getSlimeTick()) < 40)
                limit += 0.34;
            if (elapsed(user.getTick(), user.getUnderBlockTick()) < 40) limit += 0.91;
            if (elapsed(user.getTick(), user.getVelocityTick()) < user.getMaxVelocityTicks()) limit += Math.abs(user.getVelocityX() + user.getVelocityZ());

            if (user.getDeltaXZ() > limit
                    && elapsed(user.getTick(), user.getTeleportTick()) > 40
                    && elapsed(user.getTick(), user.getFlyingTick()) > 40) {
                if (++preVLA > 7) {
                    flag(user, "Limit","breached limit, s: " + user.getDeltaXZ(), false);
                }
            } else preVLA *= 0.75;

            double prediction = user.getLastDeltaXZ() * 0.91F + 0.025999999F;
            double equalness = user.getDeltaXZ() - prediction;

            if (equalness > 1E-12 &&
                    !user.isOnGround() &&
                    !lastGround &&
                    user.getLocation().getY() != user.getLastLocation().getY()
                    && elapsed(user.getTick(), user.getLiquidTick()) > 20
                    && elapsed(user.getTick(), user.getClimbableTick()) > 20
                    && elapsed(user.getTick(), user.getFlyingTick()) > 40
                    && elapsed(user.getTick(), user.getVelocityTick()) > user.getMaxVelocityTicks()) {
                if (++preVLB > 2) {
                    flag(user, "Friction","invalid predicted dist, d: " + diff, false);
                }
            } else preVLB *= 0.75;

            lastGround = user.isOnGround();

            if (diff > MathUtil.getBaseSpeed(user.getPlayer())
                    && elapsed(user.getTick(), user.getVelocityTick()) > user.getMaxVelocityTicks() && elapsed(user.getTick(), user.getTeleportTick()) > 20 && !user.getPlayer().isInsideVehicle() && elapsed(user.getTick(), user.getFlyingTick()) > 40) {
                flag(user, "Acceleration","invalid acceleration, a: " + diff, false);
            }
        }
    }

}
