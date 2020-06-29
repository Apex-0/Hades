package me.apex.hades.check.impl.movement.motion;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.packetevents.AttackEvent;
import me.apex.hades.event.impl.packetevents.FlyingEvent;
import me.apex.hades.user.User;
import org.bukkit.entity.Player;

@CheckInfo(name = "Motion", type = "B")
public class MotionB extends Check {

    @Override
    public void init() {
        dev = true;
    }

    private int hits;

    @Override
    public void onHandle(PacketEvent e, User user) {
        if (e instanceof FlyingEvent) {
            if (user.isSprinting() && ++hits <= 2) {
                double accel = Math.abs(user.getDeltaXZ() - user.getLastDeltaXZ());
                if (accel < 0.027) {
                    if (++preVL >= 7) {
                        flag(user, "invalid acceleration, a: " + accel);
                    }
                } else preVL = 0;
            }
        } else if (e instanceof AttackEvent) {
            if (((AttackEvent) e).getEntity() instanceof Player) {
                hits = 0;
            }
        }
    }
}
