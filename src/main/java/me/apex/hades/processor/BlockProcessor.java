package me.apex.hades.processor;

import me.apex.hades.user.User;
import me.apex.hades.util.PlayerUtil;
import me.apex.hades.util.TaskUtil;
import org.bukkit.block.BlockFace;

public class BlockProcessor {

    public static void process(User user) {
        if (PlayerUtil.isOnGround(user.getPlayer())) {
            user.setServerGroundTick(user.getTick());
        }

        if (user.isOnGround()) {
            user.setAirTicks(0);
            user.setGroundTick(user.getTick());
            user.setGroundTicks(user.getGroundTicks() + 1);
        } else {
            user.setGroundTicks(0);
            user.setAirTick(user.getTick());
            user.setAirTicks(user.getAirTicks() + 1);
        }

        TaskUtil.task(() -> {
            if (user.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).getType().toString().contains("ICE")
                    || user.getPlayer().getLocation().clone().add(0, -0.5, 0).getBlock().getRelative(BlockFace.DOWN).getType().toString().contains("ICE")) {
                user.setIceTick(user.getTick());
                user.setIceTicks(user.getIceTicks() + 1);
            } else user.setIceTicks(0);
            if (user.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).getType().toString().contains("SLIME")) {
                user.setSlimeTick(user.getTick());
                user.setSlimeTicks(user.getSlimeTicks() + 1);
            } else user.setSlimeTicks(0);
            if (user.getPlayer().getEyeLocation().getBlock().getType().isSolid()
                    || user.getPlayer().getEyeLocation().getBlock().getRelative(BlockFace.UP).getType().isSolid()) {
                user.setUnderBlockTick(user.getTick());
            }
            if (user.getLocation().getBlock().isLiquid()
                    || user.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).isLiquid()
                    || user.getPlayer().getEyeLocation().getBlock().isLiquid()
                    || user.getPlayer().getEyeLocation().getBlock().getRelative(BlockFace.UP).isLiquid()) {
                user.setLiquidTick(user.getTick());
                user.setLiquidTicks(user.getLiquidTicks() + 1);
            } else user.setLiquidTicks(0);
            if (user.isOnClimbableBlock() || user.isInWeb()) {
                user.setClimbableTick(user.getTick());
                user.setClimbableTicks(user.getClimbableTicks() + 1);
            } else {
                user.setClimbableTicks(0);
            }
        });
    }

}
