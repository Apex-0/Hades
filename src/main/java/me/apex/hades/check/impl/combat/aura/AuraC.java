package me.apex.hades.check.impl.combat.aura;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.packetevents.AttackEvent;
import me.apex.hades.event.impl.packetevents.FlyingEvent;
import me.apex.hades.user.User;
import org.bukkit.entity.Entity;

@CheckInfo(name = "Aura")
public class AuraC extends Check {

    private int ticks;
    private Entity lastTarget;

    @Override
    public void onHandle(PacketEvent e, User user) {
        if (e instanceof AttackEvent) {
            Entity target = ((AttackEvent) e).getEntity();
            Entity lastTarget = this.lastTarget != null ? this.lastTarget : target;
            this.lastTarget = target;

            if (target != lastTarget) {
                if (ticks < 2) {
                    if (++preVL > 2) {
                        flag(user, "Switch","invalid attack pattern, t: " + ticks);
                    }
                } else preVL *= 0.75;
            }
            ticks = 0;
        } else if (e instanceof FlyingEvent) {
            ticks++;
        }
    }

}
