package me.xanium.gemseconomy.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;

public class ModernChat {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    public static Component MMRC(String message) {
        return MINI_MESSAGE.deserialize(message);
    }

    public static void send(CommandSender sender, String message) {
        sender.sendMessage(MMRC(message));
    }

    public static void send(CommandSender sender, Component component) {
        sender.sendMessage(component);
    }
}
