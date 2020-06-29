package me.apex.hades.check.impl.combat.autoblock;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.packetevents.AttackEvent;
import me.apex.hades.event.impl.packetevents.FlyingEvent;
import me.apex.hades.event.impl.packetevents.PlaceEvent;
import me.apex.hades.user.User;

@CheckInfo(name = "AutoBlock", type = "B")
public class AutoBlockB extends Check {

    private int ticks, lastTicks;
    private boolean attacked;

    @Override
    public void onHandle(PacketEvent e, User user) {
        if(e instanceof AttackEvent) {
            attacked = true;
        }if(e instanceof PlaceEvent) {
            if(attacked) {
                if(ticks < 2) {
                    flag(user, "low tick delay, t: " + ticks);
                }
                attacked = false;
            }
            lastTicks = ticks;
            ticks = 0;
        }else if(e instanceof FlyingEvent) {
            ticks++;
        }
    }
}
