package me.apex.hades.processors;

import me.apex.hades.Hades;
import me.apex.hades.objects.User;
import me.apex.hades.utils.PacketUtils;
import me.apex.hades.utils.TaskUtils;
import me.purplex.packetevents.enums.PlayerDigType;
import me.purplex.packetevents.event.impl.PacketReceiveEvent;
import me.purplex.packetevents.packet.Packet;
import me.purplex.packetevents.packetwrappers.in.blockdig.WrappedPacketInBlockDig;
import me.purplex.packetevents.packetwrappers.in.flying.WrappedPacketInFlying;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Listener;

public enum MovementProcessor implements Listener {
    INSTANCE;

    public void processMovement(PacketReceiveEvent e, User user) {
        if (PacketUtils.isFlyingPacket(e.getPacketName())) {
            WrappedPacketInFlying packet = new WrappedPacketInFlying(e.getPacket());
            Location location = new Location(user.getPlayer().getWorld(), packet.getX(), packet.getY(), packet.getZ(), packet.getYaw(), packet.getPitch());

            if (user.getLastLocation() != null) {
                if (!e.getPacketName().contains("Look")) {
                    location.setYaw(user.getLocation().getYaw());
                    location.setPitch(user.getLocation().getPitch());
                }

                if (!e.getPacketName().contains("Position")) {
                    location.setX(user.getLocation().getX());
                    location.setY(user.getLocation().getY());
                    location.setZ(user.getLocation().getZ());
                }
            }

            Location lastLocation = user.getLocation() != null ? user.getLocation() : location;

            user.setLastLocation(lastLocation);
            user.setLocation(location);

            double lastDeltaY = user.getDeltaY();
            double deltaY = location.getY() - lastLocation.getY();

            user.setLastDeltaY(lastDeltaY);
            user.setDeltaY(deltaY);

            double lastDeltaXZ = user.getDeltaXZ();
            double deltaXZ = location.toVector().clone().setY(0.0D).distance(lastLocation.toVector().clone().setY(0.0D));

            user.setLastDeltaXZ(lastDeltaXZ);
            user.setDeltaXZ(deltaXZ);

            float lastDeltaYaw = user.getDeltaYaw();
            float deltaYaw = Math.abs(location.getYaw() - lastLocation.getYaw());

            user.setLastDeltaYaw(lastDeltaYaw);
            user.setDeltaYaw(deltaYaw);

            float lastDeltaPitch = user.getDeltaPitch();
            float deltaPitch = Math.abs(location.getPitch() - lastLocation.getPitch());

            user.setLastDeltaPitch(lastDeltaPitch);
            user.setDeltaPitch(deltaPitch);

            //Check Surroundings
            if (Hades.getInstance().isEnabled()) {
                TaskUtils.run(() -> {
                    // User should never be null
                    if (user.getPlayer() == null) return;
                    if (user.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).getType().toString().contains("ICE")
                            || user.getPlayer().getLocation().clone().add(0, -0.5, 0).getBlock().getRelative(BlockFace.DOWN).getType().toString().contains("ICE")) {
                        user.setIceTicks(user.getIceTicks() + 1);
                    }else user.setIceTicks(0);
                    if (user.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).getType().toString().contains("SLIME")) {
                    	user.setSlimeTicks(user.getSlimeTicks() + 1);
                    }else user.setSlimeTicks(0);
                });
            }
            
            //Reset Teleport
            user.setTeleportTicks(0);
            
            //Update Air/Ground Ticks
            if(user.onGround()) {
            	user.setGroundTicks(user.getGroundTicks() + 1);
            	user.setAirTicks(0);
            }else {
            	user.setAirTicks(user.getAirTicks() + 1);
            	user.setGroundTicks(0);
            }
            
            //Update Client Ground Ticks
            if(packet.isOnGround()) {
            	user.setClientGroundTicks(user.getClientGroundTicks() + 1);
            }else user.setClientGroundTicks(0);

            //Process Optifine
            OptifineProcessor.INSTANCE.processOptifine(user);

            if (!user.onGround() && user.getPlayer().isFlying()){
                user.setFlyAFix(true);
            }else if(user.onGround() && !user.getPlayer().isFlying()){
                user.setFlyAFix(false);
            }
        } else if (e.getPacketName().equalsIgnoreCase(Packet.Client.BLOCK_DIG)) {
        	WrappedPacketInBlockDig packet = new WrappedPacketInBlockDig(e.getPacket());
            if (packet.getDigType() == PlayerDigType.START_DESTROY_BLOCK) {
                user.setDigging(true);
            } else if (packet.getDigType() == PlayerDigType.STOP_DESTROY_BLOCK
                    || packet.getDigType() == PlayerDigType.ABORT_DESTROY_BLOCK) {
                user.setDigging(false);
            }
        }else if (e.getPacketName().equalsIgnoreCase(Packet.Server.POSITION)){
            user.setLastServerPosition(System.currentTimeMillis());
        }
    }
}
