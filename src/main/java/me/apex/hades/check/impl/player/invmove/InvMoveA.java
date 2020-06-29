package me.apex.hades.check.impl.player.invmove;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.user.User;
import me.apex.hades.user.UserManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;

@CheckInfo(name = "InvMove", type = "A")
public class InvMoveA extends Check implements Listener {

    @Override
    public void onHandle(PacketEvent e, User user) {
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        User user = UserManager.getUser((Player) event.getWhoClicked());
        if (user != null) {
            if (user.isInWeb() || user.isInLiquid() || user.isOnClimbableBlock() || elapsed(time(), user.getVelocityTick()) < 40 || event.getClick() == ClickType.CREATIVE || event.getAction() == InventoryAction.PLACE_ALL)
                return;

            if (user.isOnGround() && user.getDeltaXZ() > 0.1) {
                if (++preVL > 2) {
                    flag(user, "player used inventory while moving.");
                }
            } else preVL = 0;
        }
    }
}
