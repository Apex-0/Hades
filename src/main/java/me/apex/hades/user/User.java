package me.apex.hades.user;

import lombok.Getter;
import lombok.Setter;
import me.apex.hades.HadesConfig;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckManager;
import me.apex.hades.util.PlayerUtil;
import me.apex.hades.util.ReachUtil;
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
    //Reach
    private ReachUtil reachLoc, lastReachLoc;
    private Player lastTarget;
    private long lastAttackPacket, lastTransction;
    //Ticks
    private int flagDelay, tick, digTick, iceTick, iceTicks, slimeTick, slimeTicks, velocityTick, velocityId, maxVelocityTicks, underBlockTick, nearWallTick, teleportTick, liquidTick, liquidTicks, airTick, airTicks, groundTick, groundTicks, totalBlockUpdates, solidLiquidTicks, climbableTick, climbableTicks, serverGroundTick, optifineTick, flyingTick, sprintingTicks = 0, velocityVerifications;
    //Deltas
    private double deltaY, lastDeltaY, deltaXZ, lastDeltaXZ, mouseSensitivity;
    private float deltaYaw, lastDeltaYaw, deltaPitch, lastDeltaPitch, deltaAngle, lastDeltaAngle;
    //Ints
    private int CPS, reports;
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
    //Long
    private Long lastReport;

    List<Location>locations = new ArrayList<>();

    public User(Player player) {
        this.player = player;
        this.location = player.getLocation();
        this.playerUUID = player.getUniqueId();
        this.checks = CheckManager.loadChecks();
        this.timeStamp = System.currentTimeMillis();
        this.executorService = Executors.newSingleThreadExecutor();
        if (HadesConfig.LOG_TO_FILE) {
            logFile = new LogUtils.TextFile("" + playerUUID, "\\\\logs");
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

    public boolean isNearWall() {
        return PlayerUtil.nearWall(player);
    }

    public int airTicks() {
        return Math.abs(tick - airTick);
    }

    public int groundTicks() {
        return Math.abs(tick - groundTick);
    }

    public int iceTicks() {
        return Math.abs(tick - iceTick);
    }

    public int slimeTicks() {
        return Math.abs(tick - slimeTick);
    }

    public int liquidTicks() {
        return Math.abs(tick - liquidTick);
    }

    public int climbableTicks() {
        return Math.abs(tick - climbableTick);
    }

    public int underBlockTicks() {
        return Math.abs(tick - underBlockTick);
    }

    public int nearWallTicks() {
        return Math.abs(tick - nearWallTick);
    }

    public int velocityTicks() {
        return Math.abs(tick - velocityTick);
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
