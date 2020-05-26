package me.purplex.packetevents.packetwrappers.in.flying;


import me.purplex.packetevents.packetwrappers.api.version.WrappedVersionPacket;
import net.minecraft.server.v1_8_R1.PacketPlayInFlying;

class WrappedPacketInFlying_1_8 extends WrappedVersionPacket {
    WrappedPacketInFlying_1_8(Object packet) {
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
        PacketPlayInFlying p = (PacketPlayInFlying) packet;
        this.x = p.a();
        this.y = p.b();
        this.z = p.c();
        this.yaw = p.d();
        this.pitch = p.e();
        this.f = p.f();
        this.hasPos = p.g();
        this.hasLook = p.h();
    }


    static class WrappedPacketInPosition_1_8 extends WrappedPacketInFlying_1_8 {

        WrappedPacketInPosition_1_8(Object packet) {
            super(packet);
            this.hasPos = true;
        }
    }

    static class WrappedPacketInPosition_Look_1_8 extends WrappedPacketInFlying_1_8 {

        WrappedPacketInPosition_Look_1_8(Object packet) {
            super(packet);
            this.hasLook = true;
        }
    }


}
