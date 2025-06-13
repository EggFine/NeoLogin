# NeoLogin

<div align="center">

**语言 | Language**

[简体中文](README.md) | [English](README_en_US.md)

</div>

## NeoLogin is the Next Generation of blbiLogin

**NeoLogin** is a modern player authentication plugin solution based on Minecraft 1.21.1 Spigot/Paper core, inheriting and improving the core features of [blbiLogin](https://github.com/EggFine/blbiLogin).

> ⚠️ **Warning**: The latest Release is still in an unusable stage and can only be used for development testing. **Do not use in production environments**. You can download [blbiLogin](https://github.com/EggFine/blbiLogin) for a complete and stable login plugin experience.

---

## ✨ Features

### 🔒 Secure Login System
- **Password Protection**: Players must register and login to play
- **Password Encryption**: Protect player passwords using BCrypt algorithm
- **Session Management**: Automatically manage player login status

### 🌐 Multi-Platform Support
- **Java Edition**: Full support for Java Edition Minecraft clients
- **Bedrock Edition**: Support for Bedrock Edition players (via Floodgate)
- **Cross-Version**: Compatible with Minecraft 1.21.1+

### 🗄️ Multi-Database Support
- **SQLite**: Lightweight local database (default)
- **MySQL**: High-performance remote database
- **MariaDB**: MySQL-compatible database
- **PostgreSQL**: Enterprise-grade database

### 🌍 Internationalization Support
- **Simplified Chinese** (zh_CN)
- **English** (en_US)
- Support for custom language files

### ⚡ High-Performance Architecture
- **Folia Support**: Native support for Folia servers
- **Java 21**: Built on the latest Java 21
- **Async Processing**: Database operations executed asynchronously without blocking the main thread

---

## 📋 System Requirements

| Item                   | Requirement            |
| ---------------------- | ---------------------- |
| **Minecraft Version**  | 1.21.1+                |
| **Server Core**        | Spigot / Paper / Folia |
| **Java Version**       | Java 21+               |
| **Dependencies**       | NeoLibrary             |

---

## 📦 Installation

1. **Download Plugin**:
   - Download the latest version from [Releases](../../releases)
   - Make sure to also download the `NeoLibrary` dependency

2. **Install Plugin**:
   ```bash
   # Place plugin files in the server plugins directory
   plugins/
   ├── NeoLibrary.jar
   └── NeoLogin.jar
   ```

3. **Start Server**:
   - Start the server, and the plugin will automatically generate configuration files
   - Configuration file location: `plugins/NeoLogin/config.yml`

---

## ⚙️ Configuration

### Basic Configuration

```yaml
# Plugin message prefix
prefix: "§8[§fNeo§bLogin§8] §f"

# Language setting
language: en_US

# Database configuration
database:
  type: "sqlite"  # Supported: sqlite, mysql, mariadb, postgresql
  filePath: "plugins/NeoLogin/playerData.db"
```

### Database Configuration

#### SQLite (Recommended for small servers)
```yaml
database:
  type: "sqlite"
  filePath: "plugins/NeoLogin/playerData.db"
```

#### MySQL/MariaDB (Recommended for large servers)
```yaml
database:
  type: "mysql"
  host: "localhost"
  port: 3306
  databaseName: "neoLogin"
  username: "root"
  password: "your_password"
```

### Player Restriction Configuration

```yaml
notLoggedInPlayerLimit:
  enabled: true
  type:
    move: true          # Prevent movement
    blockPlace: true    # Prevent block placement
    blockBreak: true    # Prevent block breaking
    chat: true          # Prevent chatting
    command: true       # Prevent command usage
    commandWhitelist:   # Command whitelist
      - "/login"
      - "/register"
```

---

## 🎮 Commands

### Player Commands

| Command            | Alias  | Description      | Permission |
| ------------------ | ------ | ---------------- | ---------- |
| `/register <password>` | `/reg` | Register account | None       |
| `/login <password>`    | `/l`   | Login account    | None       |

### Admin Commands

| Command                  | Alias             | Description                    | Permission       |
| ------------------------ | ----------------- | ------------------------------ | ---------------- |
| `/neologin reload`       | `/nl reload`      | Reload configuration file      | `neologin.admin` |
| `/neologin savelocation` | `/nl savelocation` | Save current location as spawn | `neologin.admin` |

---

## 🔑 Permissions

| Permission Node  | Description      | Default |
| ---------------- | ---------------- | ------- |
| `neologin.admin` | Admin permission | OP      |

---

## 🛠️ Development

### Building the Project

```bash
# Clone the project
git clone https://github.com/your-username/NeoLogin.git
cd NeoLogin

# Build the project
./gradlew build

# Run test server
./gradlew runServer
```

### Project Structure

```
NeoLogin/
├── src/main/java/           # Java source code
├── src/main/resources/      # Resource files
│   ├── config.yml          # Configuration file
│   ├── plugin.yml          # Plugin information
│   └── languages/          # Language files
├── build.gradle            # Gradle build file
└── README.md              # Project documentation
```

---

## 🤝 Contributing

We welcome contributions of any kind!

1. **Fork** this project
2. **Create** a feature branch (`git checkout -b feature/AmazingFeature`)
3. **Commit** your changes (`git commit -m 'Add some AmazingFeature'`)
4. **Push** to the branch (`git push origin feature/AmazingFeature`)
5. **Create** a Pull Request

---

## 📜 License

This project is licensed under the [Apache-2.0](LICENSE) license.

---

## 🙏 Acknowledgements

- Thanks to the other contributors of the [blbiLogin](https://github.com/EggFine/blbiLogin) project for their support
- Thanks to all contributors and users for their support

---

## 📞 Support

- **GitHub Issues**: [Submit Issues](../../issues)
- **Original Project Reference**: [blbiLogin](https://github.com/EggFine/blbiLogin)

---

<div align="center">

**⭐ If this project helps you, please give us a star!**

Made with ❤️ by [EggFine](https://github.com/EggFine)

</div> 