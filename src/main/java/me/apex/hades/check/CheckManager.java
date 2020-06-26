package me.apex.hades.check;

import me.apex.hades.check.impl.combat.angle.AngleA;
import me.apex.hades.check.impl.combat.angle.AngleB;
import me.apex.hades.check.impl.combat.aura.AuraA;
import me.apex.hades.check.impl.combat.aura.AuraB;
import me.apex.hades.check.impl.combat.aura.AuraC;
import me.apex.hades.check.impl.combat.autoblock.AutoBlockA;
import me.apex.hades.check.impl.combat.autoblock.AutoBlockB;
import me.apex.hades.check.impl.combat.autoclicker.AutoClickerA;
import me.apex.hades.check.impl.combat.autoclicker.AutoClickerB;
import me.apex.hades.check.impl.combat.autoclicker.AutoClickerC;
import me.apex.hades.check.impl.combat.autoclicker.AutoClickerD;
import me.apex.hades.check.impl.combat.badrotations.BadRotationsA;
import me.apex.hades.check.impl.combat.criticals.CriticalsA;
import me.apex.hades.check.impl.combat.noswing.NoSwingA;
import me.apex.hades.check.impl.combat.reach.ReachA;
import me.apex.hades.check.impl.combat.velocity.VelocityA;
import me.apex.hades.check.impl.movement.fastladder.FastLadderA;
import me.apex.hades.check.impl.movement.fly.FlyA;
import me.apex.hades.check.impl.movement.fly.FlyB;
import me.apex.hades.check.impl.movement.motion.MotionC;
import me.apex.hades.check.impl.movement.motion.MotionA;
import me.apex.hades.check.impl.movement.motion.MotionB;
import me.apex.hades.check.impl.movement.noslow.NoSlowA;
import me.apex.hades.check.impl.movement.scaffold.ScaffoldA;
import me.apex.hades.check.impl.movement.scaffold.ScaffoldB;
import me.apex.hades.check.impl.movement.speed.SpeedA;
import me.apex.hades.check.impl.movement.speed.SpeedB;
import me.apex.hades.check.impl.movement.speed.SpeedC;
import me.apex.hades.check.impl.movement.speed.SpeedD;
import me.apex.hades.check.impl.movement.sprint.SprintA;
import me.apex.hades.check.impl.movement.invalid.*;
import me.apex.hades.check.impl.other.client.ClientA;
import me.apex.hades.check.impl.player.fastuse.FastUseA;
import me.apex.hades.check.impl.player.interactreach.InteractReachA;
import me.apex.hades.check.impl.player.invmove.InvMoveA;
import me.apex.hades.check.impl.player.nofall.NoFallA;
import me.apex.hades.check.impl.player.timer.TimerA;

import java.util.ArrayList;
import java.util.List;

public class CheckManager {

    public static final Class[] CHECKS = new Class[]{
            AngleA.class,
            AngleB.class,
            AuraA.class,
            AuraB.class,
            AuraC.class,
            AutoBlockA.class,
            AutoBlockB.class,
            AutoClickerA.class,
            AutoClickerB.class,
            AutoClickerC.class,
            AutoClickerD.class,
            BadRotationsA.class,
            CriticalsA.class,
            NoSwingA.class,
            ReachA.class,
            VelocityA.class,
            FastLadderA.class,
            FlyA.class,
            FlyB.class,
            InvalidA.class,
            MotionA.class,
            MotionB.class,
            MotionC.class,
            NoSlowA.class,
            ScaffoldA.class,
            ScaffoldB.class,
            SpeedA.class,
            SpeedB.class,
            SpeedC.class,
            SpeedD.class,
            SprintA.class,
            ClientA.class,
            FastUseA.class,
            InteractReachA.class,
            InvMoveA.class,
            NoFallA.class,
            TimerA.class,
    };

    public static List<Check> loadChecks() {
        final List<Check> checklist = new ArrayList<>();
        for(Class clazz : CHECKS) {
            try {
                checklist.add((Check) clazz.getConstructor().newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return checklist;
    }

    public static CheckInfo getCheckInfo(Check check) {
        return check.getClass().getAnnotation(CheckInfo.class);
    }
}
