/*
 * Mohist - MohistMC
 * Copyright (C) 2018-2023.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.mohistmc.config;

import com.mohistmc.MohistMCStart;
import com.mohistmc.i18n.i18n;
import com.mohistmc.network.download.DownloadSource;
import com.mohistmc.yaml.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class MohistConfigUtil {

    public static final File mohistyml = new File("mohist-config", "mohist.yml");
    public static final YamlConfiguration yml = YamlConfiguration.loadConfiguration(mohistyml);

    public static void copyMohistConfig() {
        try {
            if (!mohistyml.exists()) {
                mohistyml.createNewFile();
            }
        } catch (Exception e) {
            System.out.println("File init exception!");
        }
    }

    public static boolean INSTALLATIONFINISHED() {
        return yml.getBoolean("mohist.installation-finished", false);
    }

    public static boolean CHECK_LIBRARIES() {
        String key = "mohist.check_libraries";
        if (yml.get(key) == null) {
            yml.set(key, true);
            save();
        }
        return yml.getBoolean(key, true);
    }

    public static String defaultSource() {
        String key = "mohist.libraries_downloadsource";
        if (yml.get(key) == null) {
            yml.set(key, DownloadSource.defaultSource.name());
            save();
        }
        return yml.getString(key, DownloadSource.defaultSource.name());
    }

    public static boolean aBoolean(String key, boolean defaultReturn) {
        return yml.getBoolean(key, defaultReturn);
    }

    public static void save() {
        try {
            yml.save(mohistyml);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void i18n() {
        String mohist_lang = yml.getString("mohist.lang", "xx_XX");
        String l = mohist_lang.split("_")[0];
        String c = mohist_lang.split("_")[1];
        MohistMCStart.i18n = new i18n(MohistMCStart.class.getClassLoader(), new Locale(l, c));
    }
}
