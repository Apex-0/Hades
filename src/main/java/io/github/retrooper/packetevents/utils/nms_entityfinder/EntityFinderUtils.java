package io.github.retrooper.packetevents.utils.nms_entityfinder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.enums.ServerVersion;
import io.github.retrooper.packetevents.utils.NMSUtils;

public class EntityFinderUtils {
    @Nullable
    private static ServerVersion version = PacketEvents.getServerVersion();

    public static Entity getEntityById(final int id) {
        for (final World world : Bukkit.getWorlds()) {
            final Entity entity = getEntityByIdWithWorld(world, id);
            if (entity != null) {
                return entity;
            }
        }
        return null;
    }

    @Nullable
    public static Entity getEntityByIdWithWorld(final World world, final int id) {
        Object craftWorld = craftWorldClass.cast(world);

        Object worldServer = null;
        try {
            worldServer = craftWorldGetHandle.invoke(craftWorld);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        Object nmsEntity = null;
        try {
            nmsEntity = getEntityByIdMethod.invoke(worldServer, id);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        try {
            return (Entity) getBukkitEntity.invoke(nmsEntity);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static Class<?> worldServerClass;
    private static Class<?> craftWorldClass;
    private static Class<?> entityClass;

    private static Method getEntityByIdMethod;
    private static Method craftWorldGetHandle;

    private static Method getBukkitEntity;

    static {
        try {
            worldServerClass = NMSUtils.getNMSClass("WorldServer");
            craftWorldClass = NMSUtils.getOBCClass("CraftWorld");
            entityClass = NMSUtils.getNMSClass("Entity");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            getEntityByIdMethod = worldServerClass.getMethod(getEntityByNameMethodName(), int.class);
            craftWorldGetHandle = craftWorldClass.getMethod("getHandle");
            getBukkitEntity = entityClass.getMethod("getBukkitEntity");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static String getEntityByNameMethodName() {
        if (version.isHigherThan(ServerVersion.v_1_7_10) && version.isLowerThan(ServerVersion.v_1_9)) {
            return "a";
        }
        return "getEntity";
    }
}
