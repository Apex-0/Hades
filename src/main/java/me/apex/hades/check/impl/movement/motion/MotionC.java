package me.apex.hades.check.impl.movement.motion;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.packetevents.FlyingEvent;
import me.apex.hades.user.User;

@CheckInfo(name = "Motion", type = "C")
public class MotionC extends Check {
    @Override
    public void onHandle(PacketEvent e, User user) {
        if (e instanceof FlyingEvent) {
            if (user.isUnderBlock()) return;
            if (user.getDeltaY() == -user.getLastDeltaY() && user.getDeltaY() != 0 && elapsed(user.getTick(), user.getTeleportTick()) > 0) {
                if (++preVL > 1) {
                    flag(user, "repetitive vertical motions, m: " + user.getDeltaY());
                }
            } else preVL = 0;
        }
    }
}