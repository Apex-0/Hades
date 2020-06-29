package me.apex.hades.check.impl.combat.autoblock;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.packetevents.FlyingEvent;
import me.apex.hades.event.impl.packetevents.PlaceEvent;
import me.apex.hades.user.User;
import me.apex.hades.util.PacketUtil;

@CheckInfo(name = "AutoBlock", type = "A")
public class AutoBlockA extends Check {

    private int ticks;

    @Override
    public void onHandle(PacketEvent e, User user) {
        if (e instanceof PlaceEvent) {
            PlaceEvent packet = (PlaceEvent) e;

            if (ticks < 2 && PacketUtil.isBlockPacket(packet.getItemStack().getType().toString())) {
                if (preVL++ > 4)
                    flag(user, "ticks = " + ticks);
            } else preVL = 0;

            this.ticks = 0;
        } else if (e instanceof FlyingEvent) {
            ticks++;
        }
    }
}
