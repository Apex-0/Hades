package me.apex.hades;

import me.apex.hades.check.CheckManager;
import me.apex.hades.command.CommandManager;
import me.apex.hades.command.impl.HadesCommand;
import me.apex.hades.util.text.ChatUtil;

import java.util.HashMap;
import java.util.Map;

public class HadesConfig {

    //Test Mode
    public static boolean TEST_MODE;

    //Language Settings
    public static String PREFIX;
    public static String NO_PERMISSION;
    public static String BASE_MESSAGE_COLOR;
    public static String ENABLE_ALERTS_MESSAGE;
    public static String DISABLE_ALERTS_MESSAGE;

    //System Settings
    public static String BASE_PERMISSION;
    public static String LOG_FORMAT;
    public static boolean LOG_TO_FILE;
    public static boolean LOG_TO_CONSOLE;

    //Anti-VPN
    public static boolean ANTIVPN_ENABLED;
    public static String ANTIVPN_PUNISH_COMMAND;

    //Check Settings
    public static int MAX_CPS;
    public static float MAX_REACH;

    public static boolean EXEMPT_PLAYER;
    public static boolean BROADCAST_PUNISHMENTS;
    public static String PUNISHMENT_BROADCAST_MESSAGE;
    public static String FLAG_FORMAT;
    public static String DEV_FLAG_FORMAT;

    public static Map<String, Boolean> ENABLED_CHECKS = new HashMap<>();
    public static Map<String, Boolean> PUNISHABLE_CHECKS = new HashMap<>();
    public static Map<String, Integer> MAX_VIOLATIONS = new HashMap<>();
    public static Map<String, String> PUNISH_COMMANDS = new HashMap<>();

    public static void updateSettings() {
        try{
            TEST_MODE = HadesPlugin.getInstance().getConfig().getBoolean("test-mode");

            PREFIX = ChatUtil.color(HadesPlugin.getInstance().getConfig().getString("lang.prefix"));
            NO_PERMISSION = ChatUtil.color(HadesPlugin.getInstance().getConfig().getString("lang.no-permission"));
            BASE_MESSAGE_COLOR = ChatUtil.color(HadesPlugin.getInstance().getConfig().getString("lang.base-message-color"));
            ENABLE_ALERTS_MESSAGE = ChatUtil.color(HadesPlugin.getInstance().getConfig().getString("lang.enable-alerts-message"));
            DISABLE_ALERTS_MESSAGE = ChatUtil.color(HadesPlugin.getInstance().getConfig().getString("lang.disable-alerts-message"));

            BASE_PERMISSION = "hades";
            LOG_FORMAT = HadesPlugin.getInstance().getConfig().getString("system.logging.log-format");
            LOG_TO_FILE = HadesPlugin.getInstance().getConfig().getBoolean("system.logging.log-to-file");
            LOG_TO_CONSOLE = HadesPlugin.getInstance().getConfig().getBoolean("system.logging.log-to-console");

            ANTIVPN_ENABLED = HadesPlugin.getInstance().getConfig().getBoolean("anti-vpn.enabled");
            ANTIVPN_PUNISH_COMMAND = ChatUtil.color(HadesPlugin.getInstance().getConfig().getString("anti-vpn.punish-command"));

            MAX_CPS = HadesPlugin.getInstance().getConfig().getInt("Max-CPS");
            MAX_REACH = (float)HadesPlugin.getInstance().getConfig().getDouble("Max-Reach");

            EXEMPT_PLAYER = HadesPlugin.getInstance().getConfig().getBoolean("checks.exempt-player");
            BROADCAST_PUNISHMENTS = HadesPlugin.getInstance().getConfig().getBoolean("checks.broadcast-punishments");
            PUNISHMENT_BROADCAST_MESSAGE = ChatUtil.color(HadesPlugin.getInstance().getConfig().getString("checks.broadcast-message"));
            FLAG_FORMAT = ChatUtil.color(HadesPlugin.getInstance().getConfig().getString("checks.flag-format"));
            DEV_FLAG_FORMAT = ChatUtil.color(HadesPlugin.getInstance().getConfig().getString("checks.dev-flag-format"));

            ENABLED_CHECKS.clear();
            PUNISHABLE_CHECKS.clear();
            MAX_VIOLATIONS.clear();
            PUNISH_COMMANDS.clear();

            for(Class check : CheckManager.CHECKS) {
                String checkName = check.getSimpleName().substring(0, check.getSimpleName().length() - 1);
                String checkType = String.valueOf(check.getSimpleName().toCharArray()[check.getSimpleName().length() - 1]);

                boolean enabled = HadesPlugin.getInstance().getConfig().getBoolean("checks.detections." + checkName.toLowerCase() + "." + checkType.toLowerCase() + ".enabled");
                boolean punishable = HadesPlugin.getInstance().getConfig().getBoolean("checks.detections." + checkName.toLowerCase() + "." + checkType.toLowerCase() + ".punishable");
                int maxVL = HadesPlugin.getInstance().getConfig().getInt("checks.detections." + checkName.toLowerCase() + "." + checkType.toLowerCase() + ".max-violations");
                String punishCommand = HadesPlugin.getInstance().getConfig().getString("checks.detections." + checkName.toLowerCase() + "." + checkType.toLowerCase() + ".punish-command");

                ENABLED_CHECKS.put(check.getSimpleName(), enabled);
                PUNISHABLE_CHECKS.put(check.getSimpleName(), punishable);
                MAX_VIOLATIONS.put(check.getSimpleName(), maxVL);
                PUNISH_COMMANDS.put(check.getSimpleName(), punishCommand);
            }

            CommandManager.getAdapters().clear();
            CommandManager.register(new HadesCommand());
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
