package me.apex.hades.check.impl.combat;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.packetevents.AttackEvent;
import me.apex.hades.event.impl.packetevents.FlyingEvent;
import me.apex.hades.user.User;
import me.apex.hades.util.MathUtil;
import org.bukkit.entity.Entity;

@CheckInfo(name = "Killaura")
public class Killaura extends Check {

    private int ticks, flyingTicks;
    private long lastFlying;
    private Entity lastTarget;

    private double preVLA, preVLB, preVLC;

    @Override
    public void onHandle(PacketEvent e, User user) {
        if (e instanceof AttackEvent) {
            long timeDiff = time() - lastFlying;

            if (timeDiff < 5) {
                if (++preVLA > 10) {
                    flag(user, "Post","low flying delay, d: " + timeDiff, false);
                }
            } else preVLA = 0;

            if (ticks++ > 1) {
                flag(user, "Multi","too many attacks in tick, t: " + ticks, false);
            }

            Entity target = ((AttackEvent) e).getEntity();
            Entity lastTarget = this.lastTarget != null ? this.lastTarget : target;
            this.lastTarget = target;

            if (target != lastTarget) {
                if (flyingTicks < 2) {
                    if (++preVLB > 2) {
                        flag(user, "Switch","invalid attack pattern, t: " + flyingTicks, false);
                    }
                } else preVLB *= 0.75;
            }
            flyingTicks = 0;

            Entity entity = ((AttackEvent) e).getEntity();
            double rotation = Math.abs(user.getDeltaYaw());

            double dir = MathUtil.getDirection(user.getLocation(), entity.getLocation());
            double dist = MathUtil.getDistanceBetweenAngles360(user.getLocation().getYaw(), dir);

            if (dist < 0.7 && rotation > 2) {
                if (preVLC++ > 4)
                    flag(user, "Lockview","angle = " + dist + ", rotation = " + rotation, false);
            } else preVLC *= 0.75;
        } else if (e instanceof FlyingEvent) {
            lastFlying = time();
            ticks = 0;flyingTicks++;
        }
    }

}
