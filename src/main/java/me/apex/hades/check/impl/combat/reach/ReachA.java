package me.apex.hades.check.impl.combat.reach;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.HadesPlugin;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.packetevents.FlyingEvent;
import me.apex.hades.user.User;
import me.apex.hades.user.UserManager;
import me.apex.hades.util.ReachUtil;
import me.apex.hades.util.boundingbox.AABB;
import me.apex.hades.util.boundingbox.Ray;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CheckInfo(name = "Reach", type = "A")
public class ReachA extends Check {

    @Override
    public void init() {
        dev = true;
    }

    //idea from sim0n
    //improved by LIWK / Johannes
    //Raytrace by Tecnio / undersquire

    @Override
    public void onHandle(PacketEvent e, User user) {
        if (e instanceof FlyingEvent) {
            if(e.getTimestamp() - user.getLastAttackPacket() < 3L && user.getLastTarget() != null) {
                Player target = user.getLastTarget();
                Ray ray = Ray.from(user);
                double dist = AABB.from(target).collidesD(ray, 0, 10);
                if (dist == -1) {
                    Bukkit.broadcastMessage("Returned because dist is == -1");
                    return;
                }

                ReachUtil currentLocation = user.getReachLoc();
                ReachUtil previousLocation = user.getLastReachLoc();

                if(UserManager.getUser(target).getReachLoc() != null && UserManager.getUser(target).getLastReachLoc() != null) {

                    double range = UserManager.getUser(target).reachQueue.stream().mapToDouble(loc -> {
                        double distanceX = Math.min(Math.min(Math.abs(currentLocation.getX() - loc.getMinX()), Math.abs(currentLocation.getX() - loc.getMaxX())), Math.min(Math.abs(previousLocation.getX() - loc.getMinX()), Math.abs(previousLocation.getX() - loc.getMaxX())));
                        double distanceZ = Math.min(Math.min(Math.abs(currentLocation.getZ() - loc.getMinZ()), Math.abs(currentLocation.getZ() - loc.getMaxZ())), Math.min(Math.abs(previousLocation.getZ() - loc.getMinZ()), Math.abs(previousLocation.getZ() - loc.getMaxZ())));

                        return Math.sqrt(distanceX * distanceX + distanceZ * distanceZ);
                    }).min().orElse(0.0);

                    Bukkit.broadcastMessage(user.getPlayer().getName() + " > range=§c" + range + " §f> dist=§c" + dist);
                    if (range >= HadesPlugin.getInstance().getConfig().getDouble("Max-Reach") && dist > 2.9) {
                        if (++preVL > 2) {
                            flag(user, "hitting farther than possible. r: " + range + ", d: " + dist);
                        }
                    } else preVL = 0;
                } else Bukkit.broadcastMessage("rLoc=" + UserManager.getUser(target).getReachLoc() + " lastRLoc" + UserManager.getUser(target).getLastReachLoc());
            }
        }
    }
}