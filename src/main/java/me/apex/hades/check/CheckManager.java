package me.apex.hades.check;

import me.apex.hades.check.impl.combat.*;
import me.apex.hades.check.impl.movement.*;
import me.apex.hades.check.impl.other.Client;
import me.apex.hades.check.impl.player.*;

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
