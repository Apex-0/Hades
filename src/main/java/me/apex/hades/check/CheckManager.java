package me.apex.hades.check;

import me.apex.hades.check.impl.combat.Angle;
import me.apex.hades.check.impl.combat.Killaura;
import me.apex.hades.check.impl.combat.AutoBlock;
import me.apex.hades.check.impl.combat.AutoClicker;
import me.apex.hades.check.impl.combat.Pattern;
import me.apex.hades.check.impl.combat.Criticals;
import me.apex.hades.check.impl.combat.NoSwing;
import me.apex.hades.check.impl.combat.Reach;
import me.apex.hades.check.impl.combat.Velocity;
import me.apex.hades.check.impl.movement.FastLadder;
import me.apex.hades.check.impl.movement.Flight;
import me.apex.hades.check.impl.movement.Invalid;
import me.apex.hades.check.impl.movement.Motion;
import me.apex.hades.check.impl.movement.NoSlow;
import me.apex.hades.check.impl.movement.Scaffold;
import me.apex.hades.check.impl.movement.Speed;
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
            AutoBlock.class,
            AutoClicker.class,
            Criticals.class,
            Killaura.class,
            NoSwing.class,
            Pattern.class,
            Reach.class,
            Velocity.class,
            FastLadder.class,
            Flight.class,
            Invalid.class,
            Motion.class,
            NoSlow.class,
            Scaffold.class,
            Speed.class,
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
