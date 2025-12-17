![logo](assets/ico
/ba89a284-1dff-442e-bcf0-b978e7456234.png)

[![License: MIT](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.17--1.21.11-brightgreen.svg)](https://www.minecraft.net)
[![Java](https://img.shields.io/badge/Java-8%2B-orange.svg)](https://www.java.com)
[![Maven](https://img.shields.io/badge/Build-Maven-red.svg)](https://maven.apache.org/)

Staff++ is a modernized and feature-rich moderation plugin for Minecraft servers, providing administrators and moderators with powerful tools to manage their communities effectively.

Staff++ is maintained as an open collaboration project.

## What is Staff++?

Staff++ is a comprehensive moderation plugin bridging professional server management with ease of use. It combines advanced features for handling player reports, staff gadgets, and administrative controls in one unified package. Whether you're running a small community server or a large network, Staff++ adapts to your needs.

Special thanks to the Spigot and Paper communities for their continuous support and feedback!

## Supported Versions

Staff++ currently supports Minecraft Java Edition 1.17 - 1.21.11 across Spigot, Paper, and Bukkit servers. For more information on compatibility, please see [here](WIKI_NEW.md).

## Setting Up

Take a look [here](WIKI_NEW.md) for comprehensive installation and configuration guides.

## Links:

- Documentation: [WIKI](https://github.com/Anonimus1221/staff-plus/wiki)
- GitHub: https://github.com/Anonimus1221/staff-plus
- License: MIT
- Supported Servers: Spigot, Paper, Bukkit

## Key Features

- **Advanced Moderation System** - Freeze, Vanish, Examine, Alerts
- **Staff Gadgets** - Freecam, ItemRadar, XRay, NightVision, Invisible
- **Ticket System** - Automated player reports and ticket management
- **Multi-Language** - Support for 7+ languages
- **Flexible Database** - YML or MySQL storage
- **Clean Architecture** - Modern code structure with zero problematic dependencies

## What can't be fixed

Some Minecraft mechanics limit what plugins can achieve. For detailed information on current limitations and workarounds, see the [WIKI_NEW.md](WIKI_NEW.md) documentation.

## Compiling

1. Clone the repository to your computer
   ```bash
   git clone https://github.com/Anonimus1221/staff-plus.git
   cd staff-plus
   ```

2. Run Maven build
   ```bash
   mvn clean package -DskipTests
   ```

3. Locate compiled JAR files
   - `StaffPlusCore/target/Paper/Staff+.jar` - Paper server
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


