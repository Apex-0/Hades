package me.apex.hades.check.impl.movement.sprint;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.packetevents.FlyingEvent;
import me.apex.hades.user.User;
import me.apex.hades.util.PlayerUtil;
import org.bukkit.util.Vector;

//Credits to funkemunky for the base check!
@CheckInfo(name = "Sprint", type = "A")
public class SprintA extends Check {
    @Override
    public void onHandle(PacketEvent e, User user) {
        if (e instanceof FlyingEvent) {
            if ((System.currentTimeMillis() - user.getJoinTime()) < 2000 && ((FlyingEvent) e).hasMoved() && user.getPlayer().isFlying())
                return;
            Vector move = new Vector(user.getLocation().getX() - user.getLastLocation().getX(), 0, user.getLocation().getZ() - user.getLastLocation().getZ());
            double predictedDelta = move.distanceSquared(user.getDirection());
            if (predictedDelta >= .23 && PlayerUtil.isOnGround(user.getPlayer()) && user.isSprinting() && user.getDeltaXZ() > 0.1 && !user.isInLiquid() && !user.isInWeb()) {
                if (++preVL > 4) {
                    flag(user, "omni sprint, p: " + predictedDelta);
                }
            } else preVL = 0;
        }
    }
}
