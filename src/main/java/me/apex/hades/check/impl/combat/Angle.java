package me.apex.hades.check.impl.combat;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.packetevents.AttackEvent;
import me.apex.hades.user.User;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

@CheckInfo(name = "Angle")
public class Angle extends Check {
    @Override
    public void onHandle(PacketEvent e, User user) {
        if(e instanceof AttackEvent) {
            Entity entity = ((AttackEvent) e).getEntity();
            Vector vec = entity.getLocation().clone().toVector().setY(0.0).subtract(user.getPlayer().getEyeLocation().clone().toVector().setY(0.0));
            float angle = user.getPlayer().getEyeLocation().getDirection().angle(vec);

            if(angle > 1.0 && user.getPlayer().getLocation().toVector().setY(0.0).distance(entity.getLocation().toVector().setY(0.0)) > 1.0) {
                if(++preVL > 2) {
                    flag(user, "NoLook","invalid attack angle, a: " + angle);
                }
            }else preVL *= 0.75;
        }
    }
}
