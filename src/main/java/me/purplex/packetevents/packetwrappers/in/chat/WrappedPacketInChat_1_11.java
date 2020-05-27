package me.purplex.packetevents.packetwrappers.in.chat;

import me.purplex.packetevents.packetwrappers.api.version.WrappedVersionPacket;
import net.minecraft.server.v1_11_R1.PacketPlayInChat;

class WrappedPacketInChat_1_11 extends WrappedVersionPacket {
    public String message;
    WrappedPacketInChat_1_11(Object packet) {
        super(packet);
    }

    @Override
    protected void setup() {
        PacketPlayInChat p = (PacketPlayInChat)packet;
        this.message = p.a();
    }
}
