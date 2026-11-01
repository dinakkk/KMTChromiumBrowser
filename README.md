# KMT Chromium Browser (中華民國國民革命主題瀏覽器)

[![Android CI](https://img.shields.io/badge/Build-GitHub_Actions-2088FF?logo=github-actions&logoColor=white)](#)
[![API](https://img.shields.io/badge/API-24%2B%20(Android%207.0%2B)-3DDC84?logo=android&logoColor=white)](#)
[![Target SDK](https://img.shields.io/badge/Target_SDK-34%20(Android%2014)-000095)](#)
[![Engine](https://img.shields.io/badge/Engine-Chromium%20Blink%20%2F%20V8-4285F4?logo=google-chrome&logoColor=white)](#)
[![Java](https://img.shields.io/badge/JDK-17-ED8B00?logo=openjdk&logoColor=white)](#)

**KMT Chromium Browser** 是一款基於 Android 系統級 Chromium (`Android System WebView` / `Trichrome`) 渲染引擎構建的輕量級客製化瀏覽器。本專案將現代極簡的 Chromium 瀏覽器介面與「青天白日、天下為公」視覺主題深度融合，內建離線高清向量資源與專屬站內檢索接口，提供極速、低記憶體佔用的網頁瀏覽體驗。

---

## ✨ 核心特性 (Features)

* **標準青天白日動態旗面背景**：
  * 內嵌標準 `3:2` 比例之《中華民國海軍艦艏旗 / 中國國民黨黨旗》（青天白日旗）純向量 SVG 圖形。
  * 採用獨立圖層封裝十二道白日芒，透過硬體加速 CSS Keyframes 實現圍繞圓心順時針勻速自轉之動態視覺效果。
* **國父孫中山先生紀念肖像與專屬圖示**：
  * 本地封裝高清彩色孫中山先生肖像，作為首頁核心徽章、搜尋欄左側引擎圖示以及系統桌面啟動器圖示（`ic_launcher`），支援無網路環境下瞬間載入。
* **「天下為公」書法牌匾與三民主義副標**：
  * 首頁配置金邊深藍漸層之「天下為公」繁體書法牌匾，並輔以紅底白字「民族 ・ 民權 ・ 民生」徽章式副標題。
* **中國國民黨官網直連檢索**：
  * 首頁搜尋框與頂部 Chromium 風格多功能網址列（Omnibox）預設對接中國國民黨全球資訊網檢索接口（`https://www.kmt.org.tw/search?q=`），支援關鍵字自動 URL 編碼與智慧網址判斷。
* **沉浸式系統狀態列與導航列同步**：
  * 透過原生 Window API 將 Android 狀態列（Status Bar）與底部導航列（Navigation Bar）統一渲染為標準海軍深藍（`#000095`）。

---

## 🛠️ 技術規格與環境支援 (Specifications)

### 1. Android SDK 支援範圍
| 屬性項目 | 配置數值 | 對應系統版本與說明 |
| :--- | :--- | :--- |
| **Application ID** | `com.chromium.kmt.browser` | 獨立套件名稱，可與其他瀏覽器共存 |
| **Min SDK Version** | `API 24` | 最低支援 **Android 7.0 (Nougat)** 及以上裝置 |
| **Target SDK Version** | `API 34` | 完整適配 **Android 14 (Upside Down Cake)** 權限與行為變更 |
| **Compile SDK Version** | `API 34` | 使用 Android 14 SDK 進行編譯 |
| **Java 語言級別** | `Java 17` | 採用 OpenJDK 17 (`VERSION_17`) 語法與字節碼標準 |
| **Gradle 與 AGP 版本** | `Gradle 8.7` / `AGP 8.3.2` | 支援現代化 AndroidX 與非傳遞性 R 類別最佳化 |

### 2. Chromium 內核版本與架構解析
本瀏覽器採用 Android 現代化的 **System WebView / Trichrome 共享內核架構**，而非將數百 MB 的靜態舊版 Chromium 二進制文件硬編碼打包於 APK 中：

* **渲染與腳本引擎**：底層直接調用系統內建的 **Chromium Blink 排版引擎** 與 **V8 JavaScript 引擎**。
* **內核版本動態演進（Evergreen Kernel）**：
  * 在 Android 7.0 ~ 9.0 裝置上，由系統中的 Chrome 或獨立的 `com.google.android.webview` 提供 Chromium 內核支援。
  * 在 Android 10 (API 29) 及以上裝置上，基於 **Trichrome 架構**與系統 Google Play 服務連動。當使用者透過系統更新升級 `Android System WebView` 時，本瀏覽器的 Chromium 內核版本將同步升級至最新穩定版（例如 **Chromium 120 ~ 130+**），自動獲得最新的 HTML5、CSS3 Grid/Flexbox、ES6+、WebGL 2.0 支援以及零日安全漏洞修補。
* **CPU 指令集架構相容性（ABI Support）**：
  * 由於應用層採用純 Java/AndroidX 編寫並透過系統框架層橋接底層 Native Chromium 庫，編譯產出的 APK 為 **全架構通用包（Universal APK）**，原生完美運行於：
    * `arm64-v8a`（主流 64 位元手機與平板）
    * `armeabi-v7a`（舊款 32 位元 ARM 裝置）
    * `x86_64` / `x86`（WSA、Android Studio 模擬器及 x86 平板設備）

---

## 📂 專案目錄結構 (Directory Structure)

```text
ChromiumKmtBrowser/
├── .github/workflows/
│   └── build.yml                  # GitHub Actions 雲端自動化編譯工作流
├── app/
│   ├── build.gradle               # App 模組建置腳本與 AndroidX 依賴配置
│   ├── proguard-rules.pro         # R8 / ProGuard 程式碼混淆保護規則
│   └── src/main/
│       ├── AndroidManifest.xml    # 應用資訊、網路權限與主 Activity 宣告
│       ├── assets/
│       │   ├── home.html          # 內建動態青天白日旗與搜尋首頁源碼
│       │   ├── flag.svg           # 標準 3:2 比例之動態旋轉青天白日向量圖
│       │   └── portrait.jpg       # 離線封裝之孫中山先生彩色肖像
│       ├── java/com/chromium/kmt/browser/
│       │   └── MainActivity.java  # 瀏覽器核心控制器、Omnibox 網址列與 WebView 容器
│       └── res/
│           └── mipmap-xxxhdpi/
│               └── ic_launcher.png# 桌面啟動器高清圖示
├── build.gradle                   # 根目錄 Gradle 插件版本宣告
├── gradle.properties              # JVM 記憶體參數與 UTF-8 編碼設定
└── settings.gradle                # Maven 倉庫源與模組引入配置
```

---

## 🚀 編譯與建置指南 (Build Instructions)

### 方式一：使用 GitHub Actions 雲端自動編譯（免配置本地環境）

1. 在 GitHub 上建立一個新的儲存庫（例如 `NewKMTViaBrowser`）。
2. 在專案解壓後的根目錄開啟終端機（Terminal / PowerShell），執行以下命令將源碼推送至 GitHub：
   ```bash
   git init
   git add .
   git commit -m "Initial commit: KMT Chromium Browser"
   git branch -M main
   git remote add origin https://github.com/你的用戶名/你的倉庫名.git
   git push -u origin main --force
   ```
3. 進入 GitHub 儲存庫頂部的 **Actions** 標籤頁，等待 `Build Custom KMT Chromium Browser APK` 工作流執行完畢（顯示綠色勾號）。
4. 點擊該次建置記錄，在頁面最下方的 **Artifacts** 區塊下載 `Chromium-KMT-Browser-APK` 壓縮包，解壓後即可獲得 `app-debug.apk` 安裝檔。

### 方式二：使用本地命令列或 Android Studio 編譯

1. **環境準備**：確保本機已安裝 **JDK 17** 與 **Android SDK (Platform 34)**。
2. **執行編譯**：在專案根目錄開啟終端機執行：
   ```bash
   gradle assembleDebug
   ```
3. **取得安裝包**：編譯完成後，APK 檔案將生成於以下路徑：
   ```text
   app/build/outputs/apk/debug/app-debug.apk
   ```

---

## ⚙️ 客製化調整說明 (Customization)

* **調整背景白日旋轉速度**：
  打開 `app/src/main/assets/home.html`，找到 `#white-sun-group` 樣式中的 `animation: rotateWhiteSun 16s linear infinite;`，將 `16s` 修改為期望的單圈秒數（數值越小轉速越快）。
* **更換預設搜尋引擎**：
  在 `app/src/main/assets/home.html` 與 `MainActivity.java` 中搜尋 `https://www.kmt.org.tw/search?q=`，將其替換為目標檢索服務的 Query URL 即可。