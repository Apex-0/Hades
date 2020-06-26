package me.apex.hades.event.impl.packetevents;

import io.github.retrooper.packetevents.enums.Direction;
import io.github.retrooper.packetevents.enums.minecraft.PlayerDigType;
import io.github.retrooper.packetevents.utils.vector.Vector3i;
import lombok.Getter;
import me.apex.hades.event.Event;

@Getter
public class DigEvent extends Event {

    private final Vector3i blockPos;
    private final Direction direction;
    private final PlayerDigType digType;

    public DigEvent(Vector3i blockPos, Direction direction, PlayerDigType digType) {
        this.blockPos = blockPos;
        this.direction = direction;
        this.digType = digType;
    }

}
