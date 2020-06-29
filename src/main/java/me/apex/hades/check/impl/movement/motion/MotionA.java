package me.apex.hades.check.impl.movement.motion;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.packetevents.FlyingEvent;
import me.apex.hades.user.User;
import me.apex.hades.util.PlayerUtil;
import org.bukkit.potion.PotionEffectType;

@CheckInfo(name = "Motion", type = "A")
public class MotionA extends Check {
    @Override
    public void onHandle(PacketEvent e, User user) {
        if (e instanceof FlyingEvent) {
            if (!user.isInLiquid() && elapsed(user.getTick(), user.getFlyingTick()) > 40 && !user.isInWeb() && elapsed(user.getTick(), user.getTeleportTick()) > 20) {
                double max = 0.7 + PlayerUtil.getPotionEffectLevel(user.getPlayer(), PotionEffectType.JUMP) * 0.1;
                if (user.getDeltaY() > max && user.getPlayer().getVelocity().getY() < -0.075
                        && elapsed(user.getTick(), user.getVelocityTick()) > 20) {
                    flag(user, "accelerating faster than possible on Y axis. d: " + user.getDeltaY());
                }
            }
        }
    }
}
