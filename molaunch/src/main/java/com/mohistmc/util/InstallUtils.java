/*
 * Mohist - MohistMC
 * Copyright (C) 2018-2022.
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

package com.mohistmc.util;

import com.mohistmc.MohistMCStart;
import com.mohistmc.util.i18n.i18n;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarFile;

public class InstallUtils {
    private static final PrintStream origin = System.out;
    public static String libPath = JarTool.getJarDir() + "/libraries/";

    public static String mohistVer = Version.MOHIST.value;
    public static String forgeVer = Version.FORGE.value;
    public static String mcpVer = Version.MCP.value;
    public static String mcVer = Version.MINECRAFT.value;

    public static String forgeStart = libPath + "net/minecraftforge/forge/" + mcVer + "-" + forgeVer + "/forge-" + mcVer + "-" + forgeVer;
    public static File universalJar = new File(forgeStart + "-universal.jar");
    public static File serverJar = new File(forgeStart + "-server.jar");

    public static File lzma = new File(libPath + "com/mohistmc/installation/data/server.lzma");
    public static File installInfo = new File(libPath + "com/mohistmc/installation/installInfo");

    public static String otherStart = libPath + "net/minecraft/server/" + mcVer + "-" + mcpVer + "/server-" + mcVer + "-" + mcpVer;
    public static File extra = new File(otherStart + "-extra.jar");
    public static File slim = new File(otherStart + "-slim.jar");
    public static File srg = new File(otherStart + "-srg.jar");

    public static String mcpStart = libPath + "de/oceanlabs/mcp/mcp_config/" + mcVer + "-" + mcpVer + "/mcp_config-" + mcVer + "-" + mcpVer;
    public static File mcpZip = new File(mcpStart + ".zip");
    public static File mcpTxt = new File(mcpStart + "-mappings.txt");

    public static File minecraft_server = new File(libPath + "minecraft_server." +  mcVer + ".jar");

    public static File fmlloader = new File(libPath + "net/minecraftforge/fmlloader/" + mcVer + "-" + forgeVer + "/fmlloader-" + mcVer + "-" + forgeVer + ".jar");
    public static File fmlcore = new File(libPath + "net/minecraftforge/fmlcore/" + mcVer + "-" + forgeVer + "/fmlcore-" + mcVer + "-" + forgeVer + ".jar");
    public static File javafmllanguage = new File(libPath + "net/minecraftforge/javafmllanguage/" + mcVer + "-" + forgeVer + "/javafmllanguage-" + mcVer + "-" + forgeVer + ".jar");
    public static File mclanguage = new File(libPath + "net/minecraftforge/mclanguage/" + mcVer + "-" + forgeVer + "/mclanguage-" + mcVer + "-" + forgeVer + ".jar");

    public static File mojmap = new File(otherStart + "-mappings.txt");
    public static File mc_unpacked = new File(otherStart + "-unpacked.jar");

    public static File mergedMapping = new File(mcpStart + "-mappings-merged.txt");

    public static File win_args  = new File(libPath + "net/minecraftforge/forge/win_args.txt");
    public static File unix_args  = new File(libPath + "net/minecraftforge/forge/unix_args.txt");
    public static File jvm_args  = new File(JarTool.getJarDir() + "/user_jvm_args.txt");

    public static void startInstallation() throws Exception {
        System.out.println(i18n.get("installation.start"));
        copyFileFromJar(lzma, "data/server.lzma");
        copyFileFromJar(universalJar, "data/forge-1.19-" + Version.FORGE.value + "-universal.jar");

        copyFileFromJar(lzma, "data/server.lzma");
        copyFileFromJar(universalJar, "data/forge-" + mcVer + "-" + forgeVer + "-universal.jar");
        copyFileFromJar(fmlloader, "data/fmlloader-" + mcVer + "-" + forgeVer + ".jar");
        copyFileFromJar(fmlcore, "data/fmlcore-" + mcVer + "-" + forgeVer + ".jar");
        copyFileFromJar(javafmllanguage, "data/javafmllanguage-" + mcVer + "-" + forgeVer + ".jar");
        copyFileFromJar(mclanguage, "data/mclanguage-" + mcVer + "-" + forgeVer + ".jar");

        copyFileFromJar(win_args,"data/win_args.txt");
        copyFileFromJar(unix_args,"data/unix_args.txt");
        copyFileFromJar(jvm_args,"data/user_jvm_args.txt");

        if (mohistVer == null || mcpVer == null) {
            System.out.println("[Mohist] There is an error with the installation, the forge / mcp version is not set.");
            System.exit(0);
        }

        if (minecraft_server.exists()) {
            mute();
            run("net.minecraftforge.installertools.ConsoleTool",
                    new String[]{"--task", "BUNDLER_EXTRACT", "--input", minecraft_server.getAbsolutePath(), "--output", libPath, "--libraries"},
                    new URL[]{
                            stringToUrl(libPath + "net/minecraftforge/installertools/1.3.0/installertools-1.3.0.jar"),
                            stringToUrl(libPath + "net/md-5/SpecialSource/1.11.0/SpecialSource-1.11.0.jar"),
                            stringToUrl(libPath + "net/sf/jopt-simple/jopt-simple/6.0-alpha-3/jopt-simple-6.0-alpha-3.jar"),
                            stringToUrl(libPath + "com/google/code/gson/gson/2.8.9/gson-2.8.9.jar"),
                            stringToUrl(libPath + "de/siegmar/fastcsv/2.0.0/fastcsv-2.0.0.jar"),
                            stringToUrl(libPath + "net/minecraftforge/srgutils/0.4.11/srgutils-0.4.11.jar"),
                            stringToUrl(libPath + "org/ow2/asm/asm-commons/9.3/asm-commons-9.3.jar"),
                            stringToUrl(libPath + "com/google/guava/guava/31.0.1-jre/guava-31.0.1-jre.jar"),
                            stringToUrl(libPath + "com/opencsv/opencsv/4.4/opencsv-4.4.jar"),
                            stringToUrl(libPath + "org/ow2/asm/asm-analysis/9.3/asm-analysis-9.3.jar"),
                            stringToUrl(libPath + "org/ow2/asm/asm-tree/9.3/asm-tree-9.3.jar"),
                            stringToUrl(libPath + "org/ow2/asm/asm/9.3/asm-9.3.jar"),
                            stringToUrl(libPath + "org/apache/commons/commons-text/1.3/commons-text-1.3.jar"),
                            stringToUrl(libPath + "org/apache/commons/commons-lang3/3.12.0/commons-lang3-3.12.0.jar"),
                            stringToUrl(libPath + "commons-beanutils/commons-beanutils/1.9.3/commons-beanutils-1.9.3.jar"),
                            stringToUrl(libPath + "org/apache/commons/commons-collections4/4.2/commons-collections4-4.2.jar"),
                            stringToUrl(libPath + "commons-logging/commons-logging/1.2/commons-logging-1.2.jar"),
                            stringToUrl(libPath + "commons-collections/commons-collections/3.2.2/commons-collections-3.2.2.jar")
            });
            unmute();
            if (!mc_unpacked.exists()) {
                System.out.println("[Mohist] Extract bundler");
                mute();
                run("net.minecraftforge.installertools.ConsoleTool",
                        new String[]{"--task", "BUNDLER_EXTRACT", "--input", minecraft_server.getAbsolutePath(), "--output", mc_unpacked.getAbsolutePath(), "--jar-only"},
                        new URL[]{
                                stringToUrl(libPath + "net/minecraftforge/installertools/1.3.0/installertools-1.3.0.jar"),
                                stringToUrl(libPath + "net/md-5/SpecialSource/1.11.0/SpecialSource-1.11.0.jar"),
                                stringToUrl(libPath + "net/sf/jopt-simple/jopt-simple/6.0-alpha-3/jopt-simple-6.0-alpha-3.jar"),
                                stringToUrl(libPath + "com/google/code/gson/gson/2.8.9/gson-2.8.9.jar"),
                                stringToUrl(libPath + "de/siegmar/fastcsv/2.0.0/fastcsv-2.0.0.jar"),
                                stringToUrl(libPath + "net/minecraftforge/srgutils/0.4.11/srgutils-0.4.11.jar"),
                                stringToUrl(libPath + "org/ow2/asm/asm-commons/9.3/asm-commons-9.3.jar"),
                                stringToUrl(libPath + "com/google/guava/guava/31.0.1-jre/guava-31.0.1-jre.jar"),
                                stringToUrl(libPath + "com/opencsv/opencsv/4.4/opencsv-4.4.jar"),
                                stringToUrl(libPath + "org/ow2/asm/asm-analysis/9.3/asm-analysis-9.3.jar"),
                                stringToUrl(libPath + "org/ow2/asm/asm-tree/9.3/asm-tree-9.3.jar"),
                                stringToUrl(libPath + "org/ow2/asm/asm/9.3/asm-9.3.jar"),
                                stringToUrl(libPath + "org/apache/commons/commons-text/1.3/commons-text-1.3.jar"),
                                stringToUrl(libPath + "org/apache/commons/commons-lang3/3.12.0/commons-lang3-3.12.0.jar"),
                                stringToUrl(libPath + "commons-beanutils/commons-beanutils/1.9.3/commons-beanutils-1.9.3.jar"),
                                stringToUrl(libPath + "org/apache/commons/commons-collections4/4.2/commons-collections4-4.2.jar"),
                                stringToUrl(libPath + "commons-logging/commons-logging/1.2/commons-logging-1.2.jar"),
                                stringToUrl(libPath + "commons-collections/commons-collections/3.2.2/commons-collections-3.2.2.jar")
                        });
                unmute();
            }
        } else {
            System.out.println(i18n.get("installation.minecraftserver"));
        }



        if (mcpZip.exists()) {
            if (!mcpTxt.exists()) {

                // MAKE THE MAPPINGS TXT FILE

                System.out.println(i18n.get("installation.mcp"));
                mute();
                run("net.minecraftforge.installertools.ConsoleTool",
                        new String[]{"--task", "MCP_DATA", "--input", mcpZip.getAbsolutePath(), "--output", mcpTxt.getAbsolutePath(), "--key", "mappings"},
                        new URL[]{
                                stringToUrl(libPath + "net/minecraftforge/installertools/1.3.0/installertools-1.3.0.jar"),
                                stringToUrl(libPath + "net/md-5/SpecialSource/1.11.0/SpecialSource-1.11.0.jar"),
                                stringToUrl(libPath + "net/sf/jopt-simple/jopt-simple/6.0-alpha-3/jopt-simple-6.0-alpha-3.jar"),
                                stringToUrl(libPath + "com/google/code/gson/gson/2.8.9/gson-2.8.9.jar"),
                                stringToUrl(libPath + "de/siegmar/fastcsv/2.0.0/fastcsv-2.0.0.jar"),
                                stringToUrl(libPath + "org/ow2/asm/asm-commons/9.3/asm-commons-9.3.jar"),
                                stringToUrl(libPath + "com/google/guava/guava/31.0.1-jre/guava-31.0.1-jre.jar"),
                                stringToUrl(libPath + "com/opencsv/opencsv/4.4/opencsv-4.4.jar"),
                                stringToUrl(libPath + "org/ow2/asm/asm-analysis/9.3/asm-analysis-9.3.jar"),
                                stringToUrl(libPath + "org/ow2/asm/asm-tree/9.3/asm-tree-9.3jar"),
                                stringToUrl(libPath + "org/ow2/asm/asm/9.3/asm-9.3.jar")});
                unmute();
            }
        } else {
            System.out.println(i18n.get("installation.mcpfilemissing"));
            System.exit(0);
        }

        if (isCorrupted(extra)) extra.delete();
        if (isCorrupted(slim)) slim.delete();
        if (isCorrupted(srg)) srg.delete();

        if (!slim.exists() || !extra.exists()) {
            System.out.println(i18n.get("installation.jars"));
            mute();
            run("net.minecraftforge.jarsplitter.ConsoleTool",
                    new String[]{"--input", libPath + "minecraft_server.1.19.jar", "--slim", slim.getAbsolutePath(), "--extra", extra.getAbsolutePath(), "--srg", mcpTxt.getAbsolutePath()},
                    new URL[]{
                            stringToUrl(libPath + "net/minecraftforge/srgutils/0.4.11/srgutils-0.4.11.jar"),
                            stringToUrl(libPath + "net/minecraftforge/jarsplitter/1.1.4/jarsplitter-1.1.4.jar"),
                            stringToUrl(libPath + "net/sf/jopt-simple/jopt-simple/6.0-alpha-3/jopt-simple-6.0-alpha-3.jar")});
            unmute();
        }

        if (!srg.exists()) {
            System.out.println(i18n.get("installation.srgjar"));
            run("net.minecraftforge.fart.Main", new String[]{"--input", slim.getAbsolutePath(), "--output", srg.getAbsolutePath(), "--names", mcpTxt.getAbsolutePath(), "--ann-fix", "--ids-fix", "--src-fix", "--record-fix"},
                    new URL[]{
                            stringToUrl(libPath + "net/minecraftforge/ForgeAutoRenamingTool/0.1.22/ForgeAutoRenamingTool-0.1.22-all.jar"),
                            stringToUrl(libPath + "net/minecraftforge/srgutils/0.4.11/srgutils-0.4.11.jar"),
                            stringToUrl(libPath + "org/ow2/asm/asm-commons/9.3/asm-commons-9.3.jar"),
                            stringToUrl(libPath + "net/sf/jopt-simple/jopt-simple/6.0-alpha-3/jopt-simple-6.0-alpha-3.jar"),
                            stringToUrl(libPath + "org/ow2/asm/asm-analysis/9.3/asm-analysis-9.3.jar"),
                            stringToUrl(libPath + "org/ow2/asm/asm-tree/9.3/asm-tree-9.3.jar"),
                            stringToUrl(libPath + "org/ow2/asm/asm/9.3/asm-9.3.jar")});
        }

        String storedServerMD5 = null;
        String storedMohistMD5 = null;
        String serverMD5 = MD5Util.getMd5(serverJar);
        String mohistMD5 = MD5Util.getMd5(new File(MohistMCStart.class.getProtectionDomain().getCodeSource().getLocation().toURI()));

        if (installInfo.exists()) {
            List<String> infoLines = Files.readAllLines(installInfo.toPath());
            if (infoLines.size() > 0)
                storedServerMD5 = infoLines.get(0);
            if (infoLines.size() > 1)
                storedMohistMD5 = infoLines.get(1);
        }

        if (!serverJar.exists()
                || storedServerMD5 == null
                || storedMohistMD5 == null
                || !storedServerMD5.equals(serverMD5)
                || !storedMohistMD5.equals(mohistMD5)) {
            System.out.println(i18n.get("installation.forgejar"));
            mute();
            run("net.minecraftforge.binarypatcher.ConsoleTool", new String[]{"--clean", srg.getAbsolutePath(), "--output", serverJar.getAbsolutePath(), "--apply", lzma.getAbsolutePath()},
                    new URL[]{
                            stringToUrl(libPath + "net/minecraftforge/binarypatcher/1.1.1/binarypatcher-1.1.1.jar"),
                            stringToUrl(libPath + "commons-io/commons-io/2.11.0/commons-io-2.11.0.jar"),
                            stringToUrl(libPath + "com/google/guava/guava/31.0.1-jre/guava-31.0.1-jre.jar"),
                            stringToUrl(libPath + "net/sf/jopt-simple/jopt-simple/6.0-alpha-3/jopt-simple-6.0-alpha-3.jar"),
                            stringToUrl(libPath + "com/github/jponge/lzma-java/1.3/lzma-java-1.3.jar"),
                            stringToUrl(libPath + "com/nothome/javaxdelta/2.0.1/javaxdelta-2.0.1.jar"),
                            stringToUrl(libPath + "com/google/code/findbugs/jsr305/3.0.2/jsr305-3.0.2.jar"),
                            stringToUrl(libPath + "org/checkerframework/checker-qual/2.0.0/checker-qual-2.0.0.jar"),
                            stringToUrl(libPath + "com/google/errorprone/error_prone_annotations/2.1.3/error_prone_annotations-2.1.3.jar"),
                            stringToUrl(libPath + "com/google/j2objc/j2objc-annotations/1.1/j2objc-annotations-1.1.jar"),
                            stringToUrl(libPath + "org/codehaus/mojo/animal-sniffer-annotations/1.14/animal-sniffer-annotations-1.14.jar"),
                            stringToUrl(libPath + "trove/trove/1.0.2/trove-1.0.2.jar")});
            unmute();
            serverMD5 = MD5Util.getMd5(serverJar);
        }

        FileWriter fw = new FileWriter(installInfo);
        fw.write(serverMD5 + "\n");
        fw.write(mohistMD5);
        fw.close();

        System.out.println(i18n.get("installation.finished"));
    }

    private static void run(String mainClass, String[] args, URL[] classPath) throws Exception {
        Class.forName(mainClass, true, new URLClassLoader(classPath, getParentClassloader())).getDeclaredMethod("main", String[].class).invoke(null, new Object[] { args });
    }

    private static ClassLoader getParentClassloader() {
        try {
            return (ClassLoader) ClassLoader.class.getDeclaredMethod("getPlatformClassLoader").invoke(null);
        } catch (Exception e) {
            return null;
        }
    }

    private static URL stringToUrl(String path) throws Exception {
        new JarLoader().loadJar(new File(path));
        return new File(path).toURI().toURL();
    }

    /*
    THIS IS TO NOT SPAM CONSOLE WHEN IT WILL PRINT A LOT OF THINGS
     */
    private static void mute() throws Exception {
        File out = new File(libPath + "com/mohistmc/installation/installationLogs.txt");
        if (!out.exists()) {
            out.getParentFile().mkdirs();
            out.createNewFile();
        }
        System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream(out))));
    }

    private static void unmute() {
        System.setOut(origin);
    }

    private static void copyFileFromJar(File file, String pathInJar) throws IOException {
        InputStream is = MohistMCStart.class.getClassLoader().getResourceAsStream(pathInJar);
        if (!file.exists() || !MD5Util.getMd5(file).equals(MD5Util.getMd5(is)) || file.length() <= 1) {
            file.getParentFile().mkdirs();
            file.createNewFile();
            if (is != null) {
                try {
                    Files.copy(is, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                }
            }
            else {
                System.out.println("[Mohist] The file " + file.getName() + " doesn't exists in the Mohist jar !");
                System.exit(0);
            }
        }
    }

    private static boolean isCorrupted(File f) {
        try {
            JarFile j = new JarFile(f);
            j.close();
            return false;
        } catch (IOException e) {
            return true;
        }
    }
}