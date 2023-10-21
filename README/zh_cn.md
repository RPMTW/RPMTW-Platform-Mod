<img src="https://raw.githubusercontent.com/RPMTW/RPMTW-Data/main/logo/rpmtw-platform/rpmtw-platform-logo.png" align="left" width="180px"/>

# RPMTW 平台

[![适用于](https://img.shields.io/badge/dynamic/json?style=for-the-badge&color=34aa2f&query=$[:]&url=https://gist.githubusercontent.com/notlin4/b3a7c0a6530d7d6ed19e063d903952bc/raw&label=适用于)](https://modrinth.com/mod/ZukQzaRP/versions)
[![发布版](https://img.shields.io/github/v/release/RPMTW/RPMTW-Platform-Mod.svg?style=for-the-badge&include_prereleases&sort=semver&label=发布版)](../../../releases)
[![许可证](https://img.shields.io/github/license/RPMTW/RPMTW-Platform-Mod.svg?style=for-the-badge&label=许可证)](../LICENSE)

[![Modrinth 下载数](https://img.shields.io/modrinth/dt/ZukQzaRP?&style=for-the-badge&logo=modrinth&label=Modrinth%20下载数)](https://modrinth.com/mod/ZukQzaRP/versions)
[![CuserForge 下载数](https://img.shields.io/badge/dynamic/json?color=f16436&style=for-the-badge&query=downloadCount&url=https://www.fibermc.com/api/v1.0/ForeignMods/461500&logo=CurseForge&label=CurseForge%20下载数)](https://www.curseforge.com/minecraft/mc-mods/rpmtw-platform/files)
<p>&nbsp;</p>
<p>&nbsp;</p>

#### 🌐 自叙文件语言

- [English](../../README.md)
- [正體中文](zh_tw.md)
- 简体中文（当前语言）

## 📚 介绍

整合了 RPMTW 各项服务的 MOD，涵盖翻译、百科、宇宙通信、中文输入优化等功能，让 Minecraft 中文玩家获得更好的游戏体验。

## 🎨 特色功能

- ### 宇宙通信

在任何版本的 Minecraft 中或游戏外聊天交流，每个玩家就像在同个世界中游玩！

- ### 配置菜单

内置精致的配置菜单，任何功能都可供您自由调整。

- ### 原文显示

想知道物品译名的原文吗？用这功能就对了！  
~~甚至还可以顺便学习英文~~

- ### 翻译资源包

自动加载最新的翻译资源包，由许多热心的社区参与者共同翻译而成。

- ### 机器翻译

遇到没人工翻译的内容吗？全部交给 AI 吧！不再被语言隔阂导致无法游玩 MOD。

- ### 开启 Crowdin 页面

想要协助翻译手中的物品或者您注视的方块或实体吗？  
使用这项功能即可，请记得到设置菜单中修改本功能的快捷键

## ⚙ 开发者

### 编译

```shell
./gradlew build
```

文件位置

- Fabric `fabric/build/libs/rpmtw-platform-mod-*.jar`
- Forge  `forge/build/libs/rpmtw-platform-mod-*.jar`
- Quilt `quilt/build/libs/rpmtw-platform-mod-*.jar`

### ️ 使用的技术

- [Kotlin](https://kotlinlang.org/)
- Java
- JVM
- [Architectury](https://github.com/architectury)
- [Cloth Config](https://github.com/shedaniel/cloth-config)
- [Socket.IO](https://github.com/socketio/socket.io-client-java)

## 🎓 许可证

[GNU 通用公共许可证 3.0 版（GPL3）](https://www.gnu.org/licenses/gpl-3.0.html)
