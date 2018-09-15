# Minecraft-RaidBosses
## Installation
### Building the server
1. Download BuildTools from https://hub.spigotmc.org/jenkins/job/BuildTools/
2. Run BuildTools with ```java -jar BuildTools.jar --rev 1.13.1```
3. This will create the server jar file ```spigot-1.13.1.jar```

### Download dependency library
1. Download the plugin HolographicDisplays from ```https://dev.bukkit.org/projects/holographic-displays```

### Setup Eclipse
!! Be sure to use UTF-8 encoding: Window > Preferences > General > Workspace -> Text file encoding: UTF-8

1. Window > Show View > Other... > Git Repositories
2. Click in Git Repositories on ```Clone a repository```
3. Set URI to https://github.com/ReneKolb/Minecraft-RaidBosses.git
4. Set Directory to e.g. ```C:\git\Minecraft-RaidBosses```
5. Create new Java Project, uncheck "use default location" and set to ```C:\git\Minecraft-RaidBosses```
6. Set Dependencies (Libraries): click on "Add External JARs..." and select the created server ```spigot-1.13.1.jar```
7. Also a the HolographicDisplays as dependency
8. After "Finish", there should appear a folder "resources" below the packages. Right-click > Build Path > Use as source folder

### Build the plugin
1. Right click on the project > Export... > General > JAR file
2. The plugin.jar file must be placed in the server's subfolder  "/plugins"

### Initial start of the server
1. It is best to copy the server file into a folder
2. Create a "run.bat" file in it.
```bat
@echo off
title Spigot Server Console
java -Xmx1G -jar spigot-1.13.1.jar
PAUSE
```
3. start ```run.bat```
4. edit ```eula.txt```, set eula=true
5. start ```run.bat```
6. enter stop
7. edit ```server.properties```
```
gamemode=2
```
8. Copy the HolographicDisplays plugin into the "/plugins" folder
9. Copy the RaidBosses plugin into the "/plugins" folder
10. start ```run.bat```

NOTE: Because of the **map** and plugin **config file**, ask the plugin creator if he is kind enough to make it available to you ;-)

## Other helpful plugins
- FastAsyncWorldEdit
- armorstandeditor
