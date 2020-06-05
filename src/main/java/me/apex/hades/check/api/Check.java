package me.apex.hades.check.api;

import me.apex.hades.Hades;
import me.apex.hades.listeners.event.HadesFlagEvent;
import me.apex.hades.objects.User;
import me.apex.hades.utils.ChatUtils;
import me.apex.hades.utils.LogUtils;
import me.apex.hades.utils.TaskUtils;
import me.purplex.packetevents.event.impl.PacketReceiveEvent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public abstract class Check{

    //Info
    public List<Violation> violations = new ArrayList<>();

    public boolean enabled, punishable, dev;

    public double vl, maxViolations;

    public long lastFlag;

    public Check() {
    	try {
            enabled = Hades.getInstance().getConfig().getBoolean("checks.detections." + getName().toLowerCase() + "." + getType().toLowerCase() + ".enabled");
            punishable = Hades.getInstance().getConfig().getBoolean("checks.detections." + getName().toLowerCase() + "." + getType().toLowerCase() + ".punishable");
            maxViolations = Hades.getInstance().getConfig().getDouble("checks.detections." + getName().toLowerCase() + "." + getType().toLowerCase() + ".max-vl");
    	}catch (Exception e) {
    		
    	}
    	
    	init();
    }
    
    public void init() { }

    protected void flag(User user, String information) {
        if (user == null || user.getPlayer() == null) return;
        lastFlag = System.currentTimeMillis();
        violations.add(new Violation(information));
        TaskUtils.run(() -> {
        	String formatColor = Hades.getInstance().getConfig().getString("lang.flag-format").replace("&", "§");
            TextComponent message = new TextComponent(formatColor.replace("%prefix%", Hades.getInstance().getPrefix()).replace("%player%", user.getPlayer().getName()).replace("%check%", getName()).replace("%checktype%", getType() + (dev ? Hades.getInstance().getConfig().getString("lang.experimental-notation") : "")).replace("%vl%", String.valueOf(violations.size())).replace("%info%", information));
            message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp "+user.getPlayer()));
            message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp "+user.getPlayer().getName()));
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7* Info\n§7* §c" + information + "\n§7* Ping: §c" + user.getPing() + "\n§7\n§7(Click to teleport)").create()));
            ChatUtils.informStaff(message, vl);
            if (Hades.getInstance().getConfig().getBoolean("system.logging.file.enabled"))
                LogUtils.logToFile(user.getLogFile(), Hades.getInstance().getConfig().getString("system.logging.log-format").replace("%player%", user.getPlayer().getName()).replace("%check%", getName()).replace("%checktype%", getType() + (dev ? Hades.getInstance().getConfig().getString("lang.experimental-notation") : "")).replace("%vl%", String.valueOf(violations.size())).replace("%info%", information));


            if (violations.size() >= maxViolations) {
                if (punishable && !user.banned) {
                    user.banned = true;
                    if (Hades.getInstance().getConfig().getBoolean("system.logging.file.enabled"))
                        LogUtils.logToFile(user.getLogFile(), "%time% > [ACTION] " + Hades.getInstance().getConfig().getString("checks.detections." + getName().toLowerCase() + ".punish-command").replace("%player%", user.getPlayer().getName()).replace("%check%", getName()).replace("%checktype%", getType() + (dev ? Hades.getInstance().getConfig().getString("lang.experimental-notation") : "")));
                    if (Hades.getInstance().getConfig().getBoolean("checks.broadcast-punishments"))
                        Bukkit.broadcastMessage(ChatUtils.color(Hades.getInstance().getConfig().getString("lang.broadcast-message").replace("%prefix%", Hades.getInstance().getPrefix()).replace("%player%", user.getPlayer().getName()).replace("%check%", getName()).replace("%newline%", "\n")));
                    TaskUtils.run(() -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Hades.getInstance().getConfig().getString("checks.detections." + getName().toLowerCase() + ".punish-command").replace("%prefix%", Hades.getInstance().getPrefix()).replace("%player%", user.getPlayer().getName()).replace("%check%", getName()).replace("%checktype%", getType() + (dev ? Hades.getInstance().getConfig().getString("lang.experimental-notation") : ""))));
                }
            }
            Bukkit.getPluginManager().callEvent(new HadesFlagEvent(user.getPlayer(), this, information));
        });
    }

    public abstract void onPacket(PacketReceiveEvent e, User user);

    //Get Check Info
    public String getName() {
        return CheckManager.INSTANCE.getCheckInfo(this).name();
    }

    public String getType() {
        return CheckManager.INSTANCE.getCheckInfo(this).type();
    }

    //Flag Util
    public void reset(long afterDelay) {
        if(System.currentTimeMillis() - lastFlag >= afterDelay)
            vl = 0;
    }
}
