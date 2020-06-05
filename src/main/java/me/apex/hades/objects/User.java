package me.apex.hades.objects;

import me.apex.hades.Hades;
import me.apex.hades.check.api.Check;
import me.apex.hades.check.api.CheckManager;
import me.apex.hades.utils.ChatUtils;
import me.apex.hades.utils.LogUtils;
import me.apex.hades.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class User {

    private final List<Check> checks;
    private final Player player;
    private final UUID playerUUID;
    private Location location, lastLocation;
    private boolean alerts, lagging, digging, flyAFix;
    private double deltaY, lastDeltaY, deltaXZ, lastDeltaXZ, lastVelX, lastVelY, lastVelZ, optifineTicks, iceTicks, hitTicks, slimeTicks, velocityTicks, teleportTicks, airTicks, groundTicks, clientGroundTicks;
    private float deltaYaw, lastDeltaYaw, deltaPitch, lastDeltaPitch, lastYawDiff, lastPitchDiff;
    private long lastKeepAlive, lastServerKeepAlive, lastJoin, lastPacket, lastLagPacket, lastLagSet, lastVelocity, lastServerPosition;
    private String address;
    private int ping, flagDelay;
    private LogUtils.TextFile logFile;
    private Deque<Long> transactionQueue = new LinkedList<>();
    public boolean banned;

    public User(UUID playerUUID, String address) {
    	this.player = Bukkit.getPlayer(playerUUID);
        this.playerUUID = playerUUID;
        this.address = address;
        checks = CheckManager.INSTANCE.loadChecks();
        if (Hades.getInstance().getConfig().getBoolean("system.logging.file.enabled")) {
            logFile = new LogUtils.TextFile("" + playerUUID, Hades.getInstance().getConfig().getString("system.logging.file.path"));
        }
    }
    
    public List<Check> getChecks() {
    	return checks;
    }

    public Player getPlayer() {
        return player;
    }
    
    public UUID getPlayerUUID() {
    	return playerUUID;
    }
    
    public Location getLocation() {
    	return location;
    }
    
    public void setLocation(Location location) {
    	this.location = location;
    }
    
    public Location getLastLocation() {
    	return lastLocation;
    }
    
    public void setLastLocation(Location lastLocation) {
    	this.lastLocation = lastLocation;
    }
    
    public boolean isAlerts() {
    	return alerts;
    }
    
    public void setAlerts(boolean alerts) {
    	this.alerts = alerts;
    }
    
    public boolean isLagging() {
    	return lagging;
    }
    
    public void setLagging(boolean lagging) {
    	this.lagging = lagging;
    }

    public void setFlyAFix(boolean flyAFix) { this.flyAFix = flyAFix; }

    public boolean getFlyAFix() { return flyAFix; }

    public long getLastServerPosition() { return lastServerPosition; }

    public void setLastServerPosition(long lastServerPosition){ this.lastServerPosition = lastServerPosition; }

    public boolean isDigging() {
    	return digging;
    }
    
    public void setDigging(boolean digging) {
    	this.digging = digging;
    }
    
    public double getDeltaY() {
    	return deltaY;
    }
    
    public void setDeltaY(double deltaY) {
    	this.deltaY = deltaY;
    }

    public long getLastVelocity(){ return lastVelocity;}

    public void setLastVelocity(long lastVelocity) { this.lastVelocity = lastVelocity; }
    
    public double getLastDeltaY() {
    	return lastDeltaY;
    }
    
    public void setLastDeltaY(double lastDeltaY) {
    	this.lastDeltaY = lastDeltaY;
    }
    
    public double getDeltaXZ() {
    	return deltaXZ;
    }
    
    public void setDeltaXZ(double deltaXZ) {
    	this.deltaXZ = deltaXZ;
    }
    
    public double getLastDeltaXZ() {
    	return lastDeltaXZ;
    }
    
    public void setLastDeltaXZ(double lastDeltaXZ) {
    	this.lastDeltaXZ = lastDeltaXZ;
    }
    
    public double getLastVelX() {
    	return lastVelX;
    }
    
    public void setLastVelX(double lastVelX) {
    	this.lastVelX = lastVelX;
    }
    
    public double getLastVelY() {
    	return lastVelY;
    }
    
    public void setLastVelY(double lastVelY) {
    	this.lastVelY = lastVelY;
    }
    
    public double getLastVelZ() {
    	return lastVelZ;
    }
    
    public void setLastVelZ(double lastVelZ) {
    	this.lastVelZ = lastVelZ;
    }
    
    public double getOptifineTicks() {
    	return optifineTicks;
    }
    
    public void setOptifineTicks(double optifineTicks) {
    	this.optifineTicks = optifineTicks;
    }
    
    public float getDeltaYaw() {
    	return deltaYaw;
    }
    
    public void setDeltaYaw(float deltaYaw) {
    	this.deltaYaw = deltaYaw;
    }
    
    public float getLastDeltaYaw() {
    	return lastDeltaYaw;
    }
    
    public void setLastDeltaYaw(float lastDeltaYaw) {
    	this.lastDeltaYaw = lastDeltaYaw;
    }
    
    public float getDeltaPitch() {
    	return deltaPitch;
    }
    
    public void setDeltaPitch(float deltaPitch) {
    	this.deltaPitch = deltaPitch;
    }
    
    public float getLastDeltaPitch() {
    	return lastDeltaPitch;
    }
    
    public void setLastDeltaPitch(float lastDeltaPitch) {
    	this.lastDeltaPitch = lastDeltaPitch;
    }
    
    public float getLastYawDiff() {
    	return lastYawDiff;
    }
    
    public void setLastYawDiff(float lastYawDiff) {
    	this.lastYawDiff = lastYawDiff;
    }
    
    public float getLastPitchDiff() {
    	return lastPitchDiff;
    }
    
    public void setLastPitchDiff(float lastPitchDiff) {
    	this.lastPitchDiff = lastPitchDiff;
    }
    
    public double getIceTicks() {
    	return iceTicks;
    }
    
    public void setIceTicks(double iceTicks) {
    	this.iceTicks = iceTicks;
    }
    
    public double getHitTicks() {
    	return hitTicks;
    }
    
    public void setHitTicks(double hitTicks) {
    	this.hitTicks = hitTicks;
    }
    
    public double getSlimeTicks() {
    	return slimeTicks;
    }
    
    public void setSlimeTicks(double slimeTicks) {
    	this.slimeTicks = slimeTicks;
    }
    
    public double getVelocityTicks() {
    	return velocityTicks;
    }
    
    public void setVelocityTicks(double velocityTicks) {
    	this.velocityTicks = velocityTicks;
    }
    
    public double getTeleportTicks() {
    	return teleportTicks;
    }
    
    public void setTeleportTicks(double teleportTicks) {
    	this.teleportTicks = teleportTicks;
    }
    
    public double getAirTicks() {
    	return airTicks;
    }
    
    public void setAirTicks(double airTicks) {
    	this.airTicks = airTicks;
    }
    
    public double getGroundTicks() {
    	return groundTicks;
    }
    
    public void setGroundTicks(double groundTicks) {
    	this.groundTicks = groundTicks;
    }
    
    public double getClientGroundTicks() {
    	return clientGroundTicks;
    }
    
    public void setClientGroundTicks(double clientGroundTicks) {
    	this.clientGroundTicks = clientGroundTicks;
    }
    
    public long getLastKeepAlive() {
    	return lastKeepAlive;
    }
    
    public void setLastKeepAlive(long lastKeepAlive) {
    	this.lastKeepAlive = lastKeepAlive;
    }
    
    public long getLastServerKeepAlive() {
    	return lastServerKeepAlive;
    }
    
    public void setLastServerKeepAlive(long lastServerKeepAlive) {
    	this.lastServerKeepAlive = lastServerKeepAlive;
    }
    
    public long getLastJoin() {
    	return lastJoin;
    }
    
    public void setLastJoin(long lastJoin) {
    	this.lastJoin = lastJoin;
    }
    
    public long getLastPacket() {
    	return lastPacket;
    }
    
    public void setLastPacket(long lastPacket) {
    	this.lastPacket = lastPacket;
    }
    
    public long getLastLagPacket() {
    	return lastLagPacket;
    }
    
    public void setLastLagPacket(long lastLagPacket) {
    	this.lastLagPacket = lastLagPacket;
    }
    
    public long getLastLagSet() {
    	return lastLagSet;
    }
    
    public void setLastLagSet(long lastLagSet) {
    	this.lastLagSet = lastLagSet;
    }
    
    public String getAddress() {
    	return address;
    }
    
    public void setAddress(String address) {
    	this.address = address;
    }
    
    public int getPing() {
    	return ping;
    }
    
    public void setPing(int ping) {
    	this.ping = ping;
    }
    
    public int getFlagDelay() {
    	return flagDelay;
    }
    
    public void setFlagDelay(int flagDelay) {
    	this.flagDelay = flagDelay;
    }
    
    public LogUtils.TextFile getLogFile() {
    	return logFile;
    }
    
    public void setLogFile(LogUtils.TextFile logFile) {
    	this.logFile = logFile;
    }
    
    public Deque<Long> getTransactionQueue() {
    	return transactionQueue;
    }
    
    public void setTransactionQueue(Deque<Long> transactionQueue) {
    	this.transactionQueue = transactionQueue;
    }
    
    public boolean isBanned() {
    	return banned;
    }
    
    public void setBanned(boolean banned) {
    	this.banned = banned;
    }
    
    //Global
    public boolean onGround() {
        return PlayerUtils.isNearGround(player.getLocation());
    }

    public boolean onIce() {
        return PlayerUtils.isOnIce(getPlayer());
    }

    public boolean underBlock() {
        return PlayerUtils.blockNearHead(getPlayer());
    }

    public void sendMessage(String message) {
        getPlayer().sendMessage(ChatUtils.color(message));
    }

    public boolean isUsingOptifine() {
        return optifineTicks > 0.0D;
    }
}
