package me.apex.hades.check.impl.combat.velocity;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.packetevents.FlyingEvent;
import me.apex.hades.user.User;

//Credits to Jonhan for original check idea!
@CheckInfo(name = "Velocity", type = "A")
public class VelocityA extends Check {

    @Override
    public void init() {
        dev = true;
    }

    @Override
    public void onHandle(PacketEvent e, User user) {
        if(e instanceof FlyingEvent) {
            if(elapsed(user.getTick(), user.getVelocityTick()) < 6) {
                if(user.getVelocityY() > 0.2
                        && user.getDeltaY() <= user.getVelocityY() * 0.99
                        && elapsed(user.getTick(), user.getUnderBlockTick()) > 20
                        && elapsed(user.getTick(), user.getLiquidTick()) > 20
                        && !user.isInWeb()) {
                    if(++preVL >= 6) {
                        preVL = 0;
                        flag(user, "didnt take expected velocity, d: " + user.getDeltaY() + ", v: " + (user.getVelocityY() * 0.99));
                    }
                }else preVL = 0;
            }
        }
    }

}
