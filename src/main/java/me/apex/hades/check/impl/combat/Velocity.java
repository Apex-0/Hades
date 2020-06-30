package me.apex.hades.check.impl.combat;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.packetevents.FlyingEvent;
import me.apex.hades.user.User;
import me.apex.hades.util.MathUtil;

import java.util.ArrayList;
import java.util.List;

@CheckInfo(name = "Velocity")
public class Velocity extends Check {

    private List<Double> verticals = new ArrayList<>(), horizontalX = new ArrayList<>(), horizontalZ = new ArrayList<>();
    private double preVLA, preVLB, preVLC;

    @Override
    public void onHandle(PacketEvent e, User user) {
        if(e instanceof FlyingEvent) {
            if(user.isVerifyingVelocity() || elapsed(user.getTick(), user.getVelocityTick()) < 6) {
                verticals.add(user.getDeltaY());
            }else {
                if(verticals.size() > 0) {
                    double max = verticals.stream().mapToDouble(d -> d).max().getAsDouble();
                    double min = user.getVelocityY() * 0.99F;

                    if(max <= min
                            && user.liquidTicks() > 20
                            && user.nearWallTicks() > 20
                            && user.climbableTicks() > 20
                            && user.underBlockTicks() > 20) {
                        flag(user, "Vertical", "max = " + max + ", min = " + min);
                    }

                    double lastVertical = -999;
                    for(double vertical : verticals) {
                        if(lastVertical != -999) {
                            if(!MathUtil.isRoughlyEqual(vertical, lastVertical * 0.6F, 0.1)
                                    && user.liquidTicks() > 20
                                    && user.nearWallTicks() > 20
                                    && user.climbableTicks() > 20
                                    && user.underBlockTicks() > 20) {
                                if(++preVLC > 1) {
                                    flag(user, "InvalidY", "d = " + vertical + ", ld = " + lastVertical);
                                }
                            }else preVLC = 0;
                            lastVertical = vertical;
                        }else lastVertical = vertical;
                    }

                    verticals.clear();
                }
            }

            if(user.isVerifyingVelocity() || elapsed(user.getTick(), user.getVelocityTick()) < user.getMaxVelocityTicks()) {
                horizontalX.add(Math.abs(user.getLocation().getX() - user.getLastLocation().getX()));
            }else {
                if(horizontalX.size() > 0) {
                    double max = horizontalX.stream().mapToDouble(d -> d).max().getAsDouble();
                    double min = Math.abs(user.getVelocityX()) * 0.99;

                    if(max <= min
                            && user.liquidTicks() > 20
                            && user.nearWallTicks() > 20
                            && user.climbableTicks() > 20
                            && user.underBlockTicks() > 20) {
                        if(++preVLA > 2) {
                            flag(user, "HorizontalX", "max = " + max + ", min = " + min, true);
                        }
                    }else preVLA *= 0.75;

                    horizontalX.clear();
                }
            }

            if(user.isVerifyingVelocity() || elapsed(user.getTick(), user.getVelocityTick()) < user.getMaxVelocityTicks()) {
                horizontalZ.add(Math.abs(user.getLocation().getZ() - user.getLastLocation().getZ()));
            }else {
                if(horizontalZ.size() > 0) {
                    double max = horizontalZ.stream().mapToDouble(d -> d).max().getAsDouble();
                    double min = Math.abs(user.getVelocityZ()) * 0.99;

                    if(max <= min
                            && user.liquidTicks() > 20
                            && user.nearWallTicks() > 20
                            && user.climbableTicks() > 20
                            && user.underBlockTicks() > 20) {
                        if(++preVLB > 2) {
                            flag(user, "HorizontalZ", "max = " + max + ", min = " + min, true);
                        }
                    }else preVLB *= 0.75;

                    horizontalZ.clear();
                }
            }
        }
    }

}
