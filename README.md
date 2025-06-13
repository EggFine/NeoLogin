# NeoLogin

## NeoLogin 是下一代 blbiLogin

**NeoLogin** 是基于 Minecraft 1.21.1 Spigot/Paper 核心开发的现代化玩家登录插件解决方案，继承并改进了 [blbiLogin](https://github.com/EggFine/blbiLogin) 的核心功能。

```
⚠️ **警告**：最新的 Release 仍处于不可用阶段，仅可进行开发测试，**不可用于生产环境**，您可以前往下载 [blbiLogin](https://github.com/EggFine/blbiLogin) 体验完善的，稳定的登录插件
```


---

## ✨ 特性

### 🔒 安全登录系统
- **密码保护**：玩家必须注册并登录才能游戏
- **密码加密**：使用 BCrypt 算法保护玩家密码
- **会话管理**：自动管理玩家登录状态

### 🌐 多平台支持
- **Java 版**：完全支持 Java 版 Minecraft 客户端
- **基岩版**：支持基岩版玩家（通过 Floodgate）
- **跨版本**：兼容 Minecraft 1.21.1+

### 🗄️ 多数据库支持
- **SQLite**：轻量级本地数据库（默认）
- **MySQL**：高性能远程数据库
- **MariaDB**：MySQL 兼容数据库
- **PostgreSQL**：企业级数据库

### 🌍 国际化支持
- **中文简体**（zh_CN）
- **英语**（en_US）
- 支持自定义语言文件

### ⚡ 高性能架构
- **Folia 支持**：原生支持 Folia 服务器
- **Java 21**：基于最新 Java 21 构建
- **异步处理**：数据库操作异步执行，不阻塞主线程

---

## 📋 系统要求

| 项目               | 要求                   |
| ------------------ | ---------------------- |
| **Minecraft 版本** | 1.21.1+                |
| **服务器核心**     | Spigot / Paper / Folia |
| **Java 版本**      | Java 21+               |
| **依赖插件**       | NeoLibrary             |

---

## 📦 安装

1. **下载插件**：
   - 从 [Releases](../../releases) 下载最新版本
   - 确保同时下载 `NeoLibrary` 依赖

2. **安装插件**：
   ```bash
   # 将插件文件放入服务器 plugins 目录
   plugins/
   ├── NeoLibrary.jar
   └── NeoLogin.jar
   ```

3. **启动服务器**：
   - 启动服务器，插件将自动生成配置文件
   - 配置文件位置：`plugins/NeoLogin/config.yml`

---

## ⚙️ 配置

### 基本配置

```yaml
# 插件消息前缀
prefix: "§8[§fNeo§bLogin§8] §f"

# 语言设置
language: zh_CN

# 数据库配置
database:
  type: "sqlite"  # 支持：sqlite, mysql, mariadb, postgresql
  filePath: "plugins/NeoLogin/playerData.db"
```

### 数据库配置

#### SQLite（推荐小型服务器）
```yaml
database:
  type: "sqlite"
  filePath: "plugins/NeoLogin/playerData.db"
```

#### MySQL/MariaDB（推荐大型服务器）
```yaml
database:
  type: "mysql"
  host: "localhost"
  port: 3306
  databaseName: "neoLogin"
  username: "root"
  password: "your_password"
```

### 玩家限制配置

```yaml
notLoggedInPlayerLimit:
  enabled: true
  type:
    move: true          # 禁止移动
    blockPlace: true    # 禁止放置方块
    blockBreak: true    # 禁止破坏方块
    chat: true          # 禁止聊天
    command: true       # 禁止使用命令
    commandWhitelist:   # 命令白名单
      - "/login"
      - "/register"
```

---

## 🎮 命令

### 玩家命令

| 命令               | 别名   | 描述     | 权限 |
| ------------------ | ------ | -------- | ---- |
| `/register <密码>` | `/reg` | 注册账号 | 无   |
| `/login <密码>`    | `/l`   | 登录账号 | 无   |

### 管理员命令

| 命令                     | 别名               | 描述                 | 权限             |
| ------------------------ | ------------------ | -------------------- | ---------------- |
| `/neologin reload`       | `/nl reload`       | 重载配置文件         | `neologin.admin` |
| `/neologin savelocation` | `/nl savelocation` | 保存当前位置为传送点 | `neologin.admin` |

---

## 🔑 权限

| 权限节点         | 描述       | 默认 |
| ---------------- | ---------- | ---- |
| `neologin.admin` | 管理员权限 | OP   |

---

## 🛠️ 开发

### 构建项目

```bash
# 克隆项目
git clone https://github.com/your-username/NeoLogin.git
cd NeoLogin

# 构建项目
./gradlew build

# 运行测试服务器
./gradlew runServer
```

### 项目结构

```
NeoLogin/
├── src/main/java/           # Java 源代码
├── src/main/resources/      # 资源文件
│   ├── config.yml          # 配置文件
│   ├── plugin.yml          # 插件信息
│   └── languages/          # 语言文件
├── build.gradle            # Gradle 构建文件
└── README.md              # 项目说明
```

---

## 🤝 贡献

我们欢迎任何形式的贡献！

1. **Fork** 本项目
2. **创建** 功能分支 (`git checkout -b feature/AmazingFeature`)
3. **提交** 更改 (`git commit -m 'Add some AmazingFeature'`)
4. **推送** 到分支 (`git push origin feature/AmazingFeature`)
5. **创建** Pull Request

---

## 📜 许可证

本项目使用 [Apache-2.0](LICENSE) 许可证开源。

---

## 🙏 致谢

- 感谢 [blbiLogin](https://github.com/EggFine/blbiLogin) 项目的其他贡献者对我的支持
- 感谢所有贡献者和用户的支持

---

## 📞 支持

- **GitHub Issues**：[提交问题](../../issues)
- **原项目参考**：[blbiLogin](https://github.com/EggFine/blbiLogin)

---

<div align="center">

**⭐ 如果这个项目对你有帮助，请给我们一个星标！**

Made with ❤️ by [EggFine](https://github.com/EggFine)

</div>