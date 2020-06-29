package me.apex.hades.check.impl.combat.noswing;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.packetevents.AttackEvent;
import me.apex.hades.event.impl.packetevents.FlyingEvent;
import me.apex.hades.event.impl.packetevents.SwingEvent;
import me.apex.hades.user.User;

@CheckInfo(name = "NoSwing", type = "A")
public class NoSwingA extends Check {

    private boolean lastWasArm;

    @Override
    public void onHandle(PacketEvent e, User user) {
        if (e instanceof AttackEvent) {
            if (!lastWasArm) {
                if (preVL++ > 1)
                    flag(user, "swung = " + lastWasArm);
            } else preVL = 0;
        } else if (e instanceof SwingEvent) {
            lastWasArm = true;
        } else if (e instanceof FlyingEvent) {
            lastWasArm = false;
        }
    }
}
