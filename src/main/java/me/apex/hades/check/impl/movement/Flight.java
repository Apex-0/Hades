package me.apex.hades.check.impl.movement;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.packetevents.FlyingEvent;
import me.apex.hades.user.User;
import me.apex.hades.util.MathUtil;

@CheckInfo(name = "Flight")
public class Flight extends Check {
    @Override
    public void onHandle(PacketEvent e, User user) {
        if (e instanceof FlyingEvent) {
            if (user.getDeltaY() >= 0.0
                    && user.getAirTicks() > 6
                    && user.getPlayer().getVelocity().getY() < -0.075
                    && elapsed(user.getTick(), user.getFlyingTick()) > 40
                    && elapsed(user.getTick(), user.getLiquidTick()) > 20
                    && elapsed(user.getTick(), user.getClimbableTick()) > 20
                    && elapsed(user.getTick(), user.getVelocityTick()) > 100
                    && user.getPlayer().getVehicle() == null) {
                flag(user, "Ascension","y motion higher than 0, m: " + user.getDeltaY() + ", " + user.getPlayer().getLocation().getBlock().getType().toString(), false);
            }

            double prediction = (user.getLastDeltaY() - 0.08D) * 0.9800000190734863D;

            if(user.getAirTicks() > 6
                    && user.getPlayer().getVelocity().getY() < -0.075D
                    && elapsed(user.getTick(), user.getFlyingTick()) > 40
                    && elapsed(user.getTick(), user.getLiquidTick()) > 20
                    && elapsed(user.getTick(), user.getClimbableTick()) > 20
                    && elapsed(user.getTick(), user.getVelocityTick()) > 100
                    && user.getPlayer().getVehicle() == null) {
                if(!MathUtil.isRoughlyEqual(user.getDeltaY(), prediction, 0.001)) {
                    flag(user, "Prediction","invalid vertical motion, m: " + user.getDeltaY() + ", p: " + prediction, false);
                }
            }
        }
    }

}