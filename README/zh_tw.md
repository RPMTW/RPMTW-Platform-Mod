<img src="https://raw.githubusercontent.com/RPMTW/RPMTW-Data/main/logo/rpmtw-platform/rpmtw-platform-logo.png" align="left" width="180px"/>

# RPMTW 平台

[![可用於](https://img.shields.io/badge/dynamic/json?style=for-the-badge&color=34aa2f&query=$[:]&url=https://gist.githubusercontent.com/notlin4/b3a7c0a6530d7d6ed19e063d903952bc/raw&label=可用於)](https://modrinth.com/mod/ZukQzaRP/versions)
[![發布版](https://img.shields.io/github/v/release/RPMTW/RPMTW-Platform-Mod.svg?style=for-the-badge&include_prereleases&sort=semver&label=發布版)](../../../releases)
[![授權條款](https://img.shields.io/github/license/RPMTW/RPMTW-Platform-Mod.svg?style=for-the-badge&label=授權條款)](../LICENSE)

[![Modrinth 下載數](https://img.shields.io/modrinth/dt/ZukQzaRP?&style=for-the-badge&logo=modrinth&label=Modrinth%20下載數)](https://modrinth.com/mod/ZukQzaRP/versions)
[![CuserForge 下載數](https://img.shields.io/badge/dynamic/json?color=f16436&style=for-the-badge&query=downloadCount&url=https://www.fibermc.com/api/v1.0/ForeignMods/461500&logo=CurseForge&label=CurseForge%20下載數)](https://www.curseforge.com/minecraft/mc-mods/461500/files)
<p></p>
<p>&nbsp;</p>

#### 🌐 說明檔案語言

- [English](../README.md)
- 正體中文（目前語言）
- [简体中文](zh_cn.md)

## 📚 介紹

整合了 RPMTW 各項服務的模組，涵蓋翻譯、百科、宇宙通訊、中文輸入最佳化等功能，讓 Minecraft 中文玩家獲得更好的遊戲體驗。

## 🎨 特色功能

- ### 宇宙通訊

在任何版本的 Minecraft 中或遊戲外聊天交流，每個玩家就像在同個世界中遊玩！

- ### 設定選單

內建精緻的設定選單，任何功能都可供您自由調整。

- ### 原文顯示

想知道物品譯名的原文嗎？用這功能就對了！  
~~甚至還可以順便學習英文~~

- ### 翻譯資源包

自動載入最新的翻譯資源包，由許多熱心的社群參與者共同翻譯而成。

- ### 機器翻譯

遇到沒人工翻譯的內容嗎？全部交給 AI 吧！不再被語言隔閡導致無法遊玩模組。

- ### 開啟 Crowdin 頁面

想要協助翻譯手中的物品或者您注視的方塊或實體嗎？  
使用這功能即可，請記得到設定選單中修改本功能的快捷鍵  

## ⚙ 開發者

### 編譯

```shell
./gradlew build
```

檔案位置

- Fabric `fabric/build/libs/rpmtw-platform-mod-*.jar`
- Forge  `forge/build/libs/rpmtw-platform-mod-*.jar`
- Quilt `quilt/build/libs/rpmtw-platform-mod-*.jar`

### ️ 使用的技術

- [Kotlin](https://kotlinlang.org)
- Java
- JVM
- [Architectury](https://github.com/architectury)
- [Cloth Config](https://github.com/shedaniel/cloth-config)
- [Socket.IO](https://github.com/socketio/socket.io-client-java)

## 🎓 授權條款

[GNU 通用公眾授權條款 3.0 版（GPL3）](https://www.gnu.org/licenses/gpl-3.0.html)
