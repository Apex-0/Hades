package me.apex.hades.check.impl.movement.fly;

import me.apex.hades.check.api.Check;
import me.apex.hades.check.api.CheckInfo;
import me.apex.hades.objects.User;
import me.apex.hades.utils.PacketUtils;
import me.apex.hades.utils.PlayerUtils;
import me.purplex.packetevents.event.impl.PacketReceiveEvent;

@CheckInfo(name = "Fly", type = "B")
public class FlyB extends Check {

    @Override
    public void onPacket(PacketReceiveEvent e, User user) {
        if (PacketUtils.isFlyingPacket(e.getPacketName())) {
            double dist = user.getDeltaY() - user.getLastDeltaY();

            if (!user.onGround()
            		&& !user.getPlayer().getAllowFlight()
            		&& !PlayerUtils.isClimbableBlock(user.getLocation().getBlock())
            		&& !PlayerUtils.isClimbableBlock(user.getLocation().subtract(0,1,0).getBlock())
            		&& !PlayerUtils.isInWeb(user.getPlayer())
            		&& !PlayerUtils.isInLiquid(user.getPlayer())
                    && !PlayerUtils.isOnGround(user.getPlayer())
                    && !user.getPlayer().isInsideVehicle()){
                if (dist >= 0.0D) {
                	if(vl++ > 10) {
                		flag(user, "dist = " + dist);
                	}
                }
            } else vl = 0;
        }
    }

}
