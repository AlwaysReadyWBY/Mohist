package org.bukkit.craftbukkit.v1_20_R3.block;

import com.google.common.base.Preconditions;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.DecoratedPot;
import org.bukkit.block.DecoratedPot.Side;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftInventoryDecoratedPot;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemType;
import org.bukkit.craftbukkit.v1_20_R3.util.CraftMagicNumbers;
import org.bukkit.inventory.DecoratedPotInventory;

public class CraftDecoratedPot extends CraftBlockEntityState<DecoratedPotBlockEntity> implements DecoratedPot {

    public CraftDecoratedPot(World world, DecoratedPotBlockEntity tileEntity) {
        super(world, tileEntity);
    }

    protected CraftDecoratedPot(CraftDecoratedPot state, Location location) {
        super(state, location);
    }

    @Override
    public DecoratedPotInventory getSnapshotInventory() {
        return new CraftInventoryDecoratedPot(this.getSnapshot());
    }

    @Override
    public DecoratedPotInventory getInventory() {
        if (!this.isPlaced()) {
            return this.getSnapshotInventory();
        }

        return new CraftInventoryDecoratedPot(this.getTileEntity());
    }

    @Override
    public void setSherd(Side face, Material sherd) {
        Preconditions.checkArgument(face != null, "face must not be null");
        Preconditions.checkArgument(sherd == null || sherd == Material.BRICK || Tag.ITEMS_DECORATED_POT_SHERDS.isTagged(sherd), "sherd is not a valid sherd material: %s", sherd);

        Item sherdItem = (sherd != null) ? CraftItemType.bukkitToMinecraft(sherd) : Items.BRICK;
        DecoratedPotBlockEntity.Decorations decorations = getSnapshot().getDecorations(); // PAIL rename Decorations

        switch (face) {
            case BACK -> getSnapshot().decorations = new DecoratedPotBlockEntity.Decorations(sherdItem, decorations.left(), decorations.right(), decorations.front());
            case LEFT -> getSnapshot().decorations = new DecoratedPotBlockEntity.Decorations(decorations.back(), sherdItem, decorations.right(), decorations.front());
            case RIGHT -> getSnapshot().decorations = new DecoratedPotBlockEntity.Decorations(decorations.back(), decorations.left(), sherdItem, decorations.front());
            case FRONT -> getSnapshot().decorations = new DecoratedPotBlockEntity.Decorations(decorations.back(), decorations.left(), decorations.right(), sherdItem);
            default -> throw new IllegalArgumentException("Unexpected value: " + face);
        }
    }

    @Override
    public Material getSherd(Side face) {
        Preconditions.checkArgument(face != null, "face must not be null");

        DecoratedPotBlockEntity.Decorations decorations = getSnapshot().getDecorations(); // PAIL rename Decorations
        Item sherdItem = switch (face) {
            case BACK -> decorations.back();
            case LEFT -> decorations.left();
            case RIGHT -> decorations.right();
            case FRONT -> decorations.front();
            default -> throw new IllegalArgumentException("Unexpected value: " + face);
        };

        return CraftItemType.minecraftToBukkit(sherdItem);
    }

    @Override
    public Map<Side, Material> getSherds() {
        DecoratedPotBlockEntity.Decorations decorations = getSnapshot().getDecorations(); // PAIL rename Decorations

        Map<Side, Material> sherds = new EnumMap<>(Side.class);
        sherds.put(Side.BACK, CraftItemType.minecraftToBukkit(decorations.back()));
        sherds.put(Side.LEFT, CraftItemType.minecraftToBukkit(decorations.left()));
        sherds.put(Side.RIGHT, CraftItemType.minecraftToBukkit(decorations.right()));
        sherds.put(Side.FRONT, CraftItemType.minecraftToBukkit(decorations.front()));
        return sherds;
    }

    @Override
    public List<Material> getShards() {
        return getSnapshot().getDecorations().sorted().map(CraftMagicNumbers::getMaterial).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public CraftDecoratedPot copy() {
        return new CraftDecoratedPot(this, null);
    }

    @Override
    public CraftDecoratedPot copy(Location location) {
        return new CraftDecoratedPot(this, location);
    }
}
