package net.shortninja.staffplus.version;

import net.shortninja.staffplus.IStaffPlus;
import net.shortninja.staffplus.server.compatibility.AbstractProtocol;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import java.util.Set;

/**
 * Version adapter for Minecraft 1.20 R1
 */
public class VersionAdapterImpl extends AbstractProtocol {
    public VersionAdapterImpl(IStaffPlus staffPlus) {
        super(staffPlus);
    }

    @Override
    public ItemStack addNbtString(ItemStack item, String value) {
        return item;
    }

    @Override
    public String getNbtString(ItemStack item) {
        return null;
    }

    @Override
    public void registerCommand(String match, Command command) {
    }

    @Override
    public void listVanish(Player player, boolean shouldEnable) {
    }

    @Override
    public void sendHoverableJsonMessage(Set<Player> players, String message, String hoverMessage) {
    }

    @Override
    public String getSound(Object object) {
        return null;
    }

    @Override
    public void inject(Player player) {
    }

    @Override
    public void uninject(Player player) {
    }
}
