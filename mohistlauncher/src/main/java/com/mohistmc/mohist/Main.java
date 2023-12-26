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

package com.mohistmc.mohist;

import com.mohistmc.i18n.i18n;
import com.mohistmc.mohist.action.v_1_20_R3;
import com.mohistmc.mohist.config.MohistConfigUtil;
import com.mohistmc.mohist.feature.AutoDeleteMods;
import com.mohistmc.mohist.libraries.DefaultLibraries;
import com.mohistmc.mohist.libraries.Libraries;
import com.mohistmc.mohist.util.DataParser;
import com.mohistmc.mohist.util.EulaUtil;
import com.mohistmc.mohist.util.JarLoader;
import java.io.File;
import java.util.Scanner;
import net.minecraftforge.bootstrap.ForgeBootstrap;

public class Main {
    static final boolean DEBUG = Boolean.getBoolean("mohist.debug");
    public static String MCVERSION;
    public static i18n i18n;
    public static StringBuilder classpath = new StringBuilder(System.getProperty("java.class.path"));

    public static String getVersion() {
        return (Main.class.getPackage().getImplementationVersion() != null) ? Main.class.getPackage().getImplementationVersion() : MCVERSION;
    }

    public static void main(String[] args) throws Exception {
        DataParser.parseVersions();
        MohistConfigUtil.copyMohistConfig();
        MohistConfigUtil.i18n();
        if (MohistConfigUtil.aBoolean("mohist.show_logo", true)) {
            String test = """

                     ███╗   ███╗  ██████╗  ██╗  ██╗ ██╗ ███████╗ ████████╗
                     ████╗ ████║ ██╔═══██╗ ██║  ██║ ██║ ██╔════╝ ╚══██╔══╝
                     ██╔████╔██║ ██║   ██║ ███████║ ██║ ███████╗    ██║
                     ██║╚██╔╝██║ ██║   ██║ ██╔══██║ ██║ ╚════██║    ██║
                     ██║ ╚═╝ ██║ ╚██████╔╝ ██║  ██║ ██║ ███████║    ██║
                     ╚═╝     ╚═╝  ╚═════╝  ╚═╝  ╚═╝ ╚═╝ ╚══════╝    ╚═╝
                                        
                                        
                    %s - %s, Java(%s) %s
                    """;
            System.out.printf(test + "%n", i18n.as("mohist.launch.welcomemessage"), getVersion(), System.getProperty("java.version"), System.getProperty("java.class.version"));
        }

        if (System.getProperty("log4j.configurationFile") == null) {
            System.setProperty("log4j.configurationFile", "log4j2_mohist.xml");
        }

        // if (!MohistConfigUtil.INSTALLATIONFINISHED() && MohistConfigUtil.CHECK_UPDATE()) { UpdateUtils.versionCheck(); }

        DefaultLibraries.run();

        for (Libraries libraries : DefaultLibraries.librariesSet) {
            if (!libraries.path().endsWith(".jar")) {
                continue;
            }
            File file = new File(libraries.path());
            classpath.append(File.pathSeparator).append(file.getAbsolutePath());
            JarLoader.loadJar(file.toPath());
        }
        v_1_20_R3.run();
        System.setProperty("java.class.path", classpath.toString());
        if (MohistConfigUtil.CHECK_CLIENT_MODS()) {
            AutoDeleteMods.jar();
        }

        if (!EulaUtil.hasAcceptedEULA()) {
            System.out.println(i18n.as("eula"));
            while (!"true".equals(new Scanner(System.in).next())) {
            }
            EulaUtil.writeInfos();
        }

        String extraArgs = "--launchTarget forge_server";

        String[] pts = extraArgs.split(" ");
        String[] joined = new String[pts.length + args.length];
        System.arraycopy(pts, 0, joined, 0, pts.length);
        System.arraycopy(args, 0, joined, pts.length, args.length);
        ForgeBootstrap.main(joined);
    }
}
