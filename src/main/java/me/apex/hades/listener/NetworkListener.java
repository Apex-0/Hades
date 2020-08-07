package me.apex.hades.listener;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.annotations.PacketHandler;
import io.github.retrooper.packetevents.enums.minecraft.EntityUseAction;
import io.github.retrooper.packetevents.enums.minecraft.PlayerAction;
import io.github.retrooper.packetevents.enums.minecraft.PlayerDigType;
import io.github.retrooper.packetevents.event.PacketEvent;
import io.github.retrooper.packetevents.event.PacketListener;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketSendEvent;
import io.github.retrooper.packetevents.event.impl.PlayerInjectEvent;
import io.github.retrooper.packetevents.event.impl.PlayerUninjectEvent;
import io.github.retrooper.packetevents.packet.PacketType;
import io.github.retrooper.packetevents.packetwrappers.in.blockdig.WrappedPacketInBlockDig;
import io.github.retrooper.packetevents.packetwrappers.in.blockplace.WrappedPacketInBlockPlace;
import io.github.retrooper.packetevents.packetwrappers.in.chat.WrappedPacketInChat;
import io.github.retrooper.packetevents.packetwrappers.in.entityaction.WrappedPacketInEntityAction;
import io.github.retrooper.packetevents.packetwrappers.in.flying.WrappedPacketInFlying;
import io.github.retrooper.packetevents.packetwrappers.in.transaction.WrappedPacketInTransaction;
import io.github.retrooper.packetevents.packetwrappers.in.useentity.WrappedPacketInUseEntity;
import io.github.retrooper.packetevents.packetwrappers.out.entityvelocity.WrappedPacketOutEntityVelocity;
import io.github.retrooper.packetevents.packetwrappers.out.transaction.WrappedPacketOutTransaction;
import me.apex.hades.HadesConfig;
import me.apex.hades.HadesPlugin;
import me.apex.hades.event.impl.packetevents.*;
import me.apex.hades.processor.MovementProcessor;
import me.apex.hades.user.User;
import me.apex.hades.user.UserManager;
import me.apex.hades.util.TaskUtil;
import me.apex.hades.util.text.ChatUtil;
import me.apex.hades.util.vpn.VPNChecker;
import org.bukkit.Bukkit;
import org.bukkit.util.Vector;

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

            //Call Checks
            PacketEvent callEvent = e;
            if (e.getPacketId() == PacketType.Client.ARM_ANIMATION) {
                callEvent = new SwingEvent();
            } else if (e.getPacketId() == PacketType.Client.USE_ENTITY) {
                WrappedPacketInUseEntity packet = new WrappedPacketInUseEntity(e.getNMSPacket());
                if (packet.getAction() == EntityUseAction.ATTACK) {
                    callEvent = new AttackEvent(packet.getEntityId(), packet.getEntity());
                } else if (packet.getAction() == EntityUseAction.INTERACT
                        || packet.getAction() == EntityUseAction.INTERACT_AT) {
                    callEvent = new EntityInteractEvent(packet.getEntityId(), packet.getEntity());
                } else callEvent = e;
            } else if (e.getPacketId() == PacketType.Client.CHAT) {
                WrappedPacketInChat packet = new WrappedPacketInChat(e.getNMSPacket());
                callEvent = new ChatEvent(packet.getMessage());
            } else if (e.getPacketId() == PacketType.Client.BLOCK_DIG) {
                WrappedPacketInBlockDig packet = new WrappedPacketInBlockDig(e.getNMSPacket());
                user.setDigTick(user.getTick());
                if (packet.getDigType() == PlayerDigType.START_DESTROY_BLOCK) user.setDigging(true);
                else if (packet.getDigType() == PlayerDigType.STOP_DESTROY_BLOCK
                        || packet.getDigType() == PlayerDigType.ABORT_DESTROY_BLOCK) user.setDigging(false);
                callEvent = new DigEvent(packet.getBlockPosition(), packet.getDirection(), packet.getDigType());
            } else if (e.getPacketId() == PacketType.Client.ENTITY_ACTION) {
                WrappedPacketInEntityAction packet = new WrappedPacketInEntityAction(e.getNMSPacket());
                if (packet.getAction() != null){
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
                callEvent = new EntityActionEvent(packet.getEntityId(), packet.getEntity(), packet.getJumpBoost(), packet.getAction());
            } else if (PacketType.Client.Util.isInstanceOfFlying(e.getPacketId())) {
                WrappedPacketInFlying packet = new WrappedPacketInFlying(e.getNMSPacket());
                user.setTick(user.getTick() + 1);
                callEvent = new FlyingEvent(packet.getX(), packet.getY(), packet.getZ(), packet.getYaw(), packet.getPitch(),
                        packet.isPosition(),
                        packet.isLook(),
                        packet.isOnGround());
            } else if (e.getPacketId() == PacketType.Client.KEEP_ALIVE) {
                callEvent = new PingEvent();
            } else if (e.getPacketId() == PacketType.Client.BLOCK_PLACE) {
                WrappedPacketInBlockPlace packet = new WrappedPacketInBlockPlace(e.getPlayer(), e.getNMSPacket());
                callEvent = new PlaceEvent(packet.getBlockPosition(), packet.getItemStack());
            }else if(e.getPacketId() == PacketType.Client.TRANSACTION) {
                WrappedPacketInTransaction packet = new WrappedPacketInTransaction(e.getNMSPacket());
                if (user.isVerifyingVelocity()) {
                    if(packet.getActionNumber() == user.getVelocityId()) {
                        user.setVerifyingVelocity(false);
                        user.setVelocityTick(user.getTick() + 1);
                        user.setVelocityVerifications(user.getVelocityVerifications() + 1);
                        user.setMaxVelocityTicks((int)(user.getLocation().clone().toVector().distance(new Vector(user.getLocation().getX() + user.getVelocityX(), user.getLocation().getY() + user.getVelocityY(), user.getLocation().getZ() + user.getVelocityZ())) * 20));
                    }
                }
            }
            PacketEvent finalCallEvent = callEvent;
            if (!HadesPlugin.getInstance().getConfig().getBoolean("checks.exempt-players") || !user.getPlayer().hasPermission(HadesConfig.BASE_PERMISSION + ".exempt.checks"))
                user.getExecutorService().execute(() -> user.getChecks().stream().filter(check -> check.enabled).forEach(check -> check.onHandle(finalCallEvent, user)));
        }
    }

    @PacketHandler
    public void onPacketSend(PacketSendEvent e) {
        User user = UserManager.getUser(e.getPlayer());
        if (user != null) {
            if (e.getPacketId() == PacketType.Server.POSITION) {
                user.setTeleportTick(user.getTick());
                if (!HadesPlugin.getInstance().getConfig().getBoolean("checks.exempt-players") || !user.getPlayer().hasPermission(HadesConfig.BASE_PERMISSION + ".exempt.checks"))
                    user.getExecutorService().execute(() -> user.getChecks().stream().filter(check -> check.enabled).forEach(check -> check.onHandle(new TeleportEvent(-1, -1, -1, -1, -1), user)));
            } else if (e.getPacketId() == PacketType.Server.ENTITY_VELOCITY) {
                WrappedPacketOutEntityVelocity packet = new WrappedPacketOutEntityVelocity(e.getNMSPacket());
                if (e.getPlayer().getEntityId() == packet.getEntityId()) {
                    user.setVerifyingVelocity(true);
                    Random random = new Random();
                    user.setVelocityId(Math.abs(random.nextInt(1000)));
                    user.setVelocityX(packet.getVelocityX());
                    user.setVelocityY(packet.getVelocityY());
                    user.setVelocityZ(packet.getVelocityZ());
                    PacketEvents.getAPI().getPlayerUtils().sendPacket(user.getPlayer(), new WrappedPacketOutTransaction(0, (short)user.getVelocityId(), false));
                    if (!HadesPlugin.getInstance().getConfig().getBoolean("checks.exempt-players") || !user.getPlayer().hasPermission(HadesConfig.BASE_PERMISSION + ".exempt.checks"))
                        user.getExecutorService().execute(() -> user.getChecks().stream().filter(check -> check.enabled).forEach(check -> check.onHandle(new VelocityEvent(packet.getEntityId(), packet.getVelocityX(), packet.getVelocityY(), packet.getVelocityZ()), user)));
                }
            } else {
                if (!HadesPlugin.getInstance().getConfig().getBoolean("checks.exempt-players") || !user.getPlayer().hasPermission(HadesConfig.BASE_PERMISSION + ".exempt.checks"))
                    user.getExecutorService().execute(() -> user.getChecks().stream().filter(check -> check.enabled).forEach(check -> check.onHandle(e, user)));
            }
        }
    }

}
