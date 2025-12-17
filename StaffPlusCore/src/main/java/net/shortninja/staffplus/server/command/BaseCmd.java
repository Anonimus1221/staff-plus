package net.shortninja.staffplus.server.command;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.util.MessageCoordinator;
import org.bukkit.command.Command;

import java.util.ArrayList;
import java.util.List;

public class BaseCmd {
    private Messages messages = StaffPlus.get().messages;
    private String node;
    private Command command;
    private boolean isEnabled;
    private String match;
    private String description;
    private String usage;
    private List<String> permissions = new ArrayList<String>();

    public BaseCmd(String node, Command command, boolean isEnabled, String permission, String description,
            String usage) {
        this.node = node;
        this.command = command;
        this.isEnabled = isEnabled;
        this.match = command.getName() != null ? command.getName() : node;
        this.description = MessageCoordinator.colorize(description);
        this.usage = "/" + match + " " + usage;

        this.command.setPermissionMessage(MessageCoordinator.colorize(messages.noPermission));
        this.command.setDescription(description);
        this.command.setUsage(usage);

        if (!permission.isEmpty()) {
            this.command.setPermission(permission);
            permissions.add(permission);
        }
    }

    public BaseCmd(String node, Command command, boolean isEnabled, List<String> permission, String description,
            String usage) {
        this.node = node;
        this.command = command;
        this.isEnabled = isEnabled;
        this.match = command.getName() != null ? command.getName() : node;
        this.description = description;
        this.usage = "/" + match + " " + usage;

        this.command.setPermissionMessage(MessageCoordinator.colorize(messages.noPermission));
        this.command.setDescription(description);
        this.command.setUsage(usage);
        permissions.addAll(permission);
    }

    public BaseCmd(String node, Command command, boolean isEnabled, String description, String usage) {
        this(node, command, isEnabled, "", description, usage);
    }

    public String getNode() {
        return node;
    }

    public boolean isEnabled() {
        return !match.isEmpty() && isEnabled;
    }

    public Command getCommand() {
        return command;
    }

    public String getMatch() {
        return match;
    }

    public String getDescription() {
        return description;
    }

    public String getUsage() {
        return usage;
    }

    public boolean matches(String string) {
        // Check main command name
        if (match.equalsIgnoreCase(string)) {
            return true;
        }
        // Check all aliases
        List<String> aliases = command.getAliases();
        if (aliases != null) {
            for (String alias : aliases) {
                if (alias.equalsIgnoreCase(string)) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<String> getPermissions() {
        return permissions;
    }
}