package me.apex.hades.check.impl.combat.aura;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.packetevents.AttackEvent;
import me.apex.hades.event.impl.packetevents.FlyingEvent;
import me.apex.hades.user.User;

@CheckInfo(name = "Aura")
public class AuraB extends Check {

    private int ticks;

    @Override
    public void onHandle(PacketEvent e, User user) {
        if (e instanceof AttackEvent) {
            if (ticks++ > 1) {
                flag(user, "Multi","too many attacks in tick, t: " + ticks);
            }
        } else if (e instanceof FlyingEvent) {
            ticks = 0;
        }
    }

}
