package me.apex.hades.check.impl.combat;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.packetevents.AttackEvent;
import me.apex.hades.event.impl.packetevents.FlyingEvent;
import me.apex.hades.event.impl.packetevents.PlaceEvent;
import me.apex.hades.user.User;
import me.apex.hades.util.PacketUtil;

@CheckInfo(name = "AutoBlock")
public class AutoBlock extends Check {

    private int ticks, lastTicks;
    private boolean attacked;

    @Override
    public void onHandle(PacketEvent e, User user) {
        if(e instanceof AttackEvent) {
            attacked = true;
        }if(e instanceof PlaceEvent) {
            if(attacked) {
                if(ticks < 2) {
                    flag(user, "FastBlock","low tick delay, t: " + ticks);
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
