package me.apex.hades.command.impl;

import me.apex.hades.HadesConfig;
import me.apex.hades.command.CommandAdapter;
import me.apex.hades.command.UserInput;
import me.apex.hades.user.User;
import me.apex.hades.user.UserManager;
import me.apex.hades.util.text.ChatUtil;
import org.bukkit.entity.Player;

public class AlertCommand extends CommandAdapter {

    @Override
    public boolean onCommand(Player player, UserInput input) {
        if(input.label().equalsIgnoreCase("alerts")) {
            User user = UserManager.getUser(player);
            if(input.args().length > 0) {
                try{
                    int flagDelay = Integer.valueOf(input.args()[0]);
                    user.setAlerts(true);
                    user.setFlagDelay(flagDelay);
                    user.sendMessage(ChatUtil.color(user.isAlerts() ? HadesConfig.ENABLE_ALERTS_MESSAGE.replace("%flagsetting%", "" + flagDelay) : HadesConfig.DISABLE_ALERTS_MESSAGE));
                }catch (Exception e) {
                    if(input.args()[0].equalsIgnoreCase("dev")) {
                        user.setAlerts(true);
                        user.setFlagDelay(0);
                        user.sendMessage(ChatUtil.color(user.isAlerts() ? HadesConfig.ENABLE_ALERTS_MESSAGE.replace("%flagsetting%", "dev") : HadesConfig.DISABLE_ALERTS_MESSAGE));
                    }else {
                        user.setAlerts(!user.isAlerts());
                        user.setFlagDelay(8);
                        user.sendMessage(ChatUtil.color(user.isAlerts() ? HadesConfig.ENABLE_ALERTS_MESSAGE.replace("%flagsetting%", "" + 8) : HadesConfig.DISABLE_ALERTS_MESSAGE));
                    }
                }
            }else {
                user.setAlerts(!user.isAlerts());
                user.setFlagDelay(8);
                user.sendMessage(ChatUtil.color(user.isAlerts() ? HadesConfig.ENABLE_ALERTS_MESSAGE.replace("%flagsetting%", "" + 8) : HadesConfig.DISABLE_ALERTS_MESSAGE));
            }
            return true;
        }
        return false;
    }

}
