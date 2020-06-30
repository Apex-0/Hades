package me.apex.hades.check.impl.combat;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.packetevents.AttackEvent;
import me.apex.hades.event.impl.packetevents.SwingEvent;
import me.apex.hades.user.User;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

@CheckInfo(name = "Angle")
public class Angle extends Check {

    private float angle;
    private double dist;
    private boolean swung;
    private int attackTick;
    private Entity lastEntity;

    private double preVLA;

    @Override
    public void onHandle(PacketEvent e, User user) {
        if(e instanceof AttackEvent) {
            Entity entity = ((AttackEvent) e).getEntity();
            Vector vec = entity.getLocation().clone().toVector().setY(0.0).subtract(user.getPlayer().getEyeLocation().clone().toVector().setY(0.0));
            float angle = user.getPlayer().getEyeLocation().getDirection().angle(vec);

            if(angle > 1.0 && user.getPlayer().getLocation().toVector().setY(0.0).distance(entity.getLocation().toVector().setY(0.0)) > 1.0) {
                if(++preVLA > 2) {
                    flag(user, "NoLook","invalid attack angle, a: " + angle, false);
                }
            }else preVLA *= 0.75;

            swung = false;
            lastEntity = ((AttackEvent) e).getEntity();
            attackTick = user.getTick();
        }else if(e instanceof SwingEvent) {
            if(lastEntity != null) {
                Vector vec = lastEntity.getLocation().clone().toVector().setY(0.0).subtract(user.getPlayer().getEyeLocation().clone().toVector().setY(0.0));
                float angle = user.getPlayer().getEyeLocation().getDirection().angle(vec);
                double dist = user.getPlayer().getLocation().toVector().setY(0.0).distance(lastEntity.getLocation().toVector().setY(0.0));

                if(swung && elapsed(user.getTick(), attackTick) < 5) {
                    swung = false;
                    flag(user, "Miss-hit","swung at entity without attacking, a: " + this.angle + ", d: " + this.dist, true);
                }else lastEntity = null;

                if(angle < 0.5 && dist <= 2.0) {
                    swung = true;
                    this.dist = dist;
                    this.angle = angle;
                }
            }
        }
    }
}
