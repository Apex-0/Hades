package me.apex.hades.check.impl.movement;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.bukkitevents.InteractEvent;
import me.apex.hades.event.impl.packetevents.FlyingEvent;
import me.apex.hades.event.impl.packetevents.PlaceEvent;
import me.apex.hades.user.User;
import me.apex.hades.user.UserManager;
import me.apex.hades.util.PlayerUtil;
import me.apex.hades.util.TaskUtil;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.util.Vector;

@CheckInfo(name = "Scaffold")
public class Scaffold extends Check {

    private long lastFlying;

    private double preVLA, preVLB;

    @Override
    public void onHandle(PacketEvent e, User user) {
        if (e instanceof PlaceEvent) {
            long timeDiff = time() - lastFlying;

            if (timeDiff < 5) {
                if (++preVLA > 10) {
                    flag(user, "Post","low flying delay, d: " + timeDiff, false);
                }
            } else preVLA = 0;
        } else if (e instanceof FlyingEvent) {
            lastFlying = time();
        }else if (e instanceof InteractEvent){
            InteractEvent event = (InteractEvent)e;
            if (event.getBlockClicked() == null || user.getPlayer().isInsideVehicle())return;
            if(PlayerUtil.isOnGround(user.getPlayer())){
                if (user.getLocation().getBlock().getLocation().clone().subtract(0,1,0).getBlock().equals(event.getBlockClicked()) && user.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().isSolid()){
                    if (event.getBlockFace().equals(BlockFace.DOWN)){
                        flag(user, "Invalid","invalid block placement", false);
                    }
                }
            }else{
                if (user.getLocation().getBlock().getLocation().clone().subtract(0,2,0).getBlock().equals(event.getBlockClicked()) && user.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().isSolid()){
                    if (event.getBlockFace().equals(BlockFace.DOWN)){
                        flag(user, "Invalid","invalid block placement", false);
                    }
                }
            }
        }
    }
    @EventHandler
    public void onPlace(BlockPlaceEvent event){
        TaskUtil.taskAsync(() -> {
            User user = UserManager.getUser(event.getPlayer());
            Vector blockVec = event.getBlockPlaced().getLocation().toVector();
            double dist = user.getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation().toVector().distance(blockVec);
            double diff = Math.abs(user.getDeltaYaw() - user.getLastDeltaYaw());
            if(diff > 100.0 && dist <= 2.0 && event.getBlockPlaced().getType().isSolid()) {
                if(++preVLB > 1) {
                    flag(user, "Rotation","suspicious rotations, r: " + diff + ", d: " + dist, false);
                }
            }else preVLB = 0;
        });
    }
}
