package me.apex.hades.check.impl.combat;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.packetevents.AttackEvent;
import me.apex.hades.user.User;
import org.bukkit.block.BlockFace;

@CheckInfo(name = "Criticals")
public class Criticals extends Check {

    private double preVLA;

    @Override
    public void onHandle(PacketEvent e, User user) {
        if (e instanceof AttackEvent) {
            if (user.getLocation().getBlock().getRelative(BlockFace.DOWN).isLiquid()
                    || user.getLocation().getBlock().getRelative(BlockFace.UP).isLiquid())
                return;

            if (!user.isOnGround() && !user.getPlayer().getAllowFlight()) {
                if (user.getLocation().getY() % 1.0D == 0.0D) {
                    if (user.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().isSolid()) {
                        if (preVLA++ >= 2) {
                            flag(user, "Ground","tried to hit critical on ground!", false);
                        }
                    }
                } else preVLA = 0;
            }
        }
    }
}
