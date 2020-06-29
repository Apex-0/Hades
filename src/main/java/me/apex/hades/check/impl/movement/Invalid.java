package me.apex.hades.check.impl.movement;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.packetevents.FlyingEvent;
import me.apex.hades.user.User;

@CheckInfo(name = "Invalid")
public class Invalid extends Check {

    @Override
    public void onHandle(PacketEvent e, User user) {
        if (e instanceof FlyingEvent) {
            if (Math.abs(((FlyingEvent) e).getPitch()) > 90.0F) {
                flag(user, "Pitch","invalid pitch, p: " + ((FlyingEvent) e).getPitch(), false);
            }
        }
    }
}