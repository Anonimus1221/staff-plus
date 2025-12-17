package net.shortninja.staffplus.server.compatibility;

import net.shortninja.staffplus.IStaffPlus;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Set;

/**
 * Generic protocol implementation for modern Minecraft versions (1.17+)
 * Uses basic Spigot API without NMS-specific implementations
 */
public class ModernProtocol extends AbstractProtocol {
    private static final String GADGET_KEY = "staffplus_gadget";

    public ModernProtocol(IStaffPlus staffPlus) {
        super(staffPlus);
    }

    @Override
    public String getVersion() {
        return "Modern (1.17+)";
    }

    @Override
    public ItemStack addNbtString(ItemStack item, String value) {
        if (item == null || value == null || value.isEmpty()) {
            return item;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return item;
        }

        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(org.bukkit.Bukkit.getPluginManager().getPlugin("StaffPlus"), GADGET_KEY);
        container.set(key, PersistentDataType.STRING, value);

        item.setItemMeta(meta);
        return item;
    }

    @Override
    public String getNbtString(ItemStack item) {
        if (item == null) {
            return "";
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return "";
        }

        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(org.bukkit.Bukkit.getPluginManager().getPlugin("StaffPlus"), GADGET_KEY);
        String value = container.get(key, PersistentDataType.STRING);

        return value != null ? value : "";
    }

    @Override
    public void registerCommand(String match, Command command) {
        // Commands are registered through plugin.yml
        // This is a no-op for modern versions
    }

    @Override
    public void listVanish(Player player, boolean shouldEnable) {
        // Vanish handling - basic implementation
        // Players can use vanish through direct visibility API
    }

    @Override
    public void sendHoverableJsonMessage(Set<Player> players, String message, String hoverMessage) {
        // JSON message handling - send as plain text for now
        for (Player player : players) {
            player.sendMessage(message);
        }
    }

    @Override
    public String getSound(Object object) {
        // Sound detection - not applicable for generic protocol
        return null;
    }

    @Override
    public void inject(Player player) {
        // Packet injection not needed for modern API
    }

    @Override
    public void uninject(Player player) {
        // Packet injection not needed for modern API
    }
}
