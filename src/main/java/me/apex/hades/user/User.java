package me.apex.hades.user;

import lombok.Getter;
import lombok.Setter;
import me.apex.hades.HadesConfig;
import me.apex.hades.HadesPlugin;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckManager;
import me.apex.hades.util.PlayerUtil;
import me.apex.hades.util.reflection.ReflectionUtil;
import me.apex.hades.util.text.ChatUtil;
import me.apex.hades.util.text.LogUtils;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Getter
@Setter
public class User {

    //Player
    private final Player player;
    private final UUID playerUUID;

    //Checks
    private final List<Check> checks;
    //Booleans
    private boolean alerts, usingLunarClient, onGround, collidedGround, digging, isSprinting, isSneaking, chunkLoaded, verifyingVelocity;
    //Location
    private Location location, lastLocation, lastOnGroundLocation;
    //Ticks
    private int flagDelay, tick, digTick, iceTick, iceTicks, slimeTick, slimeTicks, velocityTick, lastVelocityId, underBlockTick, teleportTick, liquidTick, liquidTicks, airTick, airTicks, groundTick, groundTicks, totalBlockUpdates, solidLiquidTicks, climbableTick, climbableTicks, serverGroundTick, optifineTick, flyingTick, sprintingTicks = 0;
    //Deltas
    private double deltaY, lastDeltaY, deltaXZ, lastDeltaXZ, mouseSensitivity;
    private float deltaYaw, lastDeltaYaw, deltaPitch, lastDeltaPitch, deltaAngle;
    //Ints
    private int CPS;
    //Interact
    private boolean rightClickingBlock, rightClickingAir, leftClickingBlock, leftClickingAir;
    private Block interactedBlock;
    //Velocity
    private long timeStamp, lastAnyBlockWithLiquid;
    private double velocityX, velocityY, velocityZ;
    //Log
    private LogUtils.TextFile logFile;
    //Thread
    private Executor executorService;
    private long joinTime;
    //Direction
    private Vector direction;

    List<Location>locations = new ArrayList<>();

    public User(Player player) {
        this.player = player;
        this.location = player.getLocation();
        this.playerUUID = player.getUniqueId();
        this.checks = CheckManager.loadChecks();
        this.timeStamp = System.currentTimeMillis();
        this.executorService = Executors.newSingleThreadExecutor();
        if (HadesPlugin.getInstance().getConfig().getBoolean("system.logging.file.enabled")) {
            logFile = new LogUtils.TextFile("" + playerUUID, HadesPlugin.getInstance().getConfig().getString("system.logging.file.path"));
        }

        if(HadesConfig.TEST_MODE) {
            alerts = true;
            flagDelay = 0;
        }
    }

    public boolean hasBlocksAround() {
        return PlayerUtil.hasBlocksAround(location) && PlayerUtil.hasBlocksAround(location.add(0, 1, 0));
    }

    public boolean isOnClimbableBlock() {
        return PlayerUtil.isOnClimbable(player);
    }

    public boolean isInLiquid() {
        return PlayerUtil.isInLiquid(player);
    }

    public boolean isInWeb() {
        return PlayerUtil.isInWeb(player);
    }

    public boolean isUnderBlock() {
        return PlayerUtil.blockNearHead(player);
    }

    //Cant do this without reflection!
    public int ping() {
        int ping = 0;
        try {
            ping = ReflectionUtil.getPlayerPing(player);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ping;
    }

    public String address() {
        return player.getAddress().getHostName();
    }

    //Send Message
    public void sendMessage(String message) {
        player.sendMessage(ChatUtil.color(message.replace("%prefix%", HadesConfig.PREFIX).replace("%player%", player.getName())));
    }
}
