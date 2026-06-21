package com.mrfloris.antifire;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;

final class AntiFireMessageStyle {
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final String BODY = "<color:#f2f5f7>";
    private static final String MUTED = "<color:#7d8790>";
    private static final String TITLE = "<color:#ffc8dd>";
    private static final String ACCENT = "<color:#bde0fe>";
    private static final String SUCCESS = "<color:#caffbf>";
    private static final String ERROR = "<color:#ffb3c1>";
    private static final String PREFIX = MUTED + "[<color:#ffd6a5>1MB AntiFire</color>] </color>" + BODY;

    private AntiFireMessageStyle() {
    }

    static void header(CommandSender sender, String title) {
        send(sender, PREFIX + TITLE + escape(title) + "</color>");
    }

    static void success(CommandSender sender, String message) {
        send(sender, PREFIX + SUCCESS + escape(message) + "</color>");
    }

    static void error(CommandSender sender, String message) {
        send(sender, PREFIX + ERROR + escape(message) + "</color>");
    }

    static void note(CommandSender sender, String message) {
        send(sender, BODY + escape(message) + "</color>");
    }

    static void itemStrong(CommandSender sender, String value) {
        send(sender, MUTED + "-</color> " + ACCENT + escape(value) + "</color>");
    }

    static void detail(CommandSender sender, String label, Object value) {
        send(sender, "  " + ACCENT + escape(label) + ":</color> " + BODY + escape(String.valueOf(value)) + "</color>");
    }

    static void state(CommandSender sender, String label, boolean enabled) {
        String state = enabled
                ? "<color:#caffbf>enabled</color>"
                : "<color:#ffb3c1>disabled</color>";
        send(sender, "  " + ACCENT + escape(label) + ":</color> " + state);
    }

    static void send(CommandSender sender, String message) {
        sender.sendMessage(component(message));
    }

    static String escape(String value) {
        return MINI_MESSAGE.escapeTags(value == null ? "" : value);
    }

    private static Component component(String message) {
        return MINI_MESSAGE.deserialize(message == null ? "" : message);
    }
}
