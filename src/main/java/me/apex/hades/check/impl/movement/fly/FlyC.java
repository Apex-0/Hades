package me.apex.hades.check.impl.movement.fly;

import me.apex.hades.check.api.Check;
import me.apex.hades.check.api.CheckInfo;
import me.apex.hades.objects.User;
import me.apex.hades.utils.MathUtils;
import me.apex.hades.utils.PacketUtils;
import me.apex.hades.utils.PlayerUtils;
import me.purplex.packetevents.event.impl.PacketReceiveEvent;

import java.util.Deque;
import java.util.LinkedList;

@CheckInfo(name = "Fly", type = "C")
public class FlyC extends Check {
	
	@Override
	public void init() {
		dev = true;
		enabled = true;
	}
	
	private Deque<Double> distList = new LinkedList();
    private double lastDeviation;

	@Override
	public void onPacket(PacketReceiveEvent e, User user) {
		if(PacketUtils.isFlyingPacket(e.getPacketName())) {
			distList.add(user.getDeltaY());

            if (distList.size() == 10) {
                double deviation = MathUtils.getStandardDeviation(distList.stream().mapToDouble(d -> d).toArray());
                double lastDeviation = this.lastDeviation;
                this.lastDeviation = deviation;
                
                if(!user.onGround() && !PlayerUtils.isClimbableBlock(user.getLocation().getBlock()) && !PlayerUtils.isOnGround(user.getPlayer()) && !user.getPlayer().isFlying() && !user.getPlayer().isInsideVehicle()) {
                	if(deviation == 0.0D) {
                		flag(user, "deviation = " + deviation);
                	}
                }

                distList.clear();
            }
		}
	}
	
}
