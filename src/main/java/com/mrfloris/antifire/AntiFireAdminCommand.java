package com.mrfloris.antifire;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

final class AntiFireAdminCommand implements CommandExecutor, TabCompleter {
    private static final String ADMIN_PERMISSION = "onembantifire.admin";

    private final AntiFirePlugin plugin;

    AntiFireAdminCommand(AntiFirePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(ADMIN_PERMISSION)) {
            sender.sendMessage("[1MB AntiFire] You need " + ADMIN_PERMISSION + " to manage this plugin.");
            return true;
        }

        if (args.length == 0 || args[0].equalsIgnoreCase("status")) {
            sendStatus(sender);
            return true;
        }

        String subCommand = args[0].toLowerCase(Locale.ROOT);
        switch (subCommand) {
            case "reload" -> {
                plugin.reloadAntiFireSettings();
                sender.sendMessage("[1MB AntiFire] Configuration reloaded from disk.");
                return true;
            }
            case "toggle" -> {
                if (args.length < 3) {
                    sender.sendMessage("[1MB AntiFire] Usage: /" + label + " toggle <key> <value>");
                    sender.sendMessage("[1MB AntiFire] Keys: " + AntiFireSetting.availableKeys());
                    return true;
                }

                Optional<AntiFireSetting> setting = AntiFireSetting.fromPath(args[1]);
                if (setting.isEmpty()) {
                    sender.sendMessage("[1MB AntiFire] Unknown key: " + args[1]);
                    sender.sendMessage("[1MB AntiFire] Keys: " + AntiFireSetting.availableKeys());
                    return true;
                }

                try {
                    Object parsedValue = setting.get().parseValue(args[2]);
                    plugin.updateSetting(setting.get(), parsedValue);
                    sender.sendMessage("[1MB AntiFire] Updated " + setting.get().path() + " = " + parsedValue);
                } catch (IllegalArgumentException exception) {
                    sender.sendMessage("[1MB AntiFire] " + exception.getMessage());
                }
                return true;
            }
            default -> {
                sender.sendMessage("[1MB AntiFire] Usage: /" + label + " [status|reload|toggle <key> <value>]");
                return true;
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!sender.hasPermission(ADMIN_PERMISSION)) {
            return List.of();
        }

        if (args.length == 1) {
            return filterMatches(args[0], List.of("status", "reload", "toggle"));
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("toggle")) {
            return filterMatches(
                    args[1],
                    List.of(
                            AntiFireSetting.PREVENT_FIRE_SPREAD.path(),
                            AntiFireSetting.PREVENT_BLOCK_BURN.path(),
                            AntiFireSetting.EXTINGUISH_ENABLED.path(),
                            AntiFireSetting.EXTINGUISH_DELAY_TICKS.path(),
                            AntiFireSetting.CHECK_INTERVAL_TICKS.path(),
                            AntiFireSetting.TRACK_PLAYER_PLACED_FIRE.path(),
                            AntiFireSetting.TRACK_LIGHTNING_FIRE.path(),
                            AntiFireSetting.TRACK_LAVA_FIRE.path(),
                            AntiFireSetting.TRACK_OTHER_IGNITE_FIRE.path(),
                            AntiFireSetting.STARTUP_LOG.path()
                    )
            );
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("toggle")) {
            Optional<AntiFireSetting> setting = AntiFireSetting.fromPath(args[1]);
            if (setting.isPresent() && (setting.get() == AntiFireSetting.EXTINGUISH_DELAY_TICKS
                    || setting.get() == AntiFireSetting.CHECK_INTERVAL_TICKS)) {
                return filterMatches(args[2], List.of("20", "40", "100", "200"));
            }
            return filterMatches(args[2], List.of("true", "false"));
        }

        return List.of();
    }

    private void sendStatus(CommandSender sender) {
        sender.sendMessage("[1MB AntiFire] Current config:");
        for (Map.Entry<String, Object> entry : plugin.getSettings().asMap().entrySet()) {
            sender.sendMessage("- " + entry.getKey() + " = " + entry.getValue());
        }
    }

    private List<String> filterMatches(String token, List<String> options) {
        String normalizedToken = token.toLowerCase(Locale.ROOT);
        return options.stream()
                .filter(option -> option.toLowerCase(Locale.ROOT).startsWith(normalizedToken))
                .collect(Collectors.toList());
    }
}
