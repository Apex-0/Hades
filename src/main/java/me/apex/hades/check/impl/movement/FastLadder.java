package me.apex.hades.check.impl.movement;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.packetevents.FlyingEvent;
import me.apex.hades.user.User;
import me.apex.hades.util.PlayerUtil;

@CheckInfo(name = "FastLadder")
public class FastLadder extends Check {

    private double preVLA;

    @Override
    public void onHandle(PacketEvent e, User user) {
        if (e instanceof FlyingEvent) {
            if (user.getLocation().getY() == user.getLastLocation().getY()
                    || !PlayerUtil.isOnClimbable(user.getPlayer())
                    || elapsed(user.getTick(), user.getFlyingTick()) < 40
                    && elapsed(user.getTick(), user.getVelocityTick()) <= 20
                    || user.getClimbableTicks() < 5
                    || user.getLocation().getY() < user.getLastLocation().getY()
                    || user.getDeltaY() != user.getLastDeltaY()) {
                return;
            }
            if (Math.abs(user.getLocation().getY() - user.getLastLocation().getY()) >= 0.12) {
                if (++preVLA > 3) {
                    flag(user, "Limit","going up a ladder faster than possible. s: " + user.getDeltaY(), false);
                }
            } else preVLA = 0;
        }
    }
}
