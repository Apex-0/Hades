package me.apex.hades.check.impl.movement;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.packetevents.FlyingEvent;
import me.apex.hades.user.User;
import me.apex.hades.util.PlayerUtil;
import org.bukkit.potion.PotionEffectType;

@CheckInfo(name = "Invalid")
public class Invalid extends Check {

     private double preVl = 0, preAvl = 0;

    @Override
    public void onHandle(PacketEvent e, User user) {
        if (e instanceof FlyingEvent) {
            if (Math.abs(((FlyingEvent) e).getPitch()) > (user.isOnClimbableBlock() ? 91.11 : 90.0)) {
                flag(user, "Pitch","invalid pitch, p: " + ((FlyingEvent) e).getPitch(), false);
            }

            if(elapsed(user.getTick(), user.getTeleportTick()) > 20 && elapsed(user.getTick(), user.getVelocityTick()) > user.getMaxVelocityTicks()  && user.isOnClimbableBlock() && !user.isOnGround() && !user.isInWeb() && elapsed(user.getTick(), user.getSlimeTick()) > 40 && elapsed(user.getTick(), user.getFlyingTick()) > 40) {
                if(user.getDeltaY() < 0 && user.isSneaking()) {
                    if(preVl++ >= 3) {
                        flag(user, "Motion", "moving downwards while sneaking on a climbable");
                        preVl = 0;
                    }
                } else {
                    preVl = Math.max(0, preVl - 0.5);
                }

                double hLimit = 0.24;
                if(PlayerUtil.getPotionEffectLevel(user.getPlayer(), PotionEffectType.SPEED) > 0) {
                    hLimit *= 1 + (PlayerUtil.getPotionEffectLevel(user.getPlayer(), PotionEffectType.SPEED) * 0.42);
                }
                if(user.getPlayer().getWalkSpeed() > 0.2f) {
                    hLimit *= 1 + ((user.getPlayer().getWalkSpeed() / 0.2f) * 0.39);
                }
                if(user.getDeltaXZ() > hLimit) {
                    if(preAvl++ >= 2) {
                        flag(user, "Motion", "moving faster than possible horizontally on a climbable");
                        preAvl = 0;
                    }
                } else {
                    preAvl = Math.max(0, preAvl - 0.5);
                }
            }
        }
    }
}