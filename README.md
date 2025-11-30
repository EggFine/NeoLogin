<div align="center">
  <img src="images/logo.png" alt="NeoLogin Logo" width="200"/>
  
  # NeoLogin
  
  **The Next Generation Minecraft Authentication Plugin**
  
  [![SpigotMC](https://img.shields.io/badge/SpigotMC-NeoLogin-orange?style=flat-square)](https://www.spigotmc.org/resources/125813/)
  [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg?style=flat-square)](LICENSE)
  [![Java](https://img.shields.io/badge/Java-21+-brightgreen?style=flat-square)](https://adoptium.net/)
  [![Minecraft](https://img.shields.io/badge/Minecraft-1.21.1+-green?style=flat-square)](https://minecraft.net/)
  
  [简体中文](README_CS.md) | **English**
  
</div>

---

## 📖 About

**NeoLogin** is a modern, feature-rich player authentication plugin for Minecraft servers. It is the complete rewrite and next-generation evolution of [blbiLogin](https://github.com/EggFine/blbiLogin), designed from the ground up with modern architecture, better performance, and more features.

> ⚠️ **Note**: NeoLogin is under active development. For production servers, consider using [blbiLogin](https://github.com/EggFine/blbiLogin) until NeoLogin reaches stable release.

---

## ✨ Features

### 🔒 Secure Authentication System
- **BCrypt Password Hashing** - Industry-standard password encryption
- **Session Management** - Automatic login state tracking
- **IP Logging** - Track login attempts and locations
- **Password Validation** - Configurable min/max password length

### 🗄️ Multi-Database Support
| Database | Description | Recommended For |
|----------|-------------|-----------------|
| **SQLite** | Lightweight local storage | Small servers (default) |
| **MySQL** | High-performance remote DB | Medium to large servers |
| **MariaDB** | MySQL-compatible | Enterprise environments |
| **PostgreSQL** | Advanced enterprise DB | Large-scale deployments |

### 🎮 Bedrock Edition Support
- **Floodgate Integration** - Native support for Geyser/Floodgate
- **Auto-Login Options** - UUID-based, prefix-based, or Floodgate API
- **Form UI** - Beautiful native forms for Bedrock players
- **Seamless Experience** - Bedrock players get the same features as Java

### 🎁 Registration Reward System
- **Item Rewards** - Give items on first registration
- **Experience Rewards** - Grant XP to new players
- **Command Execution** - Run custom commands (player or console)
- **Fully Configurable** - Enable/disable individual reward types

### 🚀 Advanced Features
- **Auto-Teleport System** - Teleport players to spawn on join/death
- **Return Location** - Teleport back to original location after login
- **Particle Effects** - Visual indicators for unregistered players
- **Flight State Preservation** - Restore flight permissions after login
- **Folia Support** - Native compatibility with Folia servers

### 🛡️ Pre-Login Restrictions
Comprehensive protection for unregistered/unlogged players:

| Restriction | Description |
|-------------|-------------|
| `move` | Prevent player movement |
| `blockPlace` | Prevent block placement |
| `blockBreak` | Prevent block breaking |
| `blockInteract` | Prevent block interaction |
| `chat` | Prevent chat messages |
| `command` | Prevent command usage (with whitelist) |
| `itemUse` | Prevent item usage |
| `damage` | Prevent taking damage |
| `attack` | Prevent attacking entities |

### 🌍 Internationalization
- **Built-in Languages**: English (en_US), Simplified Chinese (zh_CN)
- **Custom Languages**: Add your own language files
- **Per-Message Customization**: Customize every message

---

## 📊 NeoLogin vs blbiLogin Comparison

| Feature | NeoLogin | blbiLogin |
|---------|:--------:|:---------:|
| **Database Support** | SQLite, MySQL, MariaDB, PostgreSQL | SQLite only |
| **Registration Rewards** | ✅ Items, XP, Commands | ❌ |
| **Password Length Validation** | ✅ Min/Max configurable | ❌ |
| **Confirm Password Option** | ✅ Configurable | Single password only |
| **Particle Type** | ✅ Configurable | Hardcoded |
| **Death Teleport** | ✅ Supported | ❌ |
| **Admin Password Reset** | ✅ Console support | Player only |
| **Restriction Granularity** | ✅ Fine-grained (10+ options) | Basic (5 options) |
| **Flight State Preservation** | ✅ | ✅ |
| **Bedrock Forms** | ✅ | ✅ |
| **Folia Support** | ✅ | ✅ |
| **Code Architecture** | Modern OOP, Managers | Legacy structure |
| **Async Database Operations** | ✅ Connection pooling | Basic async |

### Why Upgrade to NeoLogin?

1. **Better Database Support** - Use MySQL/PostgreSQL for multi-server setups
2. **Enhanced Security** - Better password validation and management
3. **More Customization** - Fine-tune every aspect of the plugin
4. **Modern Codebase** - Easier to maintain and extend
5. **Active Development** - Regular updates and new features

---

## 📋 Requirements

| Requirement | Version |
|-------------|---------|
| **Minecraft** | 1.21.1+ |
| **Server Core** | Spigot / Paper / Folia |
| **Java** | 21+ |
| **Dependency** | [NeoLibrary](https://github.com/EggFine/NeoLibrary) |

---

## 📦 Installation

1. **Download** the latest release from [SpigotMC](https://www.spigotmc.org/resources/125813/) or [GitHub Releases](../../releases)

2. **Install Dependencies**:
   ```
   plugins/
   ├── NeoLibrary.jar    (required)
   └── NeoLogin.jar
   ```

3. **Start Server** - Configuration files will be generated automatically

4. **Configure** - Edit `plugins/NeoLogin/config.yml` to your needs

---

## 🎮 Commands

### Player Commands

| Command | Aliases | Description |
|---------|---------|-------------|
| `/register <password> [confirm]` | `/reg` | Register a new account |
| `/login <password>` | `/l` | Login to your account |
| `/resetpassword <old> <new>` | `/rp` | Change your password |

### Admin Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/neologin reload` | Reload configuration | `neologin.admin` |
| `/neologin savelocation` | Save spawn location | `neologin.admin` |
| `/resetpassword <player> <newpass>` | Reset player password | `neologin.admin` |

---

## ⚙️ Configuration

### Database Setup

<details>
<summary><b>SQLite (Default)</b></summary>

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
  type: "mysql"  # or "mariadb"
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

### Registration Rewards

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

### Bedrock Configuration

```yaml
bedrock:
  enabled: true
  autologin:
    floodgate: true    # Auto-login via Floodgate API
    uuid: false        # Auto-login by Bedrock UUID
    prefix: false      # Auto-login by name prefix
    prefix_value: "*"
  forms: true          # Enable form UI for Bedrock
```

---

## 🔑 Permissions

| Permission | Description | Default |
|------------|-------------|---------|
| `neologin.admin` | Access to admin commands | OP |

---

## 🛠️ Building from Source

```bash
# Clone the repository
git clone https://github.com/EggFine/NeoLogin.git
cd NeoLogin

# Build with Gradle
./gradlew build

# Output: build/libs/NeoLogin-*.jar
```

---

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📜 License

This project is licensed under the [Apache License 2.0](LICENSE).

---

## 🙏 Acknowledgements

- [blbiLogin](https://github.com/EggFine/blbiLogin) - The original project that inspired NeoLogin
- All contributors and testers
- The Minecraft server community

---

## 📞 Support

- **Issues**: [GitHub Issues](../../issues)
- **SpigotMC**: [Plugin Page](https://www.spigotmc.org/resources/125813/)

---

<div align="center">
  
**⭐ If NeoLogin helps your server, please consider giving us a star!**

Made with ❤️ by [EggFine](https://github.com/EggFine)

</div>
