package me.apex.hades.event.impl.packetevents;

import io.github.retrooper.packetevents.enums.minecraft.PlayerAction;
import lombok.Getter;
import me.apex.hades.event.Event;
import org.bukkit.entity.Entity;

@Getter
public class EntityActionEvent extends Event {

    private final int entityId;
    private final Entity entity;
    private final int jumpBoost;
    private final PlayerAction action;

    public EntityActionEvent(int entityId, Entity entity, int jumpBoost, PlayerAction action) {
        this.entityId = entityId;
        this.entity = entity;
        this.jumpBoost = jumpBoost;
        this.action = action;
    }

}
