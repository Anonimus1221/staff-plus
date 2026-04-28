package net.shortninja.staffplus.server.command.cmd;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.PermissionHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class StaffPlusCmd extends BukkitCommand {
    private PermissionHandler permission = StaffPlus.get().permission;
    private MessageCoordinator message = StaffPlus.get().message;
    private Options options = StaffPlus.get().options;
    private Messages messages = StaffPlus.get().messages;

    public StaffPlusCmd(String name) {
        super(name);
    }

    public boolean execute(CommandSender sender, String alias, String[] args) {
        if (!permission.has(sender, options.permissionStaff)) {
            message.send(sender, messages.noPermission, messages.prefixGeneral);
            return true;
        }
        if (sender instanceof Player) {
            if (args.length == 1 && permission.has(sender, options.permissionStaff)) {
                if (args[0].equalsIgnoreCase("reload")) {
                    Bukkit.getPluginManager().getPlugin("StaffPlus").reloadConfig();
                    StaffPlus.get().reloadFiles();
                    StaffPlus.get().message.sendConsoleMessage("Plugin config and lang file reloaded", false);
                    StaffPlus.get().message.send((Player) sender, "Config and lang file have been reloaded",
                            "StaffPlus");
                } else if (args[0].equalsIgnoreCase("check-update") || args[0].equalsIgnoreCase("update")) {
                    handleUpdateCheck(sender);
                }
            }
        } else {
            if (args.length == 1 && permission.has(sender, options.permissionStaff)) {
                if (args[0].equalsIgnoreCase("reload")) {
                    Bukkit.getPluginManager().getPlugin("StaffPlus").reloadConfig();
                    StaffPlus.get().reloadFiles();
                    StaffPlus.get().message.sendConsoleMessage("Plugin config and lang file reloaded", false);
                } else if (args[0].equalsIgnoreCase("check-update") || args[0].equalsIgnoreCase("update")) {
                    handleUpdateCheck(sender);
                }
            }
        }
        return true;
    }

    private void handleUpdateCheck(CommandSender sender) {
        StaffPlus plugin = StaffPlus.get();
        if (plugin.updateChecker == null) {
            message.send(sender, "§cUpdate checker is not initialized", "StaffPlus");
            return;
        }

        message.send(sender, "§aChecking for updates...", "StaffPlus");

        // Si ya se completó la verificación, mostrar resultado inmediatamente
        if (plugin.updateChecker.isCheckCompleted()) {
            showUpdateStatus(sender, plugin);
        } else {
            // Esperar a que se complete la verificación
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                // Hacer una verificación inmediata
                plugin.updateChecker.startAsyncCheck();

                // Esperar hasta 5 segundos para que se complete
                for (int i = 0; i < 50; i++) {
                    if (plugin.updateChecker.isCheckCompleted()) {
                        Bukkit.getScheduler().runTask(plugin, () -> showUpdateStatus(sender, plugin));
                        return;
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }

                // Timeout
                Bukkit.getScheduler().runTask(plugin,
                        () -> message.send(sender, "§cUpdate check timed out. Please try again later.", "StaffPlus"));
            });
        }
    }

    private void showUpdateStatus(CommandSender sender, StaffPlus plugin) {
        String current = plugin.getDescription().getVersion();
        if (plugin.updateChecker.isUpdateAvailable()) {
            String latest = plugin.updateChecker.getLatestVersion();
            message.send(sender, "§c§lNew version available!", "StaffPlus");
            message.send(sender, "§fCurrent: §c" + current, "");
            message.send(sender, "§fLatest: §a" + latest, "");
            message.send(sender, "§fDownload: §bhttps://modrinth.com/plugin/staff-plus/versions", "");
        } else {
            message.send(sender, "§aYou are running the latest version!", "StaffPlus");
            message.send(sender, "§fCurrent: §a" + current, "");
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        if (args.length == 0) {
            return List.of("reload", "check-update");
        }

        return super.tabComplete(sender, alias, args);
    }
}
