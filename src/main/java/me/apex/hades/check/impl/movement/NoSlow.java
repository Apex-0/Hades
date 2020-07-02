package me.apex.hades.check.impl.movement;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.packetevents.FlyingEvent;
import me.apex.hades.user.User;
import me.apex.hades.user.UserManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;

@CheckInfo(name = "NoSlow")
public class NoSlow extends Check {

    @Override
    public void onHandle(PacketEvent e, User user) {
        if (e instanceof FlyingEvent) {

        }
    }

    @EventHandler
    public void onBowShoow(EntityShootBowEvent event){
        if (event.getEntity() instanceof Player){
            User user = UserManager.getUser((Player) event.getEntity());
            if (!user.getPlayer().isInsideVehicle() && user.getSprintingTicks() > 5){
                flag(user, "NoBowSlow", "sprinting while shooting a bow!", false);
            }
        }
    }
}
