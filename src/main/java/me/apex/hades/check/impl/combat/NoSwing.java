package me.apex.hades.check.impl.combat;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.packetevents.AttackEvent;
import me.apex.hades.event.impl.packetevents.FlyingEvent;
import me.apex.hades.event.impl.packetevents.SwingEvent;
import me.apex.hades.user.User;

@CheckInfo(name = "NoSwing")
public class NoSwing extends Check {

    private boolean lastWasArm;

    @Override
    public void onHandle(PacketEvent e, User user) {
        if (e instanceof AttackEvent) {
            if (!lastWasArm) {
                flag(user, "*", "swung = " + lastWasArm);
            }
        } else if (e instanceof SwingEvent) {
            lastWasArm = true;
        } else if (e instanceof FlyingEvent) {
            lastWasArm = false;
        }
    }
}
