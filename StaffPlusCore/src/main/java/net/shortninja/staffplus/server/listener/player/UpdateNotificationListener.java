package net.shortninja.staffplus.server.listener.player;

import net.shortninja.staffplus.StaffPlus;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Listener que notifica a los administradores sobre nuevas versiones
 * disponibles
 */
public class UpdateNotificationListener implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Solo notificar a administradores con permiso de staff
        if (!player.hasPermission("staff.mode")) {
            return;
        }

        StaffPlus plugin = StaffPlus.get();

        // Si no hay UpdateChecker, no hacer nada
        if (plugin.updateChecker == null) {
            return;
        }

        // Esperar un poco para que el jugador se cargue completamente
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            // Si la verificación se ha completado y hay una actualización disponible
            if (plugin.updateChecker.isCheckCompleted() && plugin.updateChecker.isUpdateAvailable()) {
                String message = "§c§l[STAFF+] §r§cNueva versión disponible: §f" +
                        plugin.updateChecker.getLatestVersion() +
                        "§c. Descarga desde: §f" +
                        "https://modrinth.com/plugin/staff-plus/versions";

                player.sendMessage("§c");
                player.sendMessage(message);
                player.sendMessage("§c");
            }
        }, 10L); // 10 ticks de delay
    }
}
