package com.mrfloris.antifire;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

final class AntiFireAdminCommand implements BasicCommand {
    private static final String ADMIN_PERMISSION = "onembantifire.admin";
    private static final String COMMAND_USAGE = "/_antifire [debug|reload|toggle <key> <value>|help]";
    private static final List<String> SUBCOMMANDS = List.of("debug", "reload", "toggle", "help");

    private final AntiFirePlugin plugin;

    AntiFireAdminCommand(AntiFirePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSourceStack source, String[] args) {
        CommandSender sender = source.getSender();

        if (args.length == 0) {
            sendHelp(sender);
            return;
        }

        String subCommand = args[0].toLowerCase(Locale.ROOT);
        switch (subCommand) {
            case "debug" -> {
                sendDebug(sender);
                return;
            }
            case "help" -> {
                sendHelp(sender);
                return;
            }
            case "reload" -> {
                plugin.reloadAntiFireSettings();
                sender.sendMessage("[1MB AntiFire] Reloaded config.yml from disk.");
                sender.sendMessage("[1MB AntiFire] Spread protection is " + formatToggle(plugin.getSettings().preventFireSpread())
                        + ", block burn protection is " + formatToggle(plugin.getSettings().preventBlockBurn())
                        + ", and auto-extinguish is " + formatToggle(plugin.getSettings().extinguishEnabled()) + ".");
                return;
            }
            case "toggle" -> {
                if (args.length < 3) {
                    sender.sendMessage("[1MB AntiFire] Usage: /_antifire toggle <key> <value>");
                    sender.sendMessage("[1MB AntiFire] Keys: " + AntiFireSetting.availableKeys());
                    return;
                }

                Optional<AntiFireSetting> setting = AntiFireSetting.fromPath(args[1]);
                if (setting.isEmpty()) {
                    sender.sendMessage("[1MB AntiFire] Unknown key: " + args[1]);
                    sender.sendMessage("[1MB AntiFire] Keys: " + AntiFireSetting.availableKeys());
                    return;
                }

                try {
                    Object previousValue = setting.get().readValue(plugin.getConfiguration());
                    Object parsedValue = setting.get().parseValue(args[2]);
                    plugin.updateSetting(setting.get(), parsedValue);
                    Object appliedValue = setting.get().readValue(plugin.getConfiguration());
                    sender.sendMessage("[1MB AntiFire] Saved " + setting.get().path()
                            + " from " + formatValue(previousValue)
                            + " to " + formatValue(appliedValue) + ".");
                } catch (IllegalArgumentException exception) {
                    sender.sendMessage("[1MB AntiFire] " + exception.getMessage());
                }
                return;
            }
            default -> {
                sender.sendMessage("[1MB AntiFire] Unknown subcommand. Try " + COMMAND_USAGE);
                sender.sendMessage("[1MB AntiFire] Keys: " + AntiFireSetting.availableKeys());
            }
        }
    }

    @Override
    public Collection<String> suggest(CommandSourceStack source, String[] args) {
        if (args.length == 0) {
            return SUBCOMMANDS;
        }

        if (args.length == 1) {
            if (args[0].isBlank()) {
                return SUBCOMMANDS;
            }
            return filterMatches(args[0], SUBCOMMANDS);
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

    @Override
    public String permission() {
        return ADMIN_PERMISSION;
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender instanceof ConsoleCommandSender || sender.hasPermission(ADMIN_PERMISSION);
    }

    private void sendDebug(CommandSender sender) {
        AntiFireSettings settings = plugin.getSettings();
        BuildMetadata buildMetadata = plugin.getBuildMetadata();
        sender.sendMessage("[1MB AntiFire] Debug view for " + plugin.getName() + " v" + plugin.getPluginMeta().getVersion());
        sender.sendMessage("[1MB AntiFire] Build:");
        sender.sendMessage("  Paper API compile target: " + buildMetadata.paperCompileTarget());
        sender.sendMessage("  Paper API dependency: " + buildMetadata.paperCompileDependency());
        sender.sendMessage("  plugin.yml api-version floor: " + buildMetadata.pluginApiCompatibilityFloor());
        sender.sendMessage("  Java target: " + buildMetadata.javaTarget());
        sender.sendMessage("[1MB AntiFire] Protection:");
        sender.sendMessage("  fire spread: " + formatToggle(settings.preventFireSpread()));
        sender.sendMessage("  block burn: " + formatToggle(settings.preventBlockBurn()));
        sender.sendMessage("[1MB AntiFire] Temporary fire:");
        sender.sendMessage("  auto extinguish: " + formatToggle(settings.extinguishEnabled()));
        sender.sendMessage("  extinguish delay: " + formatTicks(settings.extinguishDelayTicks()));
        sender.sendMessage("  check interval: " + formatTicks(settings.checkIntervalTicks()));
        sender.sendMessage("[1MB AntiFire] Tracked ignite sources:");
        sender.sendMessage("  player placed fire: " + formatToggle(settings.trackPlayerPlacedFire()));
        sender.sendMessage("  lightning fire: " + formatToggle(settings.trackLightningFire()));
        sender.sendMessage("  lava fire: " + formatToggle(settings.trackLavaFire()));
        sender.sendMessage("  other ignite fire: " + formatToggle(settings.trackOtherIgniteFire()));
        sender.sendMessage("[1MB AntiFire] Logging:");
        sender.sendMessage("  startup log: " + formatToggle(settings.startupLog()));
        sender.sendMessage("[1MB AntiFire] Command: " + COMMAND_USAGE);
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("[1MB AntiFire] Commands:");
        sender.sendMessage("  /_antifire - shows this help summary");
        sender.sendMessage("  /_antifire help - shows this help summary");
        sender.sendMessage("  /_antifire debug - shows the current config state");
        sender.sendMessage("  /_antifire reload - reloads config.yml from disk");
        sender.sendMessage("  /_antifire toggle <key> <value> - updates and saves a config key");
        sender.sendMessage("[1MB AntiFire] Permission: " + ADMIN_PERMISSION + " (console is always allowed)");
        sender.sendMessage("[1MB AntiFire] Keys: " + AntiFireSetting.availableKeys());
    }

    private List<String> filterMatches(String token, List<String> options) {
        String normalizedToken = token.toLowerCase(Locale.ROOT);
        return options.stream()
                .filter(option -> option.toLowerCase(Locale.ROOT).startsWith(normalizedToken))
                .collect(Collectors.toList());
    }

    private String formatToggle(boolean enabled) {
        return enabled ? "enabled" : "disabled";
    }

    private String formatTicks(int ticks) {
        return ticks + " ticks (" + String.format(Locale.ROOT, "%.1f", ticks / 20.0D) + "s)";
    }

    private String formatValue(Object value) {
        if (value instanceof Boolean booleanValue) {
            return formatToggle(booleanValue);
        }
        return String.valueOf(value);
    }
}
