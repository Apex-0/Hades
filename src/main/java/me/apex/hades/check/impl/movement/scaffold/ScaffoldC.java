package me.apex.hades.check.impl.movement.scaffold;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.user.User;

@CheckInfo(name = "Scaffold", type = "C")
public class ScaffoldC extends Check {
    @Override
    public void onHandle(PacketEvent e, User user) { }
}
