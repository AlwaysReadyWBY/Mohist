package com.mohistmc.forge;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableMap;
import com.mohistmc.MohistMC;
import com.mohistmc.api.ServerAPI;
import com.mohistmc.dynamicenum.MohistDynamEnum;
import com.mohistmc.entity.MohistModsEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.bukkit.Fluid;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.craftbukkit.v1_19_R2.enchantments.CraftEnchantment;
import org.bukkit.craftbukkit.v1_19_R2.potion.CraftPotionEffectType;
import org.bukkit.craftbukkit.v1_19_R2.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.v1_19_R2.util.CraftNamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ForgeInjectBukkit {

    public static BiMap<ResourceKey<LevelStem>, World.Environment> environment =
            HashBiMap.create(ImmutableMap.<ResourceKey<LevelStem>, World.Environment>builder()
                    .put(LevelStem.OVERWORLD, World.Environment.NORMAL)
                    .put(LevelStem.NETHER, World.Environment.NETHER)
                    .put(LevelStem.END, World.Environment.THE_END)
                    .build());

    public static Map<Villager.Profession, ResourceLocation> profession = new HashMap<>();
    public static Map<org.bukkit.attribute.Attribute, ResourceLocation> attributemap = new HashMap<>();


    public static void init() {
        addEnumMaterialInItems();
        addEnumMaterialsInBlocks();
        addEnumBiome();
        addEnumEnchantment();
        addEnumPotion();
        addFluid();
        //addEnumPattern();
        addEnumEntity();
        addEnumVillagerProfession();
        //addEnumArt();
    }


    public static void addEnumMaterialInItems() {
        var registry = ForgeRegistries.ITEMS;
        for (Item item : registry) {
            ResourceLocation resourceLocation = registry.getKey(item);
            if (!isMINECRAFT(resourceLocation)) {
                // inject item materials into Bukkit for FML
                String materialName = normalizeName(resourceLocation.toString());
                int id = Item.getId(item);
                Material material = Material.addMaterial(materialName, id, false, resourceLocation.getNamespace());

                if (material != null) {
                    CraftMagicNumbers.ITEM_MATERIAL.put(item, material);
                    CraftMagicNumbers.MATERIAL_ITEM.put(material, item);
                    MohistMC.LOGGER.debug("Save-ITEM: " + material.name() + " - " + materialName);
                }
            }
        }
    }


    public static void addEnumMaterialsInBlocks() {
        var registry = ForgeRegistries.BLOCKS;
        for (Block block : registry) {
            ResourceLocation resourceLocation = registry.getKey(block);
            if (!isMINECRAFT(resourceLocation)) {
                // inject block materials into Bukkit for FML
                String materialName = normalizeName(resourceLocation.toString());
                int id = Item.getId(block.asItem());
                Material material = Material.addMaterial(materialName, id, true, resourceLocation.getNamespace());

                if (material != null) {
                    CraftMagicNumbers.BLOCK_MATERIAL.put(block, material);
                    CraftMagicNumbers.MATERIAL_BLOCK.put(material, block);
                    MohistMC.LOGGER.debug("Save-BLOCK:" + material.name() + " - " + materialName);
                }
            }
        }
    }


    public static void addEnumEnchantment() {
        // Enchantment
        for (Enchantment enchantment : ForgeRegistries.ENCHANTMENTS) {
            org.bukkit.enchantments.Enchantment.registerEnchantment(new CraftEnchantment(enchantment));
        }
        org.bukkit.enchantments.Enchantment.stopAcceptingRegistrations();
    }

    public static void addEnumPotion() {
        // Points
        for (MobEffect mobEffect : ForgeRegistries.MOB_EFFECTS) {
            PotionEffectType pet = new CraftPotionEffectType(mobEffect);
            PotionEffectType.registerPotionEffectType(pet);
        }
        PotionEffectType.stopAcceptingRegistrations();
    }

    public static void addEnumBiome() {
        List<String> map = new ArrayList<>();
        var registry = ForgeRegistries.BIOMES;
        for (net.minecraft.world.level.biome.Biome biome : registry) {
            ResourceLocation resourceLocation = registry.getKey(biome);
            String biomeName = normalizeName(resourceLocation.toString());
            if (!isMINECRAFT(resourceLocation) && !map.contains(biomeName)) {
                map.add(biomeName);
                org.bukkit.block.Biome biomeCB = MohistDynamEnum.addEnum0(org.bukkit.block.Biome.class, biomeName, new Class[0]);
                MohistMC.LOGGER.debug("Save-BIOME:" + biomeCB.name() + " - " + biomeName);
            }
        }
        map.clear();
    }


    public static void addEnumEnvironment() {
        int i = World.Environment.values().length;
        var registry = ServerAPI.getNMSServer().registryAccess().registryOrThrow(Registries.LEVEL_STEM);
        for (Entry<ResourceKey<LevelStem>, LevelStem> entry : registry.entrySet()) {
            ResourceKey<LevelStem> key = entry.getKey();
            World.Environment environment1 = environment.get(key);
            if (environment1 == null) {
                String name = normalizeName(key.location().toString());
                int id = i - 1;
                environment1 = MohistDynamEnum.addEnum(World.Environment.class, name, new Class[]{Integer.TYPE}, new Object[]{id});
                environment.put(key, environment1);
                MohistMC.LOGGER.debug("Registered forge DimensionType as environment {}", environment1);
                i++;
            }
        }
    }

    public static WorldType addEnumWorldType(String name) {
        WorldType worldType = MohistDynamEnum.addEnum0(WorldType.class, name, new Class[]{String.class}, name);
        Map<String, WorldType> BY_NAME = ObfuscationReflectionHelper.getPrivateValue(WorldType.class, null, "BY_NAME");
        BY_NAME.put(name.toUpperCase(), worldType);
        return worldType;
    }

    public static void addEnumEntity() {
        var registry = ForgeRegistries.ENTITY_TYPES;
        for (net.minecraft.world.entity.EntityType<?> entity : registry) {
            ResourceLocation resourceLocation = registry.getKey(entity);
            if (!isMINECRAFT(resourceLocation)) {
                String entityType = normalizeName(resourceLocation.toString());
                int typeId = entityType.hashCode();
                EntityType bukkitType = MohistDynamEnum.addEnum0(EntityType.class, entityType, new Class[]{String.class, Class.class, Integer.TYPE, Boolean.TYPE}, entityType.toLowerCase(), MohistModsEntity.class, typeId, false);
                EntityType.NAME_MAP.put(entityType.toLowerCase(), bukkitType);
                EntityType.ID_MAP.put((short) typeId, bukkitType);
                ServerAPI.entityTypeMap.put(entity, entityType);
            }
        }
    }

    public static void addEnumVillagerProfession() {
        var registry = ForgeRegistries.VILLAGER_PROFESSIONS;
        for (VillagerProfession villagerProfession : registry) {
            ResourceLocation resourceLocation = registry.getKey(villagerProfession);
            if (!isMINECRAFT(resourceLocation)) {
                String name = normalizeName(resourceLocation.toString());
                Villager.Profession vp = MohistDynamEnum.addEnum0(Villager.Profession.class, name, new Class[0]);
                profession.put(vp, resourceLocation);
                MohistMC.LOGGER.debug("Registered forge VillagerProfession as Profession {}", vp.name());
            }
        }
    }

    public static void addEnumAttribute() {
        var registry = ForgeRegistries.ATTRIBUTES;
        for (Attribute attribute : registry) {
            ResourceLocation resourceLocation = registry.getKey(attribute);
            String name = normalizeName(resourceLocation.getPath());
            if (!isMINECRAFT(resourceLocation)) {
                org.bukkit.attribute.Attribute ab = MohistDynamEnum.addEnum0(org.bukkit.attribute.Attribute.class, name, new Class[]{String.class});
                attributemap.put(ab, resourceLocation);
                MohistMC.LOGGER.debug("Registered forge Attribute as Attribute(Bukkit) {}", ab.name());
            }
        }
    }

    public static void addFluid() {
        var registry = ForgeRegistries.FLUIDS;
        for (net.minecraft.world.level.material.Fluid fluidType : registry) {
            ResourceLocation resourceLocation = registry.getKey(fluidType);
            String name = normalizeName(resourceLocation.getPath());
            if (!isMINECRAFT(resourceLocation)) {
                Fluid fluid = MohistDynamEnum.addEnum0(Fluid.class, name, new Class[0]);
                CraftMagicNumbers.FLUIDTYPE_FLUID.put(fluidType, fluid);
                MohistMC.LOGGER.debug("Registered forge Fluid as Fluid(Bukkit) {}", fluid.name());
            }
        }
    }

    public static String normalizeName(String name) {
        return name.toUpperCase(java.util.Locale.ENGLISH).replaceAll("(:|\\s)", "_").replaceAll("\\W", "");
    }

    public static boolean isMINECRAFT(ResourceLocation resourceLocation) {
        return resourceLocation.getNamespace().equals(NamespacedKey.MINECRAFT);
    }
}
