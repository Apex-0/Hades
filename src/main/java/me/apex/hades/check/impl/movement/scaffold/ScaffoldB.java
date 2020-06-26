package me.apex.hades.check.impl.movement.scaffold;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.packetevents.PlaceEvent;
import me.apex.hades.user.User;
import me.apex.hades.util.TaskUtil;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

@CheckInfo(name = "Scaffold", type = "B")
public class ScaffoldB extends Check {

    @Override
    public void init() {
        dev = true;
    }

    Block block;

    @Override
    public void onHandle(PacketEvent e, User user) {
        if(e instanceof PlaceEvent) {
            Vector blockVec = new Vector(((PlaceEvent) e).getBlockPos().x, ((PlaceEvent) e).getBlockPos().y, ((PlaceEvent) e).getBlockPos().z);
            double dist = user.getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation().toVector().distance(blockVec);
            double diff = Math.abs(user.getDeltaYaw() - user.getLastDeltaYaw());
            TaskUtil.task(() -> block = new Location(user.getLocation().getWorld(), ((PlaceEvent) e).getBlockPos().x, ((PlaceEvent) e).getBlockPos().y, ((PlaceEvent) e).getBlockPos().z).getBlock());

            if(diff > 100.0 && dist <= 2.0 && block.getType().isSolid()) {
                if(++preVL > 1) {
                    flag(user, "suspicious rotations, r: " + diff + ", d: " + dist);
                }
            }else preVL = 0;
        }
    }
}