<img src="https://i.loli.net/2021/01/17/yLBVlWPbfa76EJu.png">

## Mohist-1.7.10

[![](https://ci.codemc.org/buildStatus/icon?job=MohistMC%2FMohist-1.7.10)](https://ci.codemc.org/job/MohistMC/job/Mohist-1.7.10/)
![](https://img.shields.io/github/stars/MohistMC/Mohist.svg?label=Stars)
![](https://img.shields.io/github/license/MohistMC/Mohist.svg)
[![](https://img.shields.io/badge/Forge-1.7.10--10.13.4.1614-brightgreen.svg?colorB=26303d)](http://files.minecraftforge.net/maven/net/minecraftforge/forge/index_1.7.10.html)
[![](https://img.shields.io/badge/Spigot-1.7.10-brightgreen.svg?colorB=DC3340)](https://papermc.io/downloads#Paper-1.12)
![](https://img.shields.io/badge/OracleJdk-8u241-brightgreen.svg?colorB=469C00)
![](https://img.shields.io/badge/Gradle-2.8-brightgreen.svg?colorB=469C00)
![](https://img.shields.io/badge/ideaIU-jbr8-brightgreen.svg?colorB=469C00)

### Getting Help
   [**Home**](https://mohistmc.com//)
   [**bStats**](https://bstats.org/plugin/bukkit/Mohist)
   [**Discord**](https://discord.gg/mohist)
   [**QQ**](https://jq.qq.com/?_wv=1027&k=5YIRYnH)  
   
   [![](https://bstats.org/signatures/server-implementation/Mohist.svg)](https://bstats.org/plugin/server-implementation/Mohist/6762)

### Download
* [**Jenkins**](https://ci.codemc.org/job/MohistMC/job/Mohist-1.7.10/)

### Building
* Checkout project
  * You can use IDE or clone from console:
  `git clone -b 1.7.10 https://github.com/MohistMC/Mohist.git`
* Setup
  * Setting up submodules:
  `git submodule update --init --recursive`
* Building
  * Build the project for Linux:
  `./gradlew setupCauldron` 
  `./gradlew launch4j`
  * or for Windows:
  `./gradlew.bat setupCauldron ` 
  `./gradlew.bat launch4j `

All builds will be in `.\Mohist\build\distributions\`

Mohist-xxxxx-server.jar - is the server we should run it


Thanks to the following projects
------
* [**Bukkit**](https://hub.spigotmc.org/stash/scm/spigot/bukkit.git) - plugin support.
* [**Paper**](https://github.com/PaperMC/Paper.git) - performance optimizations.
* [**CraftBukkit**](https://hub.spigotmc.org/stash/scm/spigot/craftbukkit.git) - plugin support.
* [**Spigot**](https://hub.spigotmc.org/stash/scm/spigot/spigot.git) - plugin support.
* [**MinecraftForge**](https://github.com/MinecraftForge/MinecraftForge.git) - mod support.
* [**Thermos**](https://github.com/CyberdyneCC/Thermos.git) - Partial code source.
* [**um_bukkit**](https://github.com/TechCatOther/um_bukkit.git) - Partial code source.

A Special Thanks To:
-------------
<a href="https://serverjars.com/"><img src="https://serverjars.com/img/logo_white.svg" width="200"></a>
<a href="https://ci.codemc.io/"><img src="https://i.loli.net/2020/03/11/YNicj3PLkU5BZJT.png" width="172"></a>
<a href="https://www.bisecthosting.com/mohistmc"><img src="https://cdn.discordapp.com/attachments/303673296929685504/709610584680955944/Asset_5.png" width="200"></a>

<a href="https://www.jetbrains.com/"><img src="https://blog.jetbrains.com/wp-content/uploads/2015/12/JetBrains_Drive_to_develop.png" width="200"></a>
![YourKit-Logo](https://www.yourkit.com/images/yklogo.png)

[YourKit](http://www.yourkit.com/), makers of the outstanding java profiler, support open source projects of all kinds with their full featured [Java](https://www.yourkit.com/java/profiler/index.jsp) and [.NET](https://www.yourkit.com/.net/profiler/index.jsp) application profilers. We thank them for granting Mohist an OSS license so that we can make our software the best it can be.

