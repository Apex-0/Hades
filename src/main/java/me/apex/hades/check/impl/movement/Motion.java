package me.apex.hades.check.impl.movement;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.packetevents.AttackEvent;
import me.apex.hades.event.impl.packetevents.FlyingEvent;
import me.apex.hades.user.User;
import me.apex.hades.util.PlayerUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

@CheckInfo(name = "Motion")
public class Motion extends Check {
    private int hits;

    private double preVLA, preVLB;

    @Override
    public void onHandle(PacketEvent e, User user) {
        if (e instanceof FlyingEvent) {
            if (!user.isInLiquid() && elapsed(user.getTick(), user.getFlyingTick()) > 40 && !user.isInWeb() && elapsed(user.getTick(), user.getTeleportTick()) > 20) {
                double max = 0.7 + PlayerUtil.getPotionEffectLevel(user.getPlayer(), PotionEffectType.JUMP) * 0.1;
                if (user.getDeltaY() > max && user.getPlayer().getVelocity().getY() < -0.075
                        && elapsed(user.getTick(), user.getVelocityTick()) > 20) {
                    flag(user, "InstantY","accelerating faster than possible on Y axis. d: " + user.getDeltaY(), false);
                }
            }

            if (user.isSprinting() && ++hits <= 2) {
                double accel = Math.abs(user.getDeltaXZ() - user.getLastDeltaXZ());
                if (accel < 0.027) {
                    if (++preVLA >= 7) {
                        flag(user, "KeepSprint","invalid acceleration, a: " + accel, false);
                    }
                } else preVLB = 0;
            }

            if (!user.isUnderBlock() && !user.getLocation().clone().subtract(0,0.2,0).getBlock().getType().equals(Material.SLIME_BLOCK)) {
                if (user.getDeltaY() == -user.getLastDeltaY() && user.getDeltaY() != 0 && elapsed(user.getTick(), user.getTeleportTick()) > 0) {
                    if (++preVLB > 1) {
                        flag(user, "SmallHop","repetitive vertical motions, m: " + user.getDeltaY(), false);
                    }
                } else preVLB = 0;
            }
        }else if(e instanceof AttackEvent) {
            if (((AttackEvent) e).getEntity() instanceof Player) {
                hits = 0;
            }
        }
    }
}
