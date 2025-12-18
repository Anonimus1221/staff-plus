![logo](assets/ico/logo.png)

[![License: MIT](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.17--1.21.11-brightgreen.svg)](https://www.minecraft.net)
[![Java](https://img.shields.io/badge/Java-8%2B-orange.svg)](https://www.java.com)
[![Maven](https://img.shields.io/badge/Build-Maven-red.svg)](https://maven.apache.org/)

Staff++ is a modernized and feature-rich moderation plugin for Minecraft servers, providing administrators and moderators with powerful tools to manage their communities effectively.

Staff++ is maintained as an open collaboration project.

## What is Staff++?

Staff++ is a comprehensive moderation plugin that bridges professional server management with ease of use. It combines advanced features for handling player reports, staff gadgets, and administrative controls in one unified package. Whether you're running a small community server or a large network, Staff++ adapts to your needs with a clean architecture and zero problematic dependencies.

### Key Features:
- **Advanced Moderation** - Freeze players, Vanish mode, Examine inventories, Real-time alerts
- **Staff Gadgets** - Freecam, ItemRadar, XRay, NightVision, Invisible mode
- **Ticket System** - Automated player reports and comprehensive ticket management
- **Multi-Language** - Full support for 12 languages (EN, ES, DE, FR, IT, PT, NL, HR, HU, NO, SV, ZH)
- **Flexible Storage** - YML flat file or MySQL database support
- **Modern Architecture** - Clean codebase with version-specific adapters for all supported Minecraft versions

Special thanks to the Spigot and Paper communities for their continuous support and feedback!

## Supported Versions

Staff++ supports Minecraft Java Edition versions **1.17 through 1.21.11** across all major server software:
- **Spigot** - Complete support
- **Paper** - Full compatibility with latest versions
- **Bukkit** - Full Bukkit API compliance

Each version includes a dedicated version adapter ensuring optimal performance and compatibility.

For detailed compatibility information, see the [WIKI](https://github.com/Anonimus1221/staff-plus/wiki).

## Setting Up

Take a look [here](WIKI) for comprehensive installation and configuration guides.

## Links:

- **Documentation**: [WIKI - Complete Guide](WIKI)
- **GitHub**: https://github.com/Anonimus1221/staff-plus
- **License**: MIT
- **Supported Servers**: Spigot, Paper, Bukkit

## Contributing

### Requirements
- Java 8 or higher
- Maven 3.8.1+
- Git

### Build Instructions

1. Clone the repository
   ```bash
   git clone https://github.com/Anonimus1221/staff-plus.git
   cd staff-plus
   ```

2. Run Maven build
   ```bash
   mvn clean package -DskipTests
   ```

3. Locate compiled artifacts
   - Main plugin: `StaffPlusCore/target/Staff+.jar`
   - Version adapters: `v1_XX_RX/target/v1_XX_RX-4.0.0.jar` (for each version)

The build system automatically creates 14 JAR files: 1 API, 1 Core plugin, 1 Bungee adapter, and 11 version-specific adapters.
## Contributing

Any contributions are appreciated. Feel free to reach out on GitHub if you're interested in helping improve Staff++.

## Libraries Used:

- [HikariCP](https://github.com/brettwooldridge/HikariCP) - Database connection pooling
- [Apache Commons Lang](https://commons.apache.org/proper/commons-lang/) - Java utilities
- [Bcrypt](https://github.com/patrickfav/bcrypt) - Password hashing
- [SLF4J](https://www.slf4j.org/) - Logging framework
- Spigot/Paper API - Server development kit

## Configuration

### Main Files
Configuration files are located in `plugins/StaffPlus/`:

- `config.yml` - Main plugin configuration
- `messages.yml` - Localization strings
- `permissions.yml` - Permission structure

## Installation

1. Download `Staff+.jar` from releases
2. Place in `plugins/` folder of your Minecraft server
3. Restart server - plugin auto-generates config
4. Configure `plugins/StaffPlus/config.yml` as needed

## System Requirements

- Java 8 or higher
- Minecraft 1.17 - 1.21.11
- At least 2GB RAM (4GB+ recommended)
- Spigot, Paper, or Bukkit server

---

<div align="center">

**Made with ❤️ by Anonimus1221**

[GitHub](https://github.com/Anonimus1221/staff-plus) • [License](LICENSE) • [Documentation](WIKI)

Last updated: December 2025

</div>


