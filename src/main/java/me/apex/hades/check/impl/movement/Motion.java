package me.apex.hades.check.impl.movement;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.packetevents.AttackEvent;
import me.apex.hades.event.impl.packetevents.FlyingEvent;
import me.apex.hades.user.User;
import me.apex.hades.util.PlayerUtil;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

@CheckInfo(name = "Motion")
public class Motion extends Check {

    private boolean attacked;
    private double preVLA, preVLB;
    private double startMotion;

    @Override
    public void onHandle(PacketEvent e, User user) {
        if (e instanceof FlyingEvent) {
            if (!user.isInLiquid() && elapsed(user.getTick(), user.getFlyingTick()) > 40 && !user.isInWeb() && elapsed(user.getTick(), user.getTeleportTick()) > 20
                    && elapsed(user.getTick(), user.getVelocityTick()) > user.getMaxVelocityTicks()) {
                double max = 0.7 + PlayerUtil.getPotionEffectLevel(user.getPlayer(), PotionEffectType.JUMP) * 0.1;
                if (user.getDeltaY() > max && user.getPlayer().getVelocity().getY() < -0.075
                        && elapsed(user.getTick(), user.getVelocityTick()) > 20) {
                    flag(user, "InstantY","accelerating faster than possible on Y axis. d: " + user.getDeltaY(), false);
                }
            }

            if(attacked && user.isSprinting()) {
                double prediction = user.getLastDeltaXZ() * 0.36;
                double diff = user.getDeltaXZ() - prediction;

                if(diff > 0.16D) {
                    if(++preVLA > 1) {
                        flag(user, "AttackAcceleration", "diff = " + diff);
                    }
                }else preVLA = 0;

                attacked = false;
            }

            if (!user.isUnderBlock() && !user.getLocation().clone().subtract(0,0.2,0).getBlock().getType().equals(Material.SLIME_BLOCK)) {
                if (user.getDeltaY() == -user.getLastDeltaY() && user.getDeltaY() != 0 && elapsed(user.getTick(), user.getTeleportTick()) > 0) {
                    if (++preVLB > 1) {
                        flag(user, "FastHop","repetitive vertical motions, m: " + user.getDeltaY(), false);
                    }
                } else preVLB = 0;
            }

            if(user.getAirTicks() == 1) {
                startMotion = user.getDeltaY();
            }
            if(!user.isUnderBlock()
                    && !PlayerUtil.blockNearHeadExpanded(user.getPlayer())
                    && elapsed(user.getTick(), user.getLiquidTick()) > 20
                    && elapsed(user.getTick(), user.getClimbableTick()) > 20
                    && elapsed(user.getTick(), user.getVelocityTick()) > user.getMaxVelocityTicks()
                    && !user.getLocation().clone().subtract(0,0.2,0).getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.SLIME_BLOCK)) {
                if(user.getDeltaY() < 0 && startMotion > 0 && user.getAirTicks() > 0 && user.getAirTicks() <= 6) {
                    flag(user, "SmallHop", "falling with low air ticks, d: " + user.getDeltaY() + ", t: " + user.getAirTicks(), true);
                }
            }
        }else if(e instanceof AttackEvent) {
            if (((AttackEvent) e).getEntity() instanceof Player) {
                attacked = true;
            }
        }
    }
}
