<div align="center">
  <img src="images/logo.png" alt="NeoLogin Logo" width="200"/>
  
  # NeoLogin
  
  **新一代 Minecraft 登录验证插件**
  
  [![SpigotMC](https://img.shields.io/badge/SpigotMC-NeoLogin-orange?style=flat-square)](https://www.spigotmc.org/resources/125813/)
  [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg?style=flat-square)](LICENSE)
  [![Java](https://img.shields.io/badge/Java-21+-brightgreen?style=flat-square)](https://adoptium.net/)
  [![Minecraft](https://img.shields.io/badge/Minecraft-1.21.8+-green?style=flat-square)](https://minecraft.net/)
  
  **简体中文** | [English](README.md)
  
</div>

---

## 📖 关于

**NeoLogin** 是一款现代化、功能丰富的 Minecraft 服务器玩家登录验证插件。它是 [blbiLogin](https://github.com/EggFine/blbiLogin) 的完全重写版本和下一代进化版，从零开始设计，拥有现代化架构、更好的性能和更多功能。

> ⚠️ **注意**：NeoLogin 正在积极开发中。对于生产服务器，建议在 NeoLogin 发布稳定版本之前使用 [blbiLogin](https://github.com/EggFine/blbiLogin)。

---

## ✨ 功能特性

### 🔒 安全认证系统
- **BCrypt 密码哈希** - 行业标准的密码加密方式
- **会话管理** - 自动追踪玩家登录状态
- **IP 记录** - 追踪登录尝试和位置
- **密码验证** - 可配置的最小/最大密码长度

### 🗄️ 多数据库支持
| 数据库 | 说明 | 推荐场景 |
|--------|------|----------|
| **SQLite** | 轻量级本地存储 | 小型服务器（默认） |
| **MySQL** | 高性能远程数据库 | 中大型服务器 |
| **MariaDB** | MySQL 兼容数据库 | 企业环境 |
| **PostgreSQL** | 高级企业级数据库 | 大规模部署 |

### 🎮 基岩版支持
- **Floodgate 集成** - 原生支持 Geyser/Floodgate
- **自动登录选项** - 基于 UUID、名称前缀或 Floodgate API
- **表单 UI** - 为基岩版玩家提供美观的原生表单
- **无缝体验** - 基岩版玩家享有与 Java 版相同的功能

### 🎁 注册奖励系统
- **物品奖励** - 首次注册时发放物品
- **经验奖励** - 给予新玩家经验值
- **命令执行** - 运行自定义命令（玩家或控制台）
- **完全可配置** - 可单独启用/禁用各类奖励

### 🚀 高级功能
- **自动传送系统** - 玩家加入/死亡时传送到出生点
- **返回位置** - 登录后传送回原始位置
- **粒子效果** - 为未注册玩家显示视觉提示
- **飞行状态保留** - 登录后恢复飞行权限
- **Folia 支持** - 原生兼容 Folia 服务器

### 🛡️ 登录前限制
为未注册/未登录玩家提供全面保护：

| 限制项 | 说明 |
|--------|------|
| `move` | 禁止玩家移动 |
| `blockPlace` | 禁止放置方块 |
| `blockBreak` | 禁止破坏方块 |
| `blockInteract` | 禁止与方块交互 |
| `chat` | 禁止发送聊天消息 |
| `command` | 禁止使用命令（支持白名单） |
| `itemUse` | 禁止使用物品 |
| `damage` | 禁止受到伤害 |
| `attack` | 禁止攻击实体 |

### 🌍 多语言支持
- **内置语言**：英语 (en_US)、简体中文 (zh_CN)
- **自定义语言**：可添加自己的语言文件
- **消息自定义**：可自定义每条消息

---

## 📊 NeoLogin 与 blbiLogin 对比

| 功能 | NeoLogin | blbiLogin |
|------|:--------:|:---------:|
| **数据库支持** | SQLite、MySQL、MariaDB、PostgreSQL | 仅 SQLite |
| **注册奖励** | ✅ 物品、经验、命令 | ❌ |
| **密码长度验证** | ✅ 最小/最大可配置 | ❌ |
| **确认密码选项** | ✅ 可配置 | 仅单密码 |
| **粒子类型** | ✅ 可配置 | 硬编码 |
| **死亡传送** | ✅ 支持 | ❌ |
| **管理员重置密码** | ✅ 支持控制台 | 仅玩家自己 |
| **限制粒度** | ✅ 细粒度（10+ 选项） | 基础（5 选项） |
| **飞行状态保留** | ✅ | ✅ |
| **基岩版表单** | ✅ | ✅ |
| **Folia 支持** | ✅ | ✅ |
| **代码架构** | 现代 OOP、管理器模式 | 传统结构 |
| **异步数据库操作** | ✅ 连接池 | 基础异步 |

### 为什么升级到 NeoLogin？

1. **更好的数据库支持** - 使用 MySQL/PostgreSQL 实现多服务器同步
2. **增强的安全性** - 更好的密码验证和管理
3. **更多自定义选项** - 可调整插件的各个方面
4. **现代代码库** - 更易维护和扩展
5. **活跃开发** - 定期更新和新功能

---

## 📋 系统要求

| 要求 | 版本 |
|------|------|
| **Minecraft** | 1.21.1+ |
| **服务器核心** | Spigot / Paper / Folia |
| **Java** | 21+ |
| **依赖插件** | [NeoLibrary](https://github.com/EggFine/NeoLibrary) |

---

## 📦 安装

1. **下载** 最新版本：[SpigotMC](https://www.spigotmc.org/resources/125813/) 或 [GitHub Releases](../../releases)

2. **安装依赖**：
   ```
   plugins/
   ├── NeoLibrary.jar    (必需)
   └── NeoLogin.jar
   ```

3. **启动服务器** - 配置文件将自动生成

4. **配置** - 编辑 `plugins/NeoLogin/config.yml` 以满足您的需求

---

## 🎮 命令

### 玩家命令

| 命令 | 别名 | 说明 |
|------|------|------|
| `/register <密码> [确认密码]` | `/reg` | 注册新账号 |
| `/login <密码>` | `/l` | 登录账号 |
| `/resetpassword <旧密码> <新密码>` | `/rp` | 修改密码 |

### 管理员命令

| 命令 | 说明 | 权限 |
|------|------|------|
| `/neologin reload` | 重载配置文件 | `neologin.admin` |
| `/neologin savelocation` | 保存出生点位置 | `neologin.admin` |
| `/resetpassword <玩家> <新密码>` | 重置玩家密码 | `neologin.admin` |

---

## ⚙️ 配置

### 数据库设置

<details>
<summary><b>SQLite（默认）</b></summary>

```yaml
database:
  type: "sqlite"
  file: "playerData.db"
```
</details>

<details>
<summary><b>MySQL / MariaDB</b></summary>

```yaml
database:
  type: "mysql"  # 或 "mariadb"
  host: "localhost"
  port: 3306
  database: "neoLogin"
  username: "root"
  password: "your_password"
```
</details>

<details>
<summary><b>PostgreSQL</b></summary>

```yaml
database:
  type: "postgresql"
  host: "localhost"
  port: 5432
  database: "neoLogin"
  username: "postgres"
  password: "your_password"
```
</details>

### 注册奖励

```yaml
register:
  reward:
    enable: true
    items:
      - "BREAD:16"
      - "IRON_SWORD:1"
    experience: 100
    consoleCommands:
      - "give %player% minecraft:cookie 5"
```

### 基岩版配置

```yaml
bedrock:
  enabled: true
  autologin:
    floodgate: true    # 通过 Floodgate API 自动登录
    uuid: false        # 通过基岩版 UUID 自动登录
    prefix: false      # 通过名称前缀自动登录
    prefix_value: "*"
  forms: true          # 为基岩版启用表单 UI
```

---

## 🔑 权限

| 权限节点 | 说明 | 默认 |
|----------|------|------|
| `neologin.admin` | 管理员命令权限 | OP |

---

## 🛠️ 从源码构建

```bash
# 克隆仓库
git clone https://github.com/EggFine/NeoLogin.git
cd NeoLogin

# 使用 Gradle 构建
./gradlew build

# 输出：build/libs/NeoLogin-*.jar
```

---

## 🤝 参与贡献

欢迎任何形式的贡献！请随时提交 Pull Request。

1. Fork 本仓库
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

---

## 📜 许可证

本项目基于 [Apache License 2.0](LICENSE) 开源。

---

## 🙏 致谢

- [blbiLogin](https://github.com/EggFine/blbiLogin) - 启发 NeoLogin 的原始项目
- 所有贡献者和测试人员
- Minecraft 服务器社区

---

## 📞 支持

- **问题反馈**：[GitHub Issues](../../issues)
- **SpigotMC**：[插件页面](https://www.spigotmc.org/resources/125813/)

---

<div align="center">
  
**⭐ 如果 NeoLogin 对您的服务器有帮助，请给我们一个星标！**

Made with ❤️ by [EggFine](https://github.com/EggFine)

</div>

