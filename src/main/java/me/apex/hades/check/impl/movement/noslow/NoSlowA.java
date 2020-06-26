package me.apex.hades.check.impl.movement.noslow;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.packetevents.FlyingEvent;
import me.apex.hades.user.User;

@CheckInfo(name = "NoSlow", type = "A")
public class NoSlowA extends Check {

    @Override
    public void onHandle(PacketEvent e, User user) {
        if (e instanceof FlyingEvent) {

        }
    }
}
