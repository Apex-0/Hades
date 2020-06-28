package me.apex.hades.check.impl.player;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.packetevents.FlyingEvent;
import me.apex.hades.user.User;
import me.apex.hades.util.PlayerUtil;

@CheckInfo(name = "NoFall")
public class NoFall extends Check {

    private boolean lastOnGround, lastLastOnGround;

    private double preVLA;

    @Override
    public void onHandle(PacketEvent e, User user) {
        if (e instanceof FlyingEvent) {

            boolean onGround = PlayerUtil.isOnGround(user.getPlayer());

            if (((FlyingEvent) e).isOnGround() && !onGround && !lastOnGround && !lastLastOnGround) {
                if (++preVLA > 2) {
                    flag(user, "GroundSpoof","spoofed ground, g: " + !PlayerUtil.isOnGround(user.getPlayer()));
                }
            } else preVLA = 0;

            this.lastLastOnGround = lastOnGround;
            this.lastOnGround = onGround;
        }
    }
}
