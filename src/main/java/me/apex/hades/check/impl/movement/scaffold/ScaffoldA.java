package me.apex.hades.check.impl.movement.scaffold;

import me.apex.hades.check.api.Check;
import me.apex.hades.check.api.CheckInfo;
import me.apex.hades.objects.User;
import me.purplex.packetevents.event.impl.PacketReceiveEvent;
import me.purplex.packetevents.packet.Packet;
import me.purplex.packetevents.packetwrappers.in.blockplace.WrappedPacketInBlockPlace;

@CheckInfo(name = "Scaffold", type = "A")
public class ScaffoldA extends Check {

    @Override
    public void onPacket(PacketReceiveEvent e, User user) {
        if (e.getPacketName().equalsIgnoreCase(Packet.Client.BLOCK_PLACE)) {
            WrappedPacketInBlockPlace packet = new WrappedPacketInBlockPlace(user.getPlayer(), e.getPacket());
            //This if should fix this
            if (e.getPlayer().getLocation().getBlockY() <= packet.getBlockPosition().y || e.getPlayer().isSneaking() || !user.onGround() || user.getDeltaXZ() < .2)return;
            if (e.getPlayer().getLocation().subtract(0, 1, 0).getBlock().getType().isSolid() && !e.getPlayer().getLocation().subtract(0, 2, 0).getBlock().getType().isSolid()) {
                flag(user, "using scaffold idk what to say lol.");
            }
        }
    }

}
