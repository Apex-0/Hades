package me.apex.hades.check.impl.movement.scaffold;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.bukkitevents.InteractEvent;
import me.apex.hades.user.User;
import me.apex.hades.util.PlayerUtil;
import org.bukkit.block.BlockFace;

@CheckInfo(name = "Scaffold", type = "C")
public class ScaffoldC extends Check {
    @Override
    public void onHandle(PacketEvent e, User user) {
        if (e instanceof InteractEvent){
            InteractEvent event = (InteractEvent)e;
            if (event.getBlockClicked() == null || user.getPlayer().isInsideVehicle())return;
            if(PlayerUtil.isOnGround(user.getPlayer())){
                if (user.getLocation().getBlock().getLocation().clone().subtract(0,1,0).getBlock().equals(event.getBlockClicked()) && user.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().isSolid()){
                    if (event.getBlockFace().equals(BlockFace.DOWN)){
                        flag(user, "placing block under the block he/she is on.");
                    }
                }
            }else{
                if (user.getLocation().getBlock().getLocation().clone().subtract(0,1.5,0).getBlock().equals(event.getBlockClicked()) && user.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().isSolid()){
                    if (event.getBlockFace().equals(BlockFace.DOWN)){
                        flag(user, "placing block under the block he/she is on.");
                    }
                }
            }
        }
    }
}
