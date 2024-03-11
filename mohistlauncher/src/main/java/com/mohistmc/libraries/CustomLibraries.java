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

package com.mohistmc.libraries;

import com.mohistmc.MohistMCStart;
import com.mohistmc.util.JarLoader;
import java.io.File;
import java.util.Objects;

public class CustomLibraries {

    public static final File file = new File(MohistMCStart.jarTool.getJarDir() + "/libraries/customize_libraries");

    public static void loadCustomLibs() throws Exception {
        if (!file.exists()) {
            file.mkdirs();
        }

        for (File lib : Objects.requireNonNull(file.listFiles((dir, name) -> name.endsWith(".jar")))) {
            JarLoader.loadJar(lib.toPath());
            System.out.println(lib.getName() + " custom library loaded successfully.");
        }
    }
}
