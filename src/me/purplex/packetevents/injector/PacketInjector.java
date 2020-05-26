package me.purplex.packetevents.injector;


import io.netty.channel.*;
import me.purplex.packetevents.PacketEvents;
import me.purplex.packetevents.enums.ServerVersion;
import me.purplex.packetevents.event.impl.*;
import me.purplex.packetevents.injector.channels.*; //all channel classes
import me.purplex.packetevents.packet.Packet;
import org.bukkit.entity.Player;


public class PacketInjector {
    private final static ServerVersion version = PacketEvents.getServerVersion();

    public void injectPlayer(Player player) {
        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
            @Override
            public void channelRead(final ChannelHandlerContext ctx, final Object packet) throws Exception {
                String packetName = packet.getClass().getSimpleName();
                PacketReceiveEvent e = new PacketReceiveEvent(player, packetName, packet);
                PacketEvents.getPacketManager().callEvent(e);
                if (e.isCancelled()) {
                    return;
                }
                super.channelRead(ctx, packet);
            }

            @Override
            public void write(final ChannelHandlerContext ctx, final Object packet, final ChannelPromise promise) throws Exception {
                String packetName = packet.getClass().getSimpleName();
                //LOGIN PACKET
                if (Packet.Login.HANDSHAKE.equals(packetName)
                        || Packet.Login.PING.equals(packetName)
                        || Packet.Login.START.equals(packetName)
                        || Packet.Login.SUCCESS.equals(packetName)) {
                    PacketLoginEvent e = new PacketLoginEvent(player, packetName, packet);
                    PacketEvents.getPacketManager().callEvent(e);
                } else {
                    PacketSendEvent e = new PacketSendEvent(player, packetName, packet);
                    PacketEvents.getPacketManager().callEvent(e);
                    if (e.isCancelled()) {
                        return;
                    }
                }
                super.write(ctx, packet, promise);
            }
        };
        final PlayerInjectEvent injectEvent = new PlayerInjectEvent(player);
        PacketEvents.getPacketManager().callEvent(injectEvent);
        if (!injectEvent.isCancelled()) {
            getChannel(player).pipeline().addBefore("packet_handler", player.getName(), channelDuplexHandler);
        }
    }

    public void uninjectPlayer(final Player player) {
        final Channel channel = getChannel(player);
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                channel.pipeline().remove(player.getName());
            }
        };
        final PlayerUninjectEvent uninjectEvent = new PlayerUninjectEvent(player);
        PacketEvents.getPacketManager().callEvent(uninjectEvent);
        if (!uninjectEvent.isCancelled()) {
            channel.eventLoop().submit(runnable);
        }
    }

    @Deprecated
    public Channel getChannel(Player player) {
        final Channel channel;
        if (version == ServerVersion.v_1_7_10) {
            channel = new Channel_1_7_10().getChannel(player);
        } else if (version == ServerVersion.v_1_8) {
            channel = new Channel_1_8().getChannel(player);
        } else if (version == ServerVersion.v_1_8_3) {
            channel = new Channel_1_8_3().getChannel(player);
        } else if (version == ServerVersion.v_1_8_8) {
            channel = new Channel_1_8_8().getChannel(player);
        } else if (version == ServerVersion.v_1_9) {
            channel = new Channel_1_9().getChannel(player);
        } else if (version == ServerVersion.v_1_9_4) {
            channel = new Channel_1_9_4().getChannel(player);
        } else if (version == ServerVersion.v_1_10) {
            channel = new Channel_1_10().getChannel(player);
        } else if (version == ServerVersion.v_1_11) {
            channel = new Channel_1_11().getChannel(player);
        } else if (version == ServerVersion.v_1_12) {
            channel = new Channel_1_12().getChannel(player);
        } else if (version == ServerVersion.v_1_13) {
            channel = new Channel_1_13().getChannel(player);
        } else if (version == ServerVersion.v_1_13_2) {
            channel = new Channel_1_13_2().getChannel(player);
        } else if (version == ServerVersion.v_1_14) {
            channel = new Channel_1_14().getChannel(player);
        } else if (version == ServerVersion.v_1_15) {
            channel = new Channel_1_15().getChannel(player);
        } else {
            final String err = "Version unsupported, please contact purplex(creator) through his discord server (http://discord.gg/2uZY5A4) and tell him what version your server is running on! Make sure you are using spigot!";
            throw new IllegalStateException(err);
        }
        return channel;
    }
}
