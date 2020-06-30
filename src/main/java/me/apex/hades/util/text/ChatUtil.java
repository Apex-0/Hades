package me.apex.hades.util.text;

import me.apex.hades.HadesConfig;
import me.apex.hades.user.UserManager;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;

public class ChatUtil {

    public static String color(String in) {
        return ChatColor.translateAlternateColorCodes('&', in);
    }

    public static void informStaff(TextComponent alert, TextComponent dev, int vl) {
        UserManager.users.stream().filter(user -> user.isAlerts() && user.getFlagDelay() > 0 && vl % user.getFlagDelay() == 0).forEach(user -> user.getPlayer().spigot().sendMessage(alert));
        UserManager.users.stream().filter(user -> user.isAlerts() && user.getFlagDelay() == 0).forEach(user -> user.getPlayer().spigot().sendMessage(dev));
    }

    public static void sendMsgToStaff(String message){
        UserManager.users.stream().filter(user -> user.isAlerts()).forEach(user -> user.sendMessage(HadesConfig.PREFIX + " " + color(message)));
    }
}
