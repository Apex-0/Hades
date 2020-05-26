package me.purplex.packetevents.packetwrappers.in.chat;

import me.purplex.packetevents.packetwrappers.api.version.WrappedVersionPacket;
import net.minecraft.server.v1_9_R2.PacketPlayInChat;

class WrappedPacketInChat_1_9_4 extends WrappedVersionPacket {
    public String message;

    WrappedPacketInChat_1_9_4(Object packet) {
        super(packet);
    }

    @Override
    protected void setup() {
        PacketPlayInChat p = (PacketPlayInChat) packet;
        this.message = p.a();
    }
}
