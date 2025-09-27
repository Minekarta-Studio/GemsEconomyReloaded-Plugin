/*
 * Copyright Xanium Development (c) 2013-2018. All Rights Reserved.
 * Any code contained within this document, and any associated APIs with similar branding
 * are the sole property of Xanium Development. Distribution, reproduction, taking snippets or claiming
 * any contents as your own will break the terms of the license, and void any agreements with you, the third party.
 * Thank you.
 */

/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.scheduler.BukkitTask
 */
package com.minekarta.gemseconomyreloaded.commands;

import com.minekarta.gemseconomyreloaded.GemsEconomyReloaded;
import com.minekarta.gemseconomyreloaded.currency.CachedTopListEntry;
import com.minekarta.gemseconomyreloaded.currency.Currency;
import com.minekarta.gemseconomyreloaded.file.F;
import com.minekarta.gemseconomyreloaded.utils.ModernChat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class BalanceTopCommand implements CommandExecutor, TabCompleter {

    private final GemsEconomyReloaded plugin = GemsEconomyReloaded.getInstance();
    private final int ACCOUNTS_PER_PAGE = 10;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s670, String[] args) {
        if (!sender.hasPermission("gemseconomy.command.baltop")) {
            ModernChat.send(sender, F.getNoPerms());
            return true;
        }
        if (!plugin.getDataStore().isTopSupported()) {
            ModernChat.send(sender, F.getRaw("Messages.balance_top.nosupport").replace("{storage}", plugin.getDataStore().getName()));
            return true;
        }

        Currency currency = plugin.getCurrencyManager().getDefaultCurrency();
        int page = 1;
        if (args.length > 0) {
            currency = plugin.getCurrencyManager().getCurrency(args[0]);
            if (currency == null) {
                ModernChat.send(sender, F.getUnknownCurrency());
                return true;
            }

            if (args.length == 2) {
                try {
                    page = Integer.parseInt(args[1]);
                } catch (NumberFormatException ex) {
                    ModernChat.send(sender, F.getUnvalidPage());
                    return true;
                }
            }
        }
        if (page < 1) {
            page = 1;
        }
        int offset = 10 * (page - 1);
        final int pageNumber = page;
        final Currency curr = currency;

        if (currency != null) {
            plugin.getDataStore().getTopList(currency, offset, ACCOUNTS_PER_PAGE, cachedTopListEntries -> {
                ModernChat.send(sender, F.getRaw("Messages.balance_top.header")
                        .replace("{currencycolor}", "" + curr.getColor())
                        .replace("{currencyplural}", curr.getPlural())
                        .replace("{page}", String.valueOf(pageNumber)));

                int num = (10 * (pageNumber - 1)) + 1;
                for (CachedTopListEntry entry : cachedTopListEntries) {
                    double balance = entry.getAmount();
                    ModernChat.send(sender, F.getRaw("Messages.balance_top.balance").replace("{number}", String.valueOf(num)).replace("{currencycolor}", "" + curr.getColor())
                            .replace("{player}", entry.getName()).replace("{balance}", curr.format(balance)));
                    num++;
                }
                if (cachedTopListEntries.isEmpty()) {
                    ModernChat.send(sender, F.getBalanceTopEmpty());
                } else {
                    ModernChat.send(sender, F.getRaw("Messages.balance_top.next").replace("{currencycolor}", "" + curr.getColor()).replace("{currencyplural}", curr.getPlural()).replace("{page}", String.valueOf((pageNumber + 1))));
                }
            });
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], plugin.getCurrencyManager().getCurrencies().stream().map(Currency::getPlural).collect(Collectors.toList()), completions);
        }
        Collections.sort(completions);
        return completions;
    }
}

