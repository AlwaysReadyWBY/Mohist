package com.mohistmc.entity;

import com.mohistmc.api.EntityAPI;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import org.bukkit.craftbukkit.v1_19_R2.CraftServer;
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftAbstractHorse;
import org.bukkit.entity.EntityCategory;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;

public class MohistModsAbstractHorse extends CraftAbstractHorse {

    public String entityName;

    public MohistModsAbstractHorse(CraftServer server, AbstractHorse entity) {
        super(server, entity);
        this.entityName = EntityAPI.entityName(entity);
    }

    @Override
    public String toString() {
        return "MohistModsAbstractHorse{" + entityName + '}';
    }

    @Override
    public AbstractHorse getHandle() {
        return (AbstractHorse) entity;
    }

    @Override
    public EntityType getType() {
        return EntityAPI.entityType(entityName, EntityType.FORGE_MOD_HORSE);
    }

    @Override
    public Horse.Variant getVariant() {
        return Horse.Variant.FORGE_MOD_HORSE;
    }

    @Override
    public EntityCategory getCategory() {
        return EntityCategory.NONE;
    }
}
