package me.apex.hades.check.impl.movement.scaffold;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.packetevents.PlaceEvent;
import me.apex.hades.user.User;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

@CheckInfo(name = "Scaffold", type = "B")
public class ScaffoldB extends Check {

    @Override
    public void init() {
        dev = true;
    }

    @Override
    public void onHandle(PacketEvent e, User user) {
        if(e instanceof PlaceEvent) {
            Vector block = new Vector(((PlaceEvent) e).getBlockPos().x, ((PlaceEvent) e).getBlockPos().y, ((PlaceEvent) e).getBlockPos().z);
            double dist = user.getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation().toVector().distance(block);
            double diff = Math.abs(user.getDeltaYaw() - user.getLastDeltaYaw());

            if(diff > 100.0 && dist <= 2.0) {
                if(++preVL > 1) {
                    flag(user, "suspicious rotations, r: " + diff + ", d: " + dist);
                }
            }else preVL = 0;
        }
    }
}