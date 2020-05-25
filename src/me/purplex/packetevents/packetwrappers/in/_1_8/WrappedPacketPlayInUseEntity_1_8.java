package me.purplex.packetevents.packetwrappers.in._1_8;

import me.purplex.packetevents.enums.EntityUseAction;
import me.purplex.packetevents.enums.ServerVersion;
import me.purplex.packetevents.packetwrappers.api.version.WrappedVersionPacket;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;

public class WrappedPacketPlayInUseEntity_1_8 extends WrappedVersionPacket {
    private Entity entity;
    private EntityUseAction action;

    public WrappedPacketPlayInUseEntity_1_8(Object packet) {
        super(packet);

    }

    private Object getRawPacket() {
        return this.packet;
    }

    public Entity getEntity() {
        return entity;
    }

    public EntityUseAction getEntityUseAction() {
        return action;
    }

    @Override
    protected void setup() {
        setupEntity();
        setupAction();
    }

    private void setupEntity() {
        if (version == ServerVersion.v_1_8) {
            net.minecraft.server.v1_8_R1.PacketPlayInUseEntity packet = (net.minecraft.server.v1_8_R1.PacketPlayInUseEntity) getRawPacket();
            net.minecraft.server.v1_8_R1.WorldServer worldServer = null;
            for (World bukkitWorld : Bukkit.getWorlds()) {
                org.bukkit.craftbukkit.v1_8_R1.CraftWorld craftWorld = (org.bukkit.craftbukkit.v1_8_R1.CraftWorld) bukkitWorld;
                worldServer = craftWorld.getHandle();
                if (packet.a(worldServer) != null) {
                    break;
                }
            }
            net.minecraft.server.v1_8_R1.Entity entity = packet.a(worldServer);
            this.entity = entity.getBukkitEntity();
        }
    }

    private void setupAction() {
        net.minecraft.server.v1_8_R1.PacketPlayInUseEntity packet = (net.minecraft.server.v1_8_R1.PacketPlayInUseEntity) getRawPacket();
        String name = packet.a().name();
        this.action = EntityUseAction.valueOf(name);
    }
}


