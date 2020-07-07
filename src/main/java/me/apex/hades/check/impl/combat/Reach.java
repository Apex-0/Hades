package me.apex.hades.check.impl.combat;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.HadesConfig;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.packetevents.AttackEvent;
import me.apex.hades.event.impl.packetevents.FlyingEvent;
import me.apex.hades.user.User;
import me.apex.hades.util.boundingbox.AABB;
import me.apex.hades.util.boundingbox.Ray;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

@CheckInfo(name = "Reach")
public class Reach extends Check {

    boolean attacked = false;
    Entity entity;
    List<Double> reachs = new ArrayList<>();

    int preVL;
    Long lastFlying;

    @Override
    public void onHandle(PacketEvent e, User user) {
        if (e instanceof AttackEvent) {
            attacked = true;
            entity = ((AttackEvent) e).getEntity();
        } else if (e instanceof FlyingEvent) {
            if (attacked){
                if (reachs.size() >= 5) {
                    reachs.remove(0);
                }
                if (lastFlying == null || elapsed(e.getTimestamp(), lastFlying) <= 500){
                    attacked = false;
                    Ray ray = Ray.from(user);
                    double dist = AABB.from(entity).collidesD(ray, 0, 10);
                    if (dist != -1) {
                        reachs.add(dist);
                    }

                    if (reachs.size() >= 5) {
                        double total = 0;
                        double avgReach = 0;
                        for (int i = 0; i < reachs.size(); i++) {
                            total += reachs.get(i);
                            avgReach = total / reachs.size();
                        }

                        if (avgReach >= HadesConfig.MAX_REACH && dist >= 3.2) {
                            if (++preVL > 1) {
                                flag(user, "RayTrace","hitting farther than possbile. Dist: " + avgReach, true);
                            }
                        } else preVL = 0;
                    }
                }
            }

            lastFlying = e.getTimestamp();
        }
    }
}
