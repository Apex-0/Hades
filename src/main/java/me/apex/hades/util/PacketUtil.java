package me.apex.hades.util;

import io.github.retrooper.packetevents.packet.PacketType;
import me.apex.hades.user.User;
import org.bukkit.Bukkit;

import java.lang.reflect.Constructor;

public class PacketUtil {

    public static boolean isPositionPacket(Byte packetName) {
        return packetName == (PacketType.Client.POSITION) || packetName == (PacketType.Client.POSITION_LOOK);
    }

    public static boolean isRotationPacket(Byte packetName) {
        return packetName == (PacketType.Client.LOOK) || packetName == PacketType.Client.POSITION_LOOK;
    }

    public static boolean isBlockPacket(String type) {
        return type.toLowerCase().contains("sword");
    }

    /*
    Packet Types
     */

    public static void sendKeepAlive(User user, int id) {
        try {
            Constructor<?> keepAliveConstructor = getNMSClass("PacketPlayOutKeepAlive").getConstructor(int.class);
            Object packet = keepAliveConstructor.newInstance(id);
            sendPacket(packet, user);
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    public static void sendTransaction(User user, int id) {
        try {
            Constructor<?> transactionConstructor = getNMSClass("PacketPlayOutTransaction").getConstructor(int.class, short.class, boolean.class);
            Object packet = transactionConstructor.newInstance((int) Math.abs(id), (short) user.getTick(), false);
            sendPacket(packet, user);
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    /*
    Packet Methods
     */

    public static void sendPacket(Object packet, User user) {
        try {
            Object handle = user.getPlayer().getClass().getMethod("getHandle").invoke(user.getPlayer());
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Class<?> getNMSClass(String name) {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
