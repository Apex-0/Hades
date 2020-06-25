package me.apex.hades.listener;

import io.github.retrooper.packetevents.annotations.PacketHandler;
import io.github.retrooper.packetevents.enums.EntityUseAction;
import io.github.retrooper.packetevents.enums.PlayerAction;
import io.github.retrooper.packetevents.enums.PlayerDigType;
import io.github.retrooper.packetevents.event.PacketEvent;
import io.github.retrooper.packetevents.event.PacketListener;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketSendEvent;
import io.github.retrooper.packetevents.event.impl.PlayerInjectEvent;
import io.github.retrooper.packetevents.event.impl.PlayerUninjectEvent;
import io.github.retrooper.packetevents.packet.Packet;
import io.github.retrooper.packetevents.packetwrappers.in.blockdig.WrappedPacketInBlockDig;
import io.github.retrooper.packetevents.packetwrappers.in.blockplace.WrappedPacketInBlockPlace;
import io.github.retrooper.packetevents.packetwrappers.in.chat.WrappedPacketInChat;
import io.github.retrooper.packetevents.packetwrappers.in.entityaction.WrappedPacketInEntityAction;
import io.github.retrooper.packetevents.packetwrappers.in.flying.WrappedPacketInFlying;
import io.github.retrooper.packetevents.packetwrappers.in.keepalive.WrappedPacketInKeepAlive;
import io.github.retrooper.packetevents.packetwrappers.in.useentity.WrappedPacketInUseEntity;
import io.github.retrooper.packetevents.packetwrappers.out.entityvelocity.WrappedPacketOutEntityVelocity;
import me.apex.hades.HadesPlugin;
import me.apex.hades.event.impl.packetevents.*;
import me.apex.hades.processor.MovementProcessor;
import me.apex.hades.user.User;
import me.apex.hades.user.UserManager;
import me.apex.hades.util.PacketUtil;
import me.apex.hades.util.TaskUtil;
import me.apex.hades.util.text.ChatUtil;
import me.apex.hades.util.vpn.VPNChecker;
import org.bukkit.Bukkit;

import java.util.Random;

public class NetworkListener implements PacketListener {

    @PacketHandler
    public void onInject(PlayerInjectEvent e) {
        User user = new User(e.getPlayer());
        UserManager.users.add(user);

        //Check for VPN
        if (VPNChecker.INSTANCE.checkUser(user)) {
            TaskUtil.task(() -> {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ChatUtil.color(HadesPlugin.getInstance().getConfig().getString("anti-vpn.punish-command")));
            });
        }

        //Check for Lunar Client
        HadesPlugin.getInstance().getLunarClientAPI().getUserManager().setPlayerData(e.getPlayer().getUniqueId(), new net.mineaus.lunar.api.user.User(e.getPlayer().getUniqueId(), e.getPlayer().getName()));
        TaskUtil.taskLater(() -> {
            UserManager.getUser(e.getPlayer()).setUsingLunarClient(HadesPlugin.getInstance().getLunarClientAPI().getUserManager().getPlayerData(e.getPlayer().getUniqueId()).isLunarClient()
                    && HadesPlugin.getInstance().getLunarClientAPI().isAuthenticated(e.getPlayer()));
        }, HadesPlugin.getInstance(), 40L);
    }

    @PacketHandler
    public void onUninject(PlayerUninjectEvent e) {
        UserManager.users.remove(UserManager.getUser(e.getPlayer()));
        HadesPlugin.getInstance().getLunarClientAPI().getUserManager().removePlayerData(e.getPlayer().getUniqueId());
    }

    @PacketHandler
    public void onPacketReceive(PacketReceiveEvent e) {
        User user = UserManager.getUser(e.getPlayer());
        if (user != null) {
            //Process Movement
            MovementProcessor.process(user, e);

            //Player Vars
            if (e.getPacketName().equalsIgnoreCase(Packet.Client.ENTITY_ACTION)) {
                WrappedPacketInEntityAction packet = new WrappedPacketInEntityAction(e.getPacket());
                if (packet.getAction().equals(PlayerAction.START_SPRINTING)) {
                    user.setSprinting(true);
                }
                if (packet.getAction().equals(PlayerAction.STOP_SPRINTING)) {
                    user.setSprinting(false);
                }
                if (packet.getAction().equals(PlayerAction.START_SNEAKING)) {
                    user.setSneaking(true);
                }
                if (packet.getAction().equals(PlayerAction.STOP_SNEAKING)) {
                    user.setSneaking(false);
                }
            }

            //Call Checks
            PacketEvent callEvent = e;
            if (e.getPacketName().equalsIgnoreCase(Packet.Client.ARM_ANIMATION)) {
                callEvent = new SwingEvent();
            } else if (e.getPacketName().equalsIgnoreCase(Packet.Client.USE_ENTITY)) {
                WrappedPacketInUseEntity packet = new WrappedPacketInUseEntity(e.getPacket());
                if (packet.getAction() == EntityUseAction.ATTACK) {
                    callEvent = new AttackEvent(packet.getEntityId(), packet.getEntity());
                } else if (packet.getAction() == EntityUseAction.INTERACT
                        || packet.getAction() == EntityUseAction.INTERACT_AT) {
                    callEvent = new EntityInteractEvent(packet.getEntityId(), packet.getEntity());
                } else callEvent = e;
            } else if (e.getPacketName().equalsIgnoreCase(Packet.Client.CHAT)) {
                WrappedPacketInChat packet = new WrappedPacketInChat(e.getPacket());
                callEvent = new ChatEvent(packet.getMessage());
            } else if (e.getPacketName().equalsIgnoreCase(Packet.Client.BLOCK_DIG)) {
                WrappedPacketInBlockDig packet = new WrappedPacketInBlockDig(e.getPacket());
                user.setDigTick(user.getTick());
                if (packet.getDigType() == PlayerDigType.START_DESTROY_BLOCK) user.setDigging(true);
                else if (packet.getDigType() == PlayerDigType.STOP_DESTROY_BLOCK
                        || packet.getDigType() == PlayerDigType.ABORT_DESTROY_BLOCK) user.setDigging(false);
                callEvent = new DigEvent(packet.getBlockPosition(), packet.getDirection(), packet.getDigType());
            } else if (e.getPacketName().equalsIgnoreCase(Packet.Client.ENTITY_ACTION)) {
                WrappedPacketInEntityAction packet = new WrappedPacketInEntityAction(e.getPacket());
                callEvent = new EntityActionEvent(packet.getEntityId(), packet.getEntity(), packet.getJumpBoost(), packet.getAction());
            } else if (PacketUtil.isFlyingPacket(e.getPacketName())) {
                WrappedPacketInFlying packet = new WrappedPacketInFlying(e.getPacket());
                user.setTick(user.getTick() + 1);
                callEvent = new FlyingEvent(packet.getX(), packet.getY(), packet.getZ(), packet.getYaw(), packet.getPitch(),
                        packet.isPosition(),
                        packet.isLook(),
                        packet.isOnGround());
            } else if (e.getPacketName().equalsIgnoreCase(Packet.Client.KEEP_ALIVE)) {
                WrappedPacketInKeepAlive packet = new WrappedPacketInKeepAlive(e.getPacket());
                if (user.isVerifyingVelocity() && packet.getId() == user.getLastVelocityId()) {
                    user.setVerifyingVelocity(false);
                    user.setVelocityTick(user.getTick() + 1);
                }
                callEvent = new PingEvent();
            } else if (e.getPacketName().equalsIgnoreCase(Packet.Client.BLOCK_PLACE)) {
                WrappedPacketInBlockPlace packet = new WrappedPacketInBlockPlace(e.getPlayer(), e.getPacket());
                callEvent = new PlaceEvent(packet.getBlockPosition(), packet.getItemStack());
            }
            PacketEvent finalCallEvent = callEvent;
            if (!HadesPlugin.getInstance().getConfig().getBoolean("checks.exempt-players") || !user.getPlayer().hasPermission(HadesPlugin.getInstance().getBasePermission() + ".exempt.checks"))
                user.getExecutorService().execute(() -> user.getChecks().stream().filter(check -> check.enabled).forEach(check -> check.onHandle(finalCallEvent, user)));
        }
    }

    @PacketHandler
    public void onPacketSend(PacketSendEvent e) {
        User user = UserManager.getUser(e.getPlayer());
        if (user != null) {
            if (e.getPacketName().equalsIgnoreCase(Packet.Server.POSITION)) {
                user.setTeleportTick(user.getTick());
                if (!HadesPlugin.getInstance().getConfig().getBoolean("checks.exempt-players") || !user.getPlayer().hasPermission(HadesPlugin.getInstance().getBasePermission() + ".exempt.checks"))
                    user.getExecutorService().execute(() -> user.getChecks().stream().filter(check -> check.enabled).forEach(check -> check.onHandle(new TeleportEvent(-1, -1, -1, -1, -1), user)));
            } else if (e.getPacketName().equalsIgnoreCase(Packet.Server.ENTITY_VELOCITY)) {
                WrappedPacketOutEntityVelocity packet = new WrappedPacketOutEntityVelocity(e.getPacket());
                if (e.getPlayer().getEntityId() == packet.getEntityId()) {
                    user.setVelocityX(packet.getVelocityX());
                    user.setVelocityY(packet.getVelocityY());
                    user.setVelocityZ(packet.getVelocityZ());
                    Random random = new Random();
                    user.setVerifyingVelocity(true);
                    user.setLastVelocityId(Math.abs(random.nextInt()));
                    PacketUtil.sendKeepAlive(user, user.getLastVelocityId());
                    if (!HadesPlugin.getInstance().getConfig().getBoolean("checks.exempt-players") || !user.getPlayer().hasPermission(HadesPlugin.getInstance().getBasePermission() + ".exempt.checks"))
                        user.getExecutorService().execute(() -> user.getChecks().stream().filter(check -> check.enabled).forEach(check -> check.onHandle(new VelocityEvent(packet.getEntityId(), packet.getVelocityX(), packet.getVelocityY(), packet.getVelocityZ()), user)));
                }
            } else {
                if (!HadesPlugin.getInstance().getConfig().getBoolean("checks.exempt-players") || !user.getPlayer().hasPermission(HadesPlugin.getInstance().getBasePermission() + ".exempt.checks"))
                    user.getExecutorService().execute(() -> user.getChecks().stream().filter(check -> check.enabled).forEach(check -> check.onHandle(e, user)));
            }
        }
    }

}
