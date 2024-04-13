/*
 * Mohist - MohistMC
 * Copyright (C) 2018-2024.
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

package com.mohistmc.mohistlauncher.feature;

import com.mohistmc.libraries.Libraries;
import com.mohistmc.libraries.LibrariesDownloadQueue;
import com.mohistmc.mohistlauncher.config.MohistConfigUtil;
import com.mohistmc.mohistlauncher.util.I18n;
import com.mohistmc.tools.ConnectionUtil;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.SneakyThrows;

public class DefaultLibraries {

    public static Set<Libraries> installer = new HashSet<>();


    public static Set<URL> installer() throws MalformedURLException {
        Set<URL> urls = new HashSet<>();
        for (Libraries libraries : installer) {
            File file = new File("libraries", libraries.getPath());
            URL url = file.toURI().toURL();
            urls.add(url);
        }
        return urls;
    }

    @SneakyThrows
    public static void run() {
        String config = MohistConfigUtil.LIBRARIES_DOWNLOADSOURCE.asString();
        String ds = System.getProperty("downloadsource") == null ? config : System.getProperty("downloadsource");
        String downloadSource = config;
        if (!Objects.equals(config, ds) && ConnectionUtil.isValid(ds)) {
            downloadSource = ds;
        }

        LibrariesDownloadQueue queue = LibrariesDownloadQueue.create()
                .inputStream(DefaultLibraries.class.getClassLoader().getResourceAsStream("libraries.txt"))
                .downloadSource(downloadSource)
                .build();
        installer = queue.installer;
        System.out.println(I18n.as("libraries.checking.start"));
        if (queue.needDownload()) {
            System.out.println(I18n.as("libraries.downloadsource", queue.downloadSource.name()));
            System.out.println(I18n.as("libraries.global.percentage"));
            queue.progressBar();
        }

        System.out.println(I18n.as("libraries.check.end"));
    }
}
