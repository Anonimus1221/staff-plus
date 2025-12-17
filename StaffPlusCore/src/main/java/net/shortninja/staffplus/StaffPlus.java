package net.shortninja.staffplus;

import net.shortninja.staffplus.player.NodeUser;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.player.attribute.SecurityHandler;
import net.shortninja.staffplus.player.attribute.TicketHandler;
import net.shortninja.staffplus.player.attribute.infraction.InfractionCoordinator;
import net.shortninja.staffplus.player.attribute.mode.ModeCoordinator;
import net.shortninja.staffplus.player.attribute.mode.handler.*;
import net.shortninja.staffplus.server.AlertCoordinator;
import net.shortninja.staffplus.server.PacketModifier;
import net.shortninja.staffplus.server.chat.ChatHandler;
import net.shortninja.staffplus.server.command.CmdHandler;
import net.shortninja.staffplus.server.compatibility.IProtocol;
import net.shortninja.staffplus.server.compatibility.ModernProtocol;
import net.shortninja.staffplus.server.data.*;
import net.shortninja.staffplus.server.data.storage.FlatFileStorage;
import net.shortninja.staffplus.server.data.storage.IStorage;
import net.shortninja.staffplus.server.data.storage.MemoryStorage;
import net.shortninja.staffplus.server.data.storage.MySQLStorage;
import net.shortninja.staffplus.server.data.config.IOptions;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.server.data.file.ChangelogFile;
import net.shortninja.staffplus.server.data.file.DataFile;
import net.shortninja.staffplus.server.data.file.LanguageFile;
import net.shortninja.staffplus.server.hook.HookHandler;
import net.shortninja.staffplus.server.hook.SuperVanishHook;
import net.shortninja.staffplus.server.listener.*;
import net.shortninja.staffplus.server.listener.entity.EntityChangeBlock;
import net.shortninja.staffplus.server.listener.entity.EntityDamage;
import net.shortninja.staffplus.server.listener.entity.EntityDamageByEntity;
import net.shortninja.staffplus.server.listener.entity.EntityTarget;
import net.shortninja.staffplus.server.listener.player.*;
import net.shortninja.staffplus.unordered.IUser;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.Metrics;
import net.shortninja.staffplus.util.PermissionHandler;
import net.shortninja.staffplus.util.lib.JavaUtils;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// TODO Add command to check e chests and offline player inventories

public class StaffPlus extends JavaPlugin implements IStaffPlus {
    private static StaffPlus plugin;

    public IProtocol versionProtocol;
    public PermissionHandler permission;
    public MessageCoordinator message;
    public Options options;
    public DataFile dataFile;
    public LanguageFile languageFile;
    public Messages messages;
    public UserManager userManager;

    public HookHandler hookHandler;
    public CpsHandler cpsHandler;
    public FreezeHandler freezeHandler;
    public GadgetHandler gadgetHandler;
    public ReviveHandler reviveHandler;
    public VanishHandler vanishHandler;
    public ChatHandler chatHandler;
    public TicketHandler ticketHandler;
    public CmdHandler cmdHandler;
    public ModeCoordinator modeCoordinator;
    public SecurityHandler securityHandler;
    public InfractionCoordinator infractionCoordinator;
    public AlertCoordinator alertCoordinator;
    public UUID consoleUUID = UUID.fromString("9c417515-22bc-46b8-be4d-538482992f8f");
    public Tasks tasks;
    public Map<UUID, IUser> users;
    public boolean ninePlus = false;
    public HashMap<Inventory, Block> viewedChest = new HashMap<>();
    public boolean twelvePlus = false;
    public boolean thirteenPlus = false;
    public IStorage storage;
    public InventoryHandler inventoryHandler;
    public boolean usesPlaceholderAPI;

    public static StaffPlus get() {
        return plugin;
    }

    @Override
    public void onLoad() {
        Plugin placeholderPlugin;
        if ((placeholderPlugin = Bukkit.getPluginManager().getPlugin("PlaceholderAPI")) != null) {
            usesPlaceholderAPI = true;
            Bukkit.getLogger().info("Hooked into PlaceholderAPI " + placeholderPlugin.getDescription().getVersion());
        }
    }

    @Override
    public void onEnable() {
        try {
            plugin = this;
            saveDefaultConfig();
            permission = new PermissionHandler(this);
            message = new MessageCoordinator(this);
            options = new Options();

            // Start the plugin (initializes dataFile before storage is created)
            start(System.currentTimeMillis());

            // Determine storage type (after dataFile is initialized)
            if (options.storageType.equalsIgnoreCase("mysql")) {
                storage = new MySQLStorage(new MySQLConnection());
            } else if (options.storageType.equalsIgnoreCase("flatfile")) {
                storage = new FlatFileStorage();
            } else {
                storage = new MemoryStorage();
                Bukkit.getLogger().warning(
                        "Storage type is invalid, defaulting to memory-based storage. IMPORTANT: Any changes are not persistent.");
            }

            // Enable metrics if configured
            if (getConfig().getBoolean("metrics"))
                new Metrics(this);

            checkUpdate();

            // Enable storage and hooks
            if (storage != null)
                storage.onEnable();

            if (hookHandler != null) {
                hookHandler.addHook(new SuperVanishHook(this));
                hookHandler.enableAll();
            }
        } catch (Exception e) {
            if (message != null) {
                message.sendConsoleMessage("FATAL ERROR: Failed to enable Staff+: " + e.getMessage(), true);
            } else {
                Bukkit.getLogger().severe("FATAL ERROR: Failed to enable Staff+: " + e.getMessage());
            }
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public UserManager getUserManager() {
        return userManager;
    }

    @Override
    public void onDisable() {
        if (message != null)
            message.sendConsoleMessage("Staff+ is now disabling!", true);
        stop();
    }

    public void saveUsers() {
        for (IUser user : userManager.getAll()) {
            new Save(new NodeUser(user));
        }

        dataFile.save();
    }

    public IStorage getStorage() {
        return storage;
    }

    protected void start(long start) {
        try {
            users = new HashMap<>();
            if (!setupVersionProtocol()) {
                message.sendConsoleMessage(
                        "This version of Minecraft is not supported! If you have just updated to a brand new server version, check the Spigot plugin page.",
                        true);
                Bukkit.getPluginManager().disablePlugin(this);
                return;
            }
            String[] tmp = Bukkit.getServer().getVersion().split("MC: ");
            String version = tmp[tmp.length - 1].substring(0, 4);
            ninePlus = JavaUtils.parseMcVer(version) >= 9;
            twelvePlus = JavaUtils.parseMcVer(version) >= 12;
            thirteenPlus = JavaUtils.parseMcVer(version) >= 13;
            dataFile = new DataFile("data.yml");
            languageFile = new LanguageFile();
            messages = new Messages();
            userManager = new UserManager(this);
            securityHandler = new SecurityHandler();
            hookHandler = new HookHandler();
            cpsHandler = new CpsHandler();
            freezeHandler = new FreezeHandler();
            gadgetHandler = new GadgetHandler();
            reviveHandler = new ReviveHandler();
            vanishHandler = new VanishHandler();
            chatHandler = new ChatHandler();
            ticketHandler = new TicketHandler();
            cmdHandler = new CmdHandler();
            modeCoordinator = new ModeCoordinator();
            infractionCoordinator = new InfractionCoordinator();
            alertCoordinator = new AlertCoordinator();
            tasks = new Tasks();
            inventoryHandler = new InventoryHandler();
            for (Player player : Bukkit.getOnlinePlayers()) {
                new Load(player);
            }
            registerListeners();
            new ChangelogFile();

            if (!options.disablePackets || !options.animationPackets.isEmpty() || !options.soundNames.isEmpty()) {
                new PacketModifier();
            }

            message.sendConsoleMessage(
                    "Staff+ has been enabled! Initialization took " + (System.currentTimeMillis() - start) + "ms.",
                    false);
            message.sendConsoleMessage("Plugin created by Anonimus1221.", false);
        } catch (Exception e) {
            message.sendConsoleMessage("ERROR during start(): " + e.getMessage(), true);
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    private boolean setupVersionProtocol() {
        // For v1.17+, use the modern protocol implementation
        final String version = Bukkit.getServer().getClass().getPackage().getName();
        final String formattedVersion = version.substring(version.lastIndexOf('.') + 1);

        versionProtocol = new ModernProtocol(this);
        message.sendConsoleMessage("Version '" + formattedVersion + "' detected - using modern Spigot API.", false);

        return true;
    }

    private void registerListeners() {
        new EntityDamage();
        new EntityDamageByEntity();
        new EntityTarget();
        new AsyncPlayerChat();
        new PlayerCommandPreprocess();
        new PlayerDeath();
        new PlayerDropItem();
        new PlayerInteract();
        new PlayerLogin();
        new PlayerJoin();
        new PlayerPickupItem();
        new PlayerQuit();
        new BlockBreak();
        new BlockPlace();
        new FoodLevelChange();
        new InventoryClick();
        new InventoryClose();
        new InventoryOpen();
        new PlayerWorldChange();
        new EntityChangeBlock();
        String[] tmp = Bukkit.getServer().getVersion().split("MC: ");
        String version = tmp[tmp.length - 1].substring(0, 4);
        if (JavaUtils.parseMcVer(version) >= 10)
            new TabComplete();
    }

    private void checkUpdate() {
        // Update checking disabled
    }

    /*
     * Nullifying all of the instances is sort of an experimental thing to deal
     * with memory leaks that could occur on reloads (where instances could be
     * handled incorrectly)
     */

    private void stop() {
        if (hookHandler != null)
            hookHandler.disableAll();

        if (userManager != null && dataFile != null)
            saveUsers();

        if (tasks != null)
            tasks.cancel();

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (modeCoordinator != null)
                modeCoordinator.removeMode(player);
            if (vanishHandler != null)
                vanishHandler.removeVanish(player);
        }

        if (storage != null)
            storage.onDisable();

        versionProtocol = null;
        permission = null;
        message = null;
        options = null;
        languageFile = null;
        userManager = null;
        securityHandler = null; // FIXME
        cpsHandler = null;
        freezeHandler = null;
        gadgetHandler = null;
        reviveHandler = null;
        vanishHandler = null;
        chatHandler = null;
        ticketHandler = null;
        cmdHandler = null;
        modeCoordinator = null;
        infractionCoordinator = null;
        alertCoordinator = null;
        tasks = null;
        plugin = null;

    }

    @Override
    public IOptions getOptions() {
        return options;
    }

    @Override
    public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command cmd, String label,
            String[] args) {
        if (cmdHandler != null) {
            cmdHandler.attemptCommand(sender, label, args);
            return true;
        }
        return false;
    }

    public void reloadFiles() {
        options = new Options();
        languageFile = new LanguageFile();
        messages = new Messages();
    }

    public PermissionHandler getPermissions() {
        return permission;
    }
}