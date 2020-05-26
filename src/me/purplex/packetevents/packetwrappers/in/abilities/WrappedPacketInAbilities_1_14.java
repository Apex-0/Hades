package me.purplex.packetevents.packetwrappers.in.abilities;

import me.purplex.packetevents.packetwrappers.api.version.WrappedVersionPacket;
import net.minecraft.server.v1_14_R1.PacketPlayInAbilities;

import java.lang.reflect.Field;

class WrappedPacketInAbilities_1_14 extends WrappedVersionPacket {

    public boolean a;
    public boolean b;
    public boolean c;
    public boolean d;
    public float e;
    public float f;

    WrappedPacketInAbilities_1_14(Object packet) {
        super(packet);
    }

    @Override
    protected void setup() {
        PacketPlayInAbilities p = (PacketPlayInAbilities) packet;
        this.a = p.b();
        this.b = p.isFlying();
        this.c = p.d();
        this.d = p.e();
        try {
            this.e = fieldE.getFloat(p);
        } catch (IllegalAccessException illegalAccessException) {
            illegalAccessException.printStackTrace();
        }
        try {
            this.f = fieldF.getFloat(p);
        } catch (IllegalAccessException illegalAccessException) {
            illegalAccessException.printStackTrace();
        }
    }

    private static Field fieldE;
    private static Field fieldF;

    static {
        try {
            fieldE = PacketPlayInAbilities.class.getDeclaredField("e");
            fieldE.setAccessible(true);
            fieldF = PacketPlayInAbilities.class.getDeclaredField("f");
            fieldF.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}