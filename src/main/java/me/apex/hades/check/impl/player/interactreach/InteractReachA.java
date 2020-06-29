package me.apex.hades.check.impl.player.interactreach;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.bukkitevents.InteractEvent;
import me.apex.hades.user.User;
import me.apex.hades.user.UserManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;

@CheckInfo(name = "InteractReach", type = "A")
public class InteractReachA extends Check {
    @Override
    public void onHandle(PacketEvent e, User user) {
        if (e instanceof InteractEvent) {
            InteractEvent packet = (InteractEvent) e;
            if (packet.getBlockClicked() != null) {
                double dist = user.getPlayer().getEyeLocation().toVector().distance(packet.getBlockClicked().getLocation().toVector());
                if (dist > 6) {
                    if (++preVL > 1) {
                        flag(user, "interacted block farther than possible. d: " + dist);
                    }
                } else preVL = 0;
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        User user = UserManager.getUser(event.getPlayer());
        double dist = user.getPlayer().getEyeLocation().toVector().distance(event.getBlockPlaced().getLocation().toVector());
        if (dist > 4.8) {
            if (++preVL > 1) {
                flag(user, "placed block farther than possible. d: " + dist);
            }
        } else preVL = 0;
    }
}
