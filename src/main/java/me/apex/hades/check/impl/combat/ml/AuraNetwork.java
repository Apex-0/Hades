package me.apex.hades.check.impl.combat.ml;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.packetevents.AttackEvent;
import me.apex.hades.user.User;

import java.util.ArrayList;
import java.util.List;

@CheckInfo(name = "Aura")
public class AuraNetwork extends Check {

    private final List<Float> angles = new ArrayList<>();

    @Override
    public void onHandle(PacketEvent e, User user) {
        if (e instanceof AttackEvent) {

        }
    }

}
