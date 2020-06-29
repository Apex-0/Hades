package me.apex.hades.check.impl.combat.angle;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.user.User;
import org.bukkit.entity.Entity;

@CheckInfo(name = "Angle", type = "B")
public class AngleB extends Check {

    @Override
    public void init() {
        dev = true;
    }

    private float angle;
    private double dist;
    private boolean swung;
    private Entity lastEntity;

    @Override
    public void onHandle(PacketEvent e, User user) {
        /*if (e instanceof AttackEvent) {
            swung = false;
            Entity entity = ((AttackEvent) e).getEntity();
            lastEntity = entity;
        }else if(e instanceof SwingEvent) {
            if(lastEntity == null) return;
            Vector vec = lastEntity.getLocation().clone().toVector().setY(0.0).subtract(user.getPlayer().getEyeLocation().clone().toVector().setY(0.0));
            float angle = user.getPlayer().getEyeLocation().getDirection().angle(vec);
            double dist = user.getPlayer().getLocation().toVector().setY(0.0).distance(lastEntity.getLocation().toVector().setY(0.0));

            if(swung) {
                swung = false;
                flag(user, "swung at entity without attacking, a: " + this.angle + ", d: " + this.dist);
            }

            if(angle < 0.5 && dist <= 2.0) {
                swung = true;
                this.dist = dist;
                this.angle = angle;
            }
        }*/
    }

}
