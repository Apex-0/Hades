package me.apex.hades.menu.impl;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.apex.hades.Hades;
import me.apex.hades.menu.api.Gui;
import me.apex.hades.menu.api.ItemBuilder;
import me.apex.hades.utils.ChatUtils;

public class HomeMenu extends Gui {

    public HomeMenu() {
        super(Hades.getInstance().getConfig().getString("lang.base-message-color") + "Hades Panel", 27);
        ItemBuilder one = new ItemBuilder(Hades.getInstance().getConfig().getString("lang.base-message-color") + "View Logs", Material.BOOK_AND_QUILL);
        one.addLore("&7Click to view player logs.");
        ItemBuilder two = new ItemBuilder(Hades.getInstance().getConfig().getString("lang.base-message-color") + "Anticheat Information", Material.SIGN);
        two.addLore(" ");
        two.addLore("&7Version: " + Hades.getInstance().getConfig().getString("lang.base-message-color") + Hades.getInstance().getDescription().getVersion());
        two.addLore(" ");
        two.addLore("&7If you have and issues or questions, please");
        two.addLore("&fLeft Click &7to get the link to our Support Discord.");
        ItemBuilder three = new ItemBuilder(Hades.getInstance().getConfig().getString("lang.base-message-color") + "Reload Anticheat", Material.WATCH);
        three.addLore("&7Click to reload anticheat.");
        items.put(one.getBukkitItem(), 11);
        items.put(two.getBukkitItem(), 13);
        items.put(three.getBukkitItem(), 15);
    }

    @Override
    public void onClick(Player player, ItemStack item) {
        if (item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatUtils.color(Hades.getInstance().getConfig().getString("lang.base-message-color") + "Anticheat Information"))) {
            player.sendMessage(ChatUtils.color(Hades.getInstance().getConfig().getString("lang.base-message-color") + "Our Support Discord: &7Gone :("));
        }
        if (item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatUtils.color(Hades.getInstance().getConfig().getString("lang.base-message-color") + "Reload Anticheat"))) {
            long start = System.currentTimeMillis();
            if (Hades.getInstance().reloadPlugin()) {
                player.sendMessage(ChatUtils.color(Hades.getInstance().getConfig().getString("lang.base-message-color") + "Successfully reloaded anticheat! (" + Math.abs(start - System.currentTimeMillis()) + "ms)"));
            } else
                player.sendMessage(ChatUtils.color(Hades.getInstance().getConfig().getString("lang.base-message-color") + "Failed to reloaded anticheat!"));
        }
    }

}
