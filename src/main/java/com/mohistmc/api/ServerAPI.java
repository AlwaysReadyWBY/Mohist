package com.mohistmc.api;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.forgespi.language.IModInfo;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.EnderDragon;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerAPI {

    public static HashSet<String> modlists = new HashSet();
    public static HashSet<String> channels = new HashSet<>();
    public static Map<String, String> forgecmdper = new ConcurrentHashMap();
    public static List<Command> forgecmd = new ArrayList<>();
    public static Map<net.minecraft.world.entity.EntityType<?>, String> entityTypeMap = new ConcurrentHashMap<>();
    public static Map<Integer, EnderDragon.Phase> phasetypeMap = new ConcurrentHashMap<>();

    public static boolean yes_steve_model() {
        return modlists.contains("yes_steve_model");
    };

    static {
        for (IModInfo modInfo : ModLoader.getModList().getMods()) {
            modlists.add(modInfo.getModId());
        }
    }

    // Don't count the default number of mods
    public static int getModSize() {
        return ModLoader.getModList().size();
    }

    public static String getModList() {
        return modlists.toString();
    }

    public static Boolean hasMod(String modid) {
        return getModList().contains(modid);
    }

    public static Boolean hasPlugin(String pluginname) {
        return Bukkit.getPluginManager().getPlugin(pluginname) != null;
    }

    public static void registerBukkitEvents(Listener listener, Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }

    public static MinecraftServer getNMSServer() {
        return MinecraftServer.getServer();
    }
}
