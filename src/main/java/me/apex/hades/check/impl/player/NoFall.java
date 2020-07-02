package me.apex.hades.check.impl.player;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.packetevents.FlyingEvent;
import me.apex.hades.user.User;
import me.apex.hades.util.PlayerUtil;

@CheckInfo(name = "NoFall")
public class NoFall extends Check {

    private double preVLA;
    private double preVLB;

    @Override
    public void onHandle(PacketEvent e, User user) {
        if (e instanceof FlyingEvent) {
            if (((FlyingEvent) e).isOnGround() && !user.isOnGroundServerSide()) {
                if (++preVLA > 2) {
                    flag(user, "GroundSpoof","spoofed ground, g: " + !PlayerUtil.isOnGround(user.getPlayer()), false);
                }
            } else preVLA = 0;
            if ((user.isOnGround() && user.getPlayer().getFallDistance() > 3) || (user.isOnGroundServerSide() && user.getPlayer().getFallDistance() > 3)){
                if (++preVLB > 3){
                    flag(user, "FallSpoof", "spoofed fall distance!",false);
                }
            }else preVLB = 0;
        }
    }
}
