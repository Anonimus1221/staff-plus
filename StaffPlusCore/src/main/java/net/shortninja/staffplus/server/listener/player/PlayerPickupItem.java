package net.shortninja.staffplus.server.listener.player;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.attribute.mode.ModeCoordinator;
import net.shortninja.staffplus.server.data.config.Options;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

/**
 * Handles item pickup events for players in staff mode.
 * Prevents staff members from picking up items while in staff mode.
 *
 * Note: PlayerPickupItemEvent is deprecated in newer versions of Bukkit.
 * This is a temporary implementation that should be updated to use
 * EntityPickupItemEvent in future versions for better compatibility.
 */
public class PlayerPickupItem implements Listener {
    private Options options = StaffPlus.get().options;
    private ModeCoordinator modeCoordinator = StaffPlus.get().modeCoordinator;

    public PlayerPickupItem() {
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    /**
     * Called when a player attempts to pick up an item.
     * If the player is in staff mode and item pickup is disabled,
     * the event is cancelled to prevent the item from being picked up.
     *
     * @param event the PlayerPickupItemEvent
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    @SuppressWarnings("deprecation")
    public void onPickup(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();

        // If item changes are disabled OR player is not in staff mode, allow pickup
        if (options.modeItemChange || !modeCoordinator.isInMode(player.getUniqueId())) {
            return;
        }

        // Cancel pickup - staff mode players cannot pick up items
        event.setCancelled(true);
    }
}
