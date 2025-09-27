/*
 * Copyright Xanium Development (c) 2013-2018. All Rights Reserved.
 * Any code contained within this document, and any associated APIs with similar branding
 * are the sole property of Xanium Development. Distribution, reproduction, taking snippets or claiming
 * any contents as your own will break the terms of the license, and void any agreements with you, the third party.
 * Thank you.
 */

package com.minekarta.gemseconomyreloaded.commands;

import com.minekarta.gemseconomyreloaded.GemsEconomyReloaded;
import com.minekarta.gemseconomyreloaded.account.Account;
import com.minekarta.gemseconomyreloaded.currency.Currency;
import com.minekarta.gemseconomyreloaded.file.F;
import com.minekarta.gemseconomyreloaded.utils.ModernChat;
import com.minekarta.gemseconomyreloaded.utils.SchedulerUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class BalanceCommand implements CommandExecutor, TabCompleter {

    private final GemsEconomyReloaded plugin = GemsEconomyReloaded.getInstance();

    @Override
    public boolean onCommand(final CommandSender sender, Command command, String s, final String[] args) {
        SchedulerUtils.runAsync(() -> {
            if (!sender.hasPermission("gemseconomy.command.balance")) {
                ModernChat.send(sender, F.getNoPerms());
                return;
            }
            Account account;
            if (args.length == 0 && sender instanceof Player) {
                account = plugin.getAccountManager().getAccount((Player) sender);
            } else if (sender.hasPermission("gemseconomy.command.balance.other") && args.length == 1) {
                account = plugin.getAccountManager().getAccount(args[0]);
            } else {
                ModernChat.send(sender, F.getNoPerms());
                return;
            }
            if (account != null) {
                int currencies = plugin.getCurrencyManager().getCurrencies().size();

                if (currencies == 0) {
                    ModernChat.send(sender, F.getNoDefaultCurrency());
                } else if (currencies == 1) {
                    Currency currency = plugin.getCurrencyManager().getDefaultCurrency();
                    if (currency == null) {
                        ModernChat.send(sender, F.getRaw("Messages.balance.none").replace("{player}", account.getNickname()));
                        return;
                    }
                    double balance = account.getBalance(currency);
                    ModernChat.send(sender, F.getRaw("Messages.balance.current").replace("{player}", account.getDisplayName()).replace("{currencycolor}", "" + currency.getColor()).replace("{balance}", currency.format(balance)));
                } else {
                    ModernChat.send(sender, F.getRaw("Messages.balance.multiple").replace("{player}", account.getDisplayName()));
                    for (Currency currency : plugin.getCurrencyManager().getCurrencies()) {
                        double balance = account.getBalance(currency);
                        ModernChat.send(sender, F.getRaw("Messages.balance.list").replace("{currencycolor}", currency.getColor() + "").replace("{format}", currency.format(balance)));
                    }
                }
            } else {
                ModernChat.send(sender, F.getPlayerDoesNotExist());
            }
        });
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()), completions);
        }
        Collections.sort(completions);
        return completions;
    }
}




