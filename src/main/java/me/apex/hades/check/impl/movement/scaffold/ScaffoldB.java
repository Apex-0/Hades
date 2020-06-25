package me.apex.hades.check.impl.movement.scaffold;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.packetevents.PlaceEvent;
import me.apex.hades.user.User;
import org.bukkit.Bukkit;
import org.bukkit.util.Vector;

@CheckInfo(name = "Scaffold", type = "B")
public class ScaffoldB extends Check {
    @Override
    public void onHandle(PacketEvent e, User user) {
        if(e instanceof PlaceEvent) {
            Vector pos = new Vector(((PlaceEvent) e).getBlockPos().x, ((PlaceEvent) e).getBlockPos().y, ((PlaceEvent) e).getBlockPos().z);
            Vector vec = pos.clone().setY(0.0).subtract(user.getPlayer().getEyeLocation().clone().toVector().setY(0.0));
            float angle = user.getPlayer().getEyeLocation().getDirection().angle(vec);

            Bukkit.broadcastMessage("" + angle);
        }
    }
}