package me.apex.hades.check.impl.combat;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.packetevents.AttackEvent;
import me.apex.hades.event.impl.packetevents.FlyingEvent;
import me.apex.hades.user.User;
import me.apex.hades.util.MathUtil;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.LinkedList;
import java.util.List;

@CheckInfo(name = "Killaura")
public class Killaura extends Check {

    private int ticks, flyingTicks;
    private long lastFlying;
    private Entity lastTarget;
    private float lastAngle;

    private double preVLA, preVLB, preVLC;



    //Credits to Moritz for this check, permission taken.
    private double lastDeviation;
    private int hitTicks;
    private boolean attacked;

    private List<Double> deltas = new LinkedList<>();


    @Override
    public void onHandle(PacketEvent e, User user) {
         if (e instanceof FlyingEvent) {
            lastFlying = time();
            ticks = 0;flyingTicks++;

             if (((FlyingEvent) e).hasLooked()) {
                 if (attacked || hitTicks > 3) {
                     deltas.add((double) (user.getDeltaYaw() % user.getDeltaPitch()));
                     attacked = false;
                     hitTicks--;
                 }
                 if (deltas.size() == 36) {
                     double deviation = MathUtil.getStandardDeviation(deltas);
                     if (Double.isNaN(deviation) & !Double.isNaN(lastDeviation)) {
                         flag(user, "Rotation", "bad rotations: " + deviation);
                     }
                     lastDeviation = deviation;
                     deltas.clear();
                 }
             }

        } else if (e instanceof AttackEvent) {
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

            Vector vec = entity.getLocation().clone().toVector().setY(0.0).subtract(user.getPlayer().getEyeLocation().clone().toVector().setY(0.0));
            float angle = user.getPlayer().getEyeLocation().getDirection().angle(vec);

            float diff = Math.abs(angle - lastAngle);

            if(diff < 0.01 && rotation > 5) {
                if(++preVLC > 6) {
                    flag(user, "LockView", "diff = " + diff + ", rotation = " + rotation, true);
                }
            }else preVLC *= 0.75;

            lastAngle = angle;

             hitTicks = 3;
             attacked = true;
        }
    }

}
