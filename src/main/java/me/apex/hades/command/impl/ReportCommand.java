package me.apex.hades.command.impl;

import me.apex.hades.HadesConfig;
import me.apex.hades.command.CommandAdapter;
import me.apex.hades.command.UserInput;
import me.apex.hades.user.User;
import me.apex.hades.user.UserManager;
import me.apex.hades.util.text.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;

//There is prob much better ways for this.
public class ReportCommand extends CommandAdapter {
    @Override
    public boolean onCommand(Player player, UserInput input) {
        if (!HadesConfig.REPORT_ENABLED)return false;
        User user = UserManager.getUser(player);
         if (user.getLastReport() == null || (user.getLastReport() - System.currentTimeMillis()) >= 60000 ){
             if (input.label().equalsIgnoreCase("report")){
                 if (input.args().length > 0){
                     String[] modifiedArray = Arrays.copyOfRange(input.args(), 1, input.args().length);
                     String reason = String.join(" ", modifiedArray);
                     Player reportedPlayer = Bukkit.getPlayer(input.args()[0]);
                     if (reason.length() > 0) {
                         if (reportedPlayer != null){
                             User reportedUser = UserManager.getUser(reportedPlayer);
                             if (reportedUser != null){
                                 reportedUser.setReports(reportedUser.getReports() + 1);
                                 ChatUtil.sendMsgToStaff(" ");
                                 ChatUtil.sendMsgToStaff(HadesConfig.BASE_MESSAGE_COLOR + "Player: " + reportedPlayer.getName() + " has been reported for " + reason + "!");
                                 ChatUtil.sendMsgToStaff(" ");
                                 user.setLastReport(System.currentTimeMillis());
                             }
                         }else{
                             user.sendMessage(HadesConfig.BASE_MESSAGE_COLOR + "Player does not exist!");
                         }
                     }else{
                         if (reportedPlayer != null){
                             User reportedUser = UserManager.getUser(reportedPlayer);
                             if (reportedUser != null){
                                 reportedUser.setReports(reportedUser.getReports() + 1);
                                 ChatUtil.sendMsgToStaff(" ");
                                 ChatUtil.sendMsgToStaff(HadesConfig.BASE_MESSAGE_COLOR + "Player: " + reportedPlayer.getName() + " has been reported for " + reason + "!");
                                 ChatUtil.sendMsgToStaff(" ");
                                 user.setLastReport(System.currentTimeMillis());
                             }
                         }else{
                             user.sendMessage(HadesConfig.BASE_MESSAGE_COLOR + "Player does not exist!");
                         }
                     }
                 }else{
                     user.sendMessage(HadesConfig.BASE_MESSAGE_COLOR + "Usage: /report <player> <reason>");
                 }
                 return true;
             }

             return false;
        }else{
             user.sendMessage(HadesConfig.BASE_MESSAGE_COLOR + "To report again you need to wait for 1 minute!");
             return true;
         }
    }
}
