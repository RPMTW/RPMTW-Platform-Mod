<img src="https://raw.githubusercontent.com/RPMTW/RPMTW-Data/main/logo/rpmtw-platform/rpmtw-platform-logo.png" align="left" width="180px"/>

# RPMTW Platform Mod

[![Available For](https://img.shields.io/badge/dynamic/json?style=for-the-badge&color=34aa2f&query=$[:]&url=https://gist.githubusercontent.com/notlin4/b3a7c0a6530d7d6ed19e063d903952bc/raw&label=Available%20For)](https://modrinth.com/mod/ZukQzaRP/versions)
[![Release](https://img.shields.io/github/v/release/RPMTW/RPMTW-Platform-Mod.svg?style=for-the-badge&include_prereleases&sort=semver)](../../releases)
[![LICENSE](https://img.shields.io/github/license/RPMTW/RPMTW-Platform-Mod.svg?style=for-the-badge)](LICENSE)

[![Modrinth Downloads](https://img.shields.io/modrinth/dt/ZukQzaRP?&style=for-the-badge&logo=modrinth&label=Modrinth%20Downloads)](https://modrinth.com/mod/ZukQzaRP/versions)
[![CuserForge Downloads](https://img.shields.io/badge/dynamic/json?color=f16436&style=for-the-badge&query=downloadCount&url=https://www.fibermc.com/api/v1.0/ForeignMods/461500&logo=CurseForge&label=CurseForge%20Downloads)](https://www.curseforge.com/minecraft/mc-mods/461500/files)
<p>&nbsp;</p> 

#### 🌐 README Languages

- English (Current Language)
- [正體中文](README/zh_tw.md)
- [简体中文](README/zh_cn.md)

## 📚 Introduction

The mod integrated with RPMTW services covers translation, wiki, universe chat, Chinese input optimization, and other
features for Minecraft players to get a better gaming experience.

## 🎨 Features

- ### Universe chat

Chat in any version of Minecraft or outside the game, every player feels like they are in the same world!

- ### Settings menu

The built-in sophisticated settings menu allows you to adjust any function freely.

- ### Original language display

Want to know the original language of an item? Use this function!  
~~You can even learn English at the same time!~~

- ### Translate resource pack

The latest translation resource pack is automatically loaded and translated by many enthusiastic community contributors.

- ### Machine translation

Don't have a human translator? Leave it all to the AI! No more language barrier that prevents you from playing the mods.

- ### Open Crowdin page

Want to help translate an item in your hand or a block or entity you are looking at?  
Just use this function, and remember to change the shortcut keys for this function in the settings menu

## ⚙️ Development

### Build

```shell
./gradlew build
```

File location

- Fabric `fabric/build/libs/rpmtw-platform-mod-*.jar`
- Forge  `forge/build/libs/rpmtw-platform-mod-*.jar`
- Quilt `quilt/build/libs/rpmtw-platform-mod-*.jar`

### ️ Technologies

- [Kotlin](https://kotlinlang.org/)
- Java
- JVM
- [Architectury](https://github.com/architectury)
- [Cloth Config](https://github.com/shedaniel/cloth-config)
- [Socket.IO](https://github.com/socketio/socket.io-client-java)

## 🎓 License

[GNU General Public License v3.0 (GPL3)](https://www.gnu.org/licenses/gpl-3.0.html)
