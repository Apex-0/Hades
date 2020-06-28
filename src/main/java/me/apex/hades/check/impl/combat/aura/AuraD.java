package me.apex.hades.check.impl.combat.aura;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.packetevents.AttackEvent;
import me.apex.hades.user.User;
import me.apex.hades.util.MathUtil;
import org.bukkit.entity.Entity;

@CheckInfo(name = "Aura")
public class AuraD extends Check {
    @Override
    public void onHandle(PacketEvent e, User user) {
        if (e instanceof AttackEvent){
            AttackEvent packet = (AttackEvent)e;

            Entity entity = packet.getEntity();
            double rotation = Math.abs(user.getDeltaYaw());

            double dir = MathUtil.getDirection(user.getLocation(), entity.getLocation());
            double dist = MathUtil.getDistanceBetweenAngles360(user.getLocation().getYaw(), dir);

            if (dist < 0.7 && rotation > 2) {
                if (vl++ > 1)
                    flag(user, "Lockview","angle = " + dist + ", rotation = " + rotation);
            } else vl = 0;
        }
    }
}
