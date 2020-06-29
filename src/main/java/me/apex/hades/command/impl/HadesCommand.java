package me.apex.hades.command.impl;

import me.apex.hades.HadesConfig;
import me.apex.hades.HadesPlugin;
import me.apex.hades.check.Check;
import me.apex.hades.command.CommandAdapter;
import me.apex.hades.command.UserInput;
import me.apex.hades.user.User;
import me.apex.hades.user.UserManager;
import me.apex.hades.util.text.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class HadesCommand extends CommandAdapter {
    @Override
    public boolean onCommand(Player player, UserInput input) {
        User user = UserManager.getUser(player);
        if (input.label().equalsIgnoreCase(HadesConfig.BASE_COMMAND)){
            if (player.hasPermission(HadesConfig.BASE_PERMISSION + ".command")){
                if(input.args().length <= 0){
                    user.sendMessage("&8&m----------------------------------------");
                    user.sendMessage(HadesConfig.BASE_MESSAGE_COLOR + "/" + HadesConfig.BASE_COMMAND + " &fgui : " + "&7View control panel");
                    user.sendMessage(HadesConfig.BASE_MESSAGE_COLOR + "/" + HadesConfig.BASE_COMMAND + " &finfo <player> : " + "&7View information of a player");
                    user.sendMessage(HadesConfig.BASE_MESSAGE_COLOR + "/" + HadesConfig.BASE_COMMAND + " &freload : " + "&7Reload entire anticheat and configuration");
                    user.sendMessage(HadesConfig.BASE_MESSAGE_COLOR + "/" + HadesConfig.BASE_COMMAND + " &falerts : " + "&7Enable or disable anticheat alerts");
                    user.sendMessage("&8&m----------------------------------------");
                }else if (input.args().length >= 0){
                    if (input.args()[0].equalsIgnoreCase("info")){
                        if(input.args().length > 1){
                            Player target = Bukkit.getPlayer(input.args()[1]);
                            if (target != null){
                                User t = UserManager.getUser(target);
                                int totalViolations = 0;
                                for (Check check : t.getChecks()) totalViolations += check.vl;

                                user.sendMessage("&8&m----------------------------------------");
                                user.sendMessage(HadesConfig.BASE_MESSAGE_COLOR + "&l" + target.getName() + "'s Information");
                                user.sendMessage(" ");
                                user.sendMessage(HadesConfig.BASE_MESSAGE_COLOR + "Total Violations: &f" + totalViolations);
                                user.sendMessage(HadesConfig.BASE_MESSAGE_COLOR + "Lunar Client&f: " + user.isUsingLunarClient());
                                user.sendMessage(HadesConfig.BASE_MESSAGE_COLOR + "Ping: &f" + t.ping());
                                user.sendMessage(HadesConfig.BASE_MESSAGE_COLOR + "IP Address: &f" + user.getPlayer().getAddress());
                                user.sendMessage("&8&m----------------------------------------");
                            }else user.sendMessage(HadesConfig.BASE_MESSAGE_COLOR + "Player is not online or does not exist!");
                        }else user.sendMessage(HadesConfig.BASE_MESSAGE_COLOR + "Usage: /hades info <player>");
                    }else if (input.args()[0].equalsIgnoreCase("reload")){
                        if (HadesPlugin.getInstance().reloadPlugin()){
                            user.sendMessage("&aSuccessfully reloaded Hades! (BETA)");
                        }else user.sendMessage("&cError whilst reloading Hades. Restart your server or reset your config! (BETA)");
                    }else if (input.args()[0].equalsIgnoreCase("gui")){
                        //TODO Implement gui and run it here
                    }else if (input.args()[0].equalsIgnoreCase("alerts")) {
                        if (player.hasPermission(HadesConfig.BASE_PERMISSION + ".alerts")) {
                            if(input.args().length > 1) {
                                try{
                                    int flagDelay = Integer.valueOf(input.args()[0]);
                                    user.setAlerts(true);
                                    user.setFlagDelay(flagDelay);
                                    user.sendMessage(ChatUtil.color(user.isAlerts() ? HadesConfig.ENABLE_ALERTS_MESSAGE.replace("%flagsetting%", "" + flagDelay) : HadesConfig.DISABLE_ALERTS_MESSAGE));
                                }catch (Exception e) {
                                    if(input.args()[1].equalsIgnoreCase("dev")) {
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
                        }else{
                            user.sendMessage(HadesConfig.NO_PERMISSION);
                        }
                    }
                }
            }
        }

        return false;
    }
}
