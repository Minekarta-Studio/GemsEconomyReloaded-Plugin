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
import com.minekarta.gemseconomyreloaded.event.GemsPayEvent;
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

public class PayCommand implements CommandExecutor, TabCompleter {

    private final GemsEconomyReloaded plugin = GemsEconomyReloaded.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s13542415, String[] args) {
        if (!(sender instanceof Player)) {
            ModernChat.send(sender, F.getNoConsole());
            return true;
        }
        SchedulerUtils.runAsync(() -> {
            if (!sender.hasPermission("gemseconomy.command.pay")) {
                ModernChat.send(sender, F.getNoPerms());
                return;
            }
            if (args.length < 2) {
                ModernChat.send(sender, F.getPayUsage());
                return;
            }
            if (plugin.getCurrencyManager().getDefaultCurrency() == null) {
                ModernChat.send(sender, F.getNoDefaultCurrency());
                return;
            }

            Currency currency = plugin.getCurrencyManager().getDefaultCurrency();
            if (args.length == 3) {
                currency = plugin.getCurrencyManager().getCurrency(args[2]);
            }
            if (currency != null) {
                double amount;

                if (!currency.isPayable()) {
                    ModernChat.send(sender, F.getRaw("Messages.currencyNotPayable").replace("{currencycolor}", "" + currency.getColor()).replace("{currency}", currency.getPlural()));
                    return;
                }
                if (!sender.hasPermission("gemseconomy.command.pay." + currency.getPlural().toLowerCase()) && !sender.hasPermission("gemseconomy.command.pay." + currency.getSingular().toLowerCase())) {
                    ModernChat.send(sender, F.getRaw("Messages.payNoPermission").replace("{currencycolor}", "" + currency.getColor()).replace("{currency}", currency.getPlural()));
                    return;
                }
                if (currency.isDecimalSupported()) {
                    try {
                        amount = Double.parseDouble(args[1]);
                        if (amount <= 0.0) {
                            throw new NumberFormatException();
                        }
                    } catch (NumberFormatException ex) {
                        ModernChat.send(sender, F.getUnvalidAmount());
                        return;
                    }
                } else {
                    try {
                        amount = Integer.parseInt(args[1]);
                        if (amount <= 0) {
                            throw new NumberFormatException();
                        }
                    } catch (NumberFormatException ex) {
                        ModernChat.send(sender, F.getUnvalidAmount());
                        return;
                    }
                }
                Account account = plugin.getAccountManager().getAccount((Player) sender);
                if (account != null) {
                    Account target = plugin.getAccountManager().getAccount(args[0]);
                    if (target != null) {
                        if (!target.getUuid().equals(account.getUuid())) {
                            if (target.canReceiveCurrency()) {
                                if (account.hasEnough(currency, amount)) {
                                    GemsPayEvent event = new GemsPayEvent(currency, account, target, amount);
                                    SchedulerUtils.run(() -> Bukkit.getPluginManager().callEvent(event));
                                    if (event.isCancelled()) return;

                                    double accBal = account.getBalance(currency) - amount;
                                    double tarBal = target.getBalance(currency) + amount;
                                    account.modifyBalance(currency, accBal, true);
                                    target.modifyBalance(currency, tarBal, true);
                                    GemsEconomyReloaded.getInstance().getEconomyLogger().log("[PAYMENT] " + account.getDisplayName() + " (New bal: " + currency.format(accBal) + ") -> paid " + target.getDisplayName() + " (New bal: " + currency.format(tarBal) + ") - An amount of " + currency.format(amount));

                                    if (Bukkit.getPlayer(target.getUuid()) != null) {
                                        ModernChat.send(Bukkit.getPlayer(target.getUuid()), F.getRaw("Messages.paid").replace("{currencycolor}", currency.getColor() + "").replace("{amount}", currency.format(amount)).replace("{player}", sender.getName()));
                                    }
                                    ModernChat.send(sender, F.getRaw("Messages.payer").replace("{currencycolor}", currency.getColor() + "").replace("{amount}", currency.format(amount)).replace("{player}", target.getDisplayName()));
                                } else {
                                    ModernChat.send(sender, F.getRaw("Messages.insufficientFunds").replace("{currencycolor}", "" + currency.getColor()).replace("{currency}", currency.getPlural()));
                                }
                            } else {
                                ModernChat.send(sender, F.getRaw("Messages.cannotReceiveMoney").replace("{player}", target.getDisplayName()));
                            }
                        } else {
                            ModernChat.send(sender, F.getPayYourself());
                        }
                    } else {
                        ModernChat.send(sender, F.getPlayerDoesNotExist());
                    }
                } else {
                    ModernChat.send(sender, F.getAccountMissing());
                }
            } else {
                ModernChat.send(sender, F.getUnknownCurrency());
            }
        });
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()), completions);
        } else if (args.length == 3) {
            StringUtil.copyPartialMatches(args[2], plugin.getCurrencyManager().getCurrencies().stream().map(Currency::getPlural).collect(Collectors.toList()), completions);
        }
        Collections.sort(completions);
        return completions;
    }
}

