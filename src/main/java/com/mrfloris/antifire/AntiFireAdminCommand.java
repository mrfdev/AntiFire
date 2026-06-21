package com.mrfloris.antifire;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
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
                AntiFireSettings settings = plugin.getSettings();
                AntiFireMessageStyle.success(sender, "Reloaded config.yml from disk.");
                AntiFireMessageStyle.state(sender, "fire spread", settings.preventFireSpread());
                AntiFireMessageStyle.state(sender, "block burn", settings.preventBlockBurn());
                AntiFireMessageStyle.state(sender, "auto extinguish", settings.extinguishEnabled());
                AntiFireMessageStyle.state(sender, "permanent soul fire", settings.allowPermanentSoulFire());
                return;
            }
            case "toggle" -> {
                if (args.length < 3) {
                    AntiFireMessageStyle.error(sender, "Usage: /_antifire toggle <key> <value>");
                    sendKeyHelp(sender);
                    return;
                }

                Optional<AntiFireSetting> setting = AntiFireSetting.fromPath(args[1]);
                if (setting.isEmpty()) {
                    AntiFireMessageStyle.error(sender, "Unknown key: " + args[1]);
                    sendKeyHelp(sender);
                    return;
                }

                try {
                    Object previousValue = setting.get().readValue(plugin.getConfiguration());
                    Object parsedValue = setting.get().parseValue(args[2]);
                    plugin.updateSetting(setting.get(), parsedValue);
                    Object appliedValue = setting.get().readValue(plugin.getConfiguration());
                    AntiFireMessageStyle.success(sender, "Saved " + setting.get().path() + ".");
                    sendValueDetail(sender, "from", previousValue);
                    sendValueDetail(sender, "to", appliedValue);
                    AntiFireMessageStyle.detail(sender, "takes effect", "immediately");
                } catch (IllegalArgumentException exception) {
                    AntiFireMessageStyle.error(sender, exception.getMessage());
                }
                return;
            }
            default -> {
                AntiFireMessageStyle.error(sender, "Unknown subcommand. Try " + COMMAND_USAGE);
                sendKeyHelp(sender);
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
            return filterMatches(args[1], AntiFireSetting.paths());
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
        AntiFireMessageStyle.header(sender, "Debug");
        AntiFireMessageStyle.note(sender, "Live runtime view for " + plugin.getName() + " v" + plugin.getPluginMeta().getVersion() + ".");
        AntiFireMessageStyle.itemStrong(sender, "Build");
        AntiFireMessageStyle.detail(sender, "Paper API compile target", buildMetadata.paperCompileTarget());
        AntiFireMessageStyle.detail(sender, "Paper API dependency", buildMetadata.paperCompileDependency());
        AntiFireMessageStyle.detail(sender, "plugin.yml api-version", buildMetadata.pluginApiVersion());
        AntiFireMessageStyle.detail(sender, "Java target", buildMetadata.javaTarget());
        AntiFireMessageStyle.itemStrong(sender, "Protection");
        AntiFireMessageStyle.state(sender, "fire spread", settings.preventFireSpread());
        AntiFireMessageStyle.state(sender, "block burn", settings.preventBlockBurn());
        AntiFireMessageStyle.itemStrong(sender, "Temporary fire");
        AntiFireMessageStyle.state(sender, "auto extinguish", settings.extinguishEnabled());
        AntiFireMessageStyle.detail(sender, "extinguish delay", formatTicks(settings.extinguishDelayTicks()));
        AntiFireMessageStyle.detail(sender, "check interval", formatTicks(settings.checkIntervalTicks()));
        AntiFireMessageStyle.itemStrong(sender, "Permanent fire");
        AntiFireMessageStyle.state(sender, "netherrack", true);
        AntiFireMessageStyle.state(sender, "soul sand / soul soil soul fire", settings.allowPermanentSoulFire());
        AntiFireMessageStyle.itemStrong(sender, "Ignite tracking");
        AntiFireMessageStyle.state(sender, "player placed fire", settings.trackPlayerPlacedFire());
        AntiFireMessageStyle.state(sender, "lightning fire", settings.trackLightningFire());
        AntiFireMessageStyle.state(sender, "lava fire", settings.trackLavaFire());
        AntiFireMessageStyle.state(sender, "other ignite fire", settings.trackOtherIgniteFire());
        AntiFireMessageStyle.itemStrong(sender, "Admin");
        AntiFireMessageStyle.detail(sender, "command", COMMAND_USAGE);
        AntiFireMessageStyle.detail(sender, "permission", ADMIN_PERMISSION + " (console always allowed)");
        AntiFireMessageStyle.itemStrong(sender, "Quick toggle");
        AntiFireMessageStyle.detail(sender, "enable soul fire", "/_antifire toggle allow-permanent-soul-fire true");
    }

    private void sendHelp(CommandSender sender) {
        AntiFireMessageStyle.header(sender, "Help");
        AntiFireMessageStyle.note(sender, "Trusted live controls for AntiFire protection settings.");
        AntiFireMessageStyle.itemStrong(sender, "/_antifire");
        AntiFireMessageStyle.detail(sender, "does", "Show this help summary.");
        AntiFireMessageStyle.itemStrong(sender, "/_antifire help");
        AntiFireMessageStyle.detail(sender, "does", "Show this help summary.");
        AntiFireMessageStyle.itemStrong(sender, "/_antifire debug");
        AntiFireMessageStyle.detail(sender, "does", "Show build metadata, active protection toggles, and permanent-fire exceptions.");
        AntiFireMessageStyle.detail(sender, "permission", ADMIN_PERMISSION);
        AntiFireMessageStyle.itemStrong(sender, "/_antifire reload");
        AntiFireMessageStyle.detail(sender, "does", "Reload config.yml from disk and apply it immediately.");
        AntiFireMessageStyle.itemStrong(sender, "/_antifire toggle <key> <value>");
        AntiFireMessageStyle.detail(sender, "does", "Update one config key in-game and save it immediately.");
        AntiFireMessageStyle.detail(sender, "values", "Use true/false for toggles and whole numbers for tick settings.");
        AntiFireMessageStyle.detail(sender, "console", "always allowed");
        sendKeyHelp(sender);
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

    private void sendValueDetail(CommandSender sender, String label, Object value) {
        if (value instanceof Boolean booleanValue) {
            AntiFireMessageStyle.state(sender, label, booleanValue);
            return;
        }
        AntiFireMessageStyle.detail(sender, label, value);
    }

    private void sendKeyHelp(CommandSender sender) {
        AntiFireMessageStyle.itemStrong(sender, "Toggle keys");
        for (AntiFireSetting setting : AntiFireSetting.values()) {
            AntiFireMessageStyle.detail(sender, setting.path(), setting.shortDescription());
        }
        AntiFireMessageStyle.detail(sender, "example", "/_antifire toggle allow-permanent-soul-fire true");
    }
}
