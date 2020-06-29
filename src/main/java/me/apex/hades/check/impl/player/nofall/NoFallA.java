package me.apex.hades.check.impl.player.nofall;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.packetevents.FlyingEvent;
import me.apex.hades.user.User;
import me.apex.hades.util.PlayerUtil;

@CheckInfo(name = "NoFall", type = "A")
public class NoFallA extends Check {

    private boolean lastOnGround, lastLastOnGround;

    @Override
    public void onHandle(PacketEvent e, User user) {
        if (e instanceof FlyingEvent) {

            boolean onGround = PlayerUtil.isOnGround(user.getPlayer());

            if (((FlyingEvent) e).isOnGround() && !onGround && !lastOnGround && !lastLastOnGround) {
                if (++preVL > 2) {
                    flag(user, "Spoofed Ground, g: " + !PlayerUtil.isOnGround(user.getPlayer()));
                }
            } else preVL = 0;

            this.lastLastOnGround = lastOnGround;
            this.lastOnGround = onGround;
        }
    }
}
