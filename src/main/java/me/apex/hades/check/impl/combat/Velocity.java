package me.apex.hades.check.impl.combat;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.packetevents.FlyingEvent;
import me.apex.hades.user.User;

import java.util.ArrayList;
import java.util.List;

@CheckInfo(name = "Velocity")
public class Velocity extends Check {

    private List<Double> verticals = new ArrayList<>(), horizontalX = new ArrayList<>(), horizontalZ = new ArrayList<>();

    @Override
    public void onHandle(PacketEvent e, User user) {
        if (e instanceof FlyingEvent) {
            if (user.isVerifyingVelocity() || elapsed(user.getTick(), user.getVelocityTick()) < 6) {
                verticals.add(user.getDeltaY());
            } else {
                if (verticals.size() > 0) {
                    double max = verticals.stream().mapToDouble(d -> d).max().getAsDouble();
                    double min = user.getVelocityY() * 0.99F;

                    if (max <= min
                            && user.liquidTicks() > 20
                            && user.nearWallTicks() > 20
                            && user.climbableTicks() > 20
                            && user.underBlockTicks() > 20
                            && elapsed(user.getTick(), user.getTeleportTick()) > 20) {
                        flag(user, "Vertical", "max = " + max + ", min = " + min);
                    }

                    verticals.clear();
                }
            }
        }
    }
}
