package me.apex.hades.check;

import me.apex.hades.check.impl.combat.Angle;
import me.apex.hades.check.impl.combat.aura.AuraA;
import me.apex.hades.check.impl.combat.aura.AuraB;
import me.apex.hades.check.impl.combat.aura.AuraC;
import me.apex.hades.check.impl.combat.aura.AuraD;
import me.apex.hades.check.impl.combat.AutoBlock;
import me.apex.hades.check.impl.combat.autoblock.AutoBlockB;
import me.apex.hades.check.impl.combat.AutoClicker;
import me.apex.hades.check.impl.combat.autoclicker.AutoClickerB;
import me.apex.hades.check.impl.combat.autoclicker.AutoClickerC;
import me.apex.hades.check.impl.combat.autoclicker.AutoClickerD;
import me.apex.hades.check.impl.combat.Pattern;
import me.apex.hades.check.impl.combat.Criticals;
import me.apex.hades.check.impl.combat.NoSwing;
import me.apex.hades.check.impl.combat.Reach;
import me.apex.hades.check.impl.combat.Velocity;
import me.apex.hades.check.impl.movement.FastLadder;
import me.apex.hades.check.impl.movement.Flight;
import me.apex.hades.check.impl.movement.fly.FlyB;
import me.apex.hades.check.impl.movement.Invalid;
import me.apex.hades.check.impl.movement.Motion;
import me.apex.hades.check.impl.movement.motion.MotionB;
import me.apex.hades.check.impl.movement.motion.MotionC;
import me.apex.hades.check.impl.movement.NoSlow;
import me.apex.hades.check.impl.movement.Scaffold;
import me.apex.hades.check.impl.movement.scaffold.ScaffoldB;
import me.apex.hades.check.impl.movement.scaffold.ScaffoldC;
import me.apex.hades.check.impl.movement.Speed;
import me.apex.hades.check.impl.movement.speed.SpeedB;
import me.apex.hades.check.impl.movement.speed.SpeedC;
import me.apex.hades.check.impl.movement.speed.SpeedD;
import me.apex.hades.check.impl.movement.Sprint;
import me.apex.hades.check.impl.other.Client;
import me.apex.hades.check.impl.player.FastUse;
import me.apex.hades.check.impl.player.InteractReach;
import me.apex.hades.check.impl.player.InventoryMove;
import me.apex.hades.check.impl.player.NoFall;
import me.apex.hades.check.impl.player.Timer;

import java.util.ArrayList;
import java.util.List;

public class CheckManager {

    public static final Class[] CHECKS = new Class[]{
            Angle.class,
            AuraA.class,
            AuraB.class,
            AuraC.class,
            AuraD.class,
            AutoBlock.class,
            AutoBlockB.class,
            AutoClicker.class,
            AutoClickerB.class,
            AutoClickerC.class,
            AutoClickerD.class,
            Pattern.class,
            Criticals.class,
            NoSwing.class,
            Reach.class,
            Velocity.class,
            FastLadder.class,
            Flight.class,
            FlyB.class,
            Invalid.class,
            Motion.class,
            MotionB.class,
            MotionC.class,
            NoSlow.class,
            Scaffold.class,
            ScaffoldB.class,
            ScaffoldC.class,
            Speed.class,
            SpeedB.class,
            SpeedC.class,
            SpeedD.class,
            Sprint.class,
            Client.class,
            FastUse.class,
            InteractReach.class,
            InventoryMove.class,
            NoFall.class,
            Timer.class,
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
