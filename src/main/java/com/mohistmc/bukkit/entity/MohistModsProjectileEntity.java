package com.mohistmc.bukkit.entity;

import com.mohistmc.api.EntityAPI;
import java.util.Objects;
import net.minecraft.world.entity.projectile.Projectile;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftProjectile;
import org.bukkit.entity.EntityType;

public class MohistModsProjectileEntity extends CraftProjectile {
    public String entityName;

    public MohistModsProjectileEntity(CraftServer server, Projectile entity) {
        super(server, entity);
        this.entityName = EntityAPI.entityName(entity);
    }

    @Override
    public Projectile getHandle() {
        return (Projectile) this.entity;
    }


    @Override
    public EntityType getType() {
        EntityType type = EntityType.fromName(this.entityName);
        return Objects.requireNonNullElse(type, EntityType.FORGE_MOD_PROJECTILE);
    }

    @Override
    public String toString() {
        return "MohistModsProjectileEntity{" + entityName + '}';
    }
}

