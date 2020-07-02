package me.apex.hades.check.impl.combat;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.packetevents.AttackEvent;
import me.apex.hades.user.User;

@CheckInfo(name = "Reach")
public class Reach extends Check {

    @Override
    public void onHandle(PacketEvent e, User user) {
        if(e instanceof AttackEvent) {
        }
    }
}
