package me.apex.hades.check.impl.movement;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.packetevents.FlyingEvent;
import me.apex.hades.user.User;
import me.apex.hades.util.PlayerUtil;
import org.bukkit.util.Vector;

//Credits to funkemunky for the base check!
@CheckInfo(name = "Sprint")
public class Sprint extends Check {

    private double preVLA;

    @Override
    public void onHandle(PacketEvent e, User user) {
        if (e instanceof FlyingEvent) {
            if (user.getTick() > 10 && elapsed(user.getTick(), user.getFlyingTick()) > 20) {
                Vector move = new Vector(user.getLocation().getX() - user.getLastLocation().getX(), 0, user.getLocation().getZ() - user.getLastLocation().getZ());
                double delta = move.distanceSquared(user.getDirection());
                if (delta >= .23 && PlayerUtil.isOnGround(user.getPlayer()) && user.isSprinting() && user.getDeltaXZ() > 0.1 && !user.isInLiquid() && !user.isInWeb()) {
                    if (++preVLA > 4) {
                        flag(user, "Multidirectional","multidirectional sprint, p: " + delta, false);
                    }
                } else preVLA = 0;
            }
        }
    }
}
