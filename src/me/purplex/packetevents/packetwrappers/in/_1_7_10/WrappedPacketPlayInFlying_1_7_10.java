package me.purplex.packetevents.packetwrappers.in._1_7_10;

import me.purplex.packetevents.packetwrappers.api.version.WrappedVersionPacket;
import net.minecraft.server.v1_7_R4.PacketPlayInFlying;

public class WrappedPacketPlayInFlying_1_7_10 extends WrappedVersionPacket {
    public WrappedPacketPlayInFlying_1_7_10(Object packet) {
        super(packet);
    }

    public double x;
    public double y;
    public double z;
    public float yaw;
    public float pitch;
    public boolean f;
    public boolean hasPos;
    public boolean hasLook;


    @Override
    protected void setup() {
        PacketPlayInFlying p = (PacketPlayInFlying)packet;
        this.x = p.c();
        this.y = p.d();
        this.z = p.e();
        this.yaw = p.g();
        this.pitch = p.h();
        this.f = p.i();
    }

    public static class WrappedPacketPlayInPosition_1_7_10 extends WrappedPacketPlayInFlying_1_7_10 {

        public WrappedPacketPlayInPosition_1_7_10(Object packet) {
            super(packet);
            this.hasPos = true;
        }
    }

    public static class WrappedPacketPlayInPosition_Look_1_7_10 extends WrappedPacketPlayInFlying_1_7_10 {

        public WrappedPacketPlayInPosition_Look_1_7_10(Object packet) {
            super(packet);
            this.hasLook = true;
        }
    }


}
