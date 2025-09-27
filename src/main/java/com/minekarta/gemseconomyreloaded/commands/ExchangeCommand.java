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

public class ExchangeCommand implements CommandExecutor, TabCompleter {

    private final GemsEconomyReloaded plugin = GemsEconomyReloaded.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String v21315, String[] args) {
        SchedulerUtils.runAsync(() -> {
            if (!sender.hasPermission("gemseconomy.command.exchange")) {
                ModernChat.send(sender, F.getNoPerms());
                return;
            }

            if (args.length == 0) {
                F.getExchangeHelp(sender);
            } else if (args.length == 3) {

                if (!sender.hasPermission("gemseconomy.command.exchange.preset")) {
                    ModernChat.send(sender, F.getExchangeNoPermPreset());
                    return;
                }
                Currency toExchange = plugin.getCurrencyManager().getCurrency(args[0]);
                Currency toReceive = plugin.getCurrencyManager().getCurrency(args[2]);
                double amount;

                if (toExchange != null && toReceive != null) {
                    if (toReceive.isDecimalSupported()) {
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
                            if (amount <= 0.0) {
                                throw new NumberFormatException();
                            }
                        } catch (NumberFormatException ex) {
                            ModernChat.send(sender, F.getUnvalidAmount());
                            return;
                        }
                    }
                    Account account = plugin.getAccountManager().getAccount(sender.getName());
                    if (account != null) {
                        if (account.convert(toExchange, amount, toReceive, -1)) {
                            ModernChat.send(sender, F.getRaw("Messages.exchange_success")
                                    .replace("{currencycolor}", "" + toExchange.getColor())
                                    .replace("{ex_curr}", toExchange.format(amount))
                                    .replace("{currencycolor2}", "" + toReceive.getColor())
                                    .replace("{re_curr}", toReceive.getPlural()));
                        }
                    }
                } else {
                    ModernChat.send(sender, F.getUnknownCurrency());
                }

            } else if (args.length == 4) {
                if (!sender.hasPermission("gemseconomy.command.exchange.custom")) {
                    ModernChat.send(sender, F.getExchangeNoPermCustom());
                    return;
                }
                Currency toExchange = plugin.getCurrencyManager().getCurrency(args[0]);
                Currency toReceive = plugin.getCurrencyManager().getCurrency(args[2]);
                double toExchangeAmount = 0.0;
                double toReceiveAmount = 0.0;

                if (toExchange != null && toReceive != null) {
                    if (toExchange.isDecimalSupported() || toReceive.isDecimalSupported()) {
                        try {
                            toExchangeAmount = Double.parseDouble(args[1]);
                            toReceiveAmount = Double.parseDouble(args[3]);
                            if (toExchangeAmount <= 0.0 || toReceiveAmount <= 0) {
                                throw new NumberFormatException();
                            }
                        } catch (NumberFormatException ex) {
                            ModernChat.send(sender, F.getUnvalidAmount());
                        }
                    } else {
                        try {
                            toExchangeAmount = Integer.parseInt(args[1]);
                            toReceiveAmount = Integer.parseInt(args[3]);
                            if (toExchangeAmount <= 0.0 || toReceiveAmount <= 0) {
                                throw new NumberFormatException();
                            }
                        } catch (NumberFormatException ex) {
                            ModernChat.send(sender, F.getUnvalidAmount());
                        }
                    }
                    Account account = plugin.getAccountManager().getAccount(sender.getName());
                    if (account != null) {
                        if (account.convert(toExchange, toExchangeAmount, toReceive, toReceiveAmount)) {
                            ModernChat.send(sender, F.getRaw("Messages.exchange_success_custom")
                                    .replace("{currencycolor}", "" + toExchange.getColor())
                                    .replace("{currEx}", toExchange.format(toExchangeAmount))
                                    .replace("{currencycolor2}", "" + toReceive.getColor())
                                    .replace("{receivedCurr}", toReceive.format(toReceiveAmount)));
                        }
                    }
                } else {
                    ModernChat.send(sender, F.getUnknownCurrency());
                }
            } else if (args.length == 5) {
                if (!sender.hasPermission("gemseconomy.command.exchange.custom.other")) {
                    ModernChat.send(sender, F.getExchangeNoPermCustom());
                    return;
                }
                Account account = plugin.getAccountManager().getAccount(args[0]);
                if (account == null) {
                    ModernChat.send(sender, F.getPlayerDoesNotExist());
                    return;
                }
                Currency toExchange = plugin.getCurrencyManager().getCurrency(args[1]);
                Currency toReceive = plugin.getCurrencyManager().getCurrency(args[3]);
                double toExchangeAmount = 0.0;
                double toReceiveAmount = 0.0;

                if (toExchange != null && toReceive != null) {
                    if (toExchange.isDecimalSupported() || toReceive.isDecimalSupported()) {
                        try {
                            toExchangeAmount = Double.parseDouble(args[2]);
                            toReceiveAmount = Double.parseDouble(args[4]);
                            if (toExchangeAmount <= 0.0 || toReceiveAmount <= 0) {
                                throw new NumberFormatException();
                            }
                        } catch (NumberFormatException ex) {
                            ModernChat.send(sender, F.getUnvalidAmount());
                        }
                    } else {
                        try {
                            toExchangeAmount = Integer.parseInt(args[2]);
                            toReceiveAmount = Integer.parseInt(args[4]);
                            if (toExchangeAmount <= 0.0 || toReceiveAmount <= 0) {
                                throw new NumberFormatException();
                            }
                        } catch (NumberFormatException ex) {
                            ModernChat.send(sender, F.getUnvalidAmount());
                        }
                    }

                    if (account.convert(toExchange, toExchangeAmount, toReceive, toReceiveAmount)) {
                        ModernChat.send(sender, F.getRaw("Messages.exchange_success_custom_other")
                                .replace("{player}", account.getDisplayName())
                                .replace("{currencycolor}", "" + toExchange.getColor())
                                .replace("{currEx}", toExchange.format(toExchangeAmount))
                                .replace("{currencycolor2}", "" + toReceive.getColor())
                                .replace("{receivedCurr}", toReceive.format(toReceiveAmount)));
                    }

                } else {
                    ModernChat.send(sender, F.getUnknownCurrency());
                }
            } else {
                ModernChat.send(sender, F.getUnknownSubCommand());
            }
        });
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final List<String> completions = new ArrayList<>();
        List<String> playerNames = Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
        List<String> currencyNames = plugin.getCurrencyManager().getCurrencies().stream().map(Currency::getPlural).collect(Collectors.toList());

        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], playerNames, completions);
            StringUtil.copyPartialMatches(args[0], currencyNames, completions);
        } else if (args.length == 2) {
            if (playerNames.contains(args[0])) {
                StringUtil.copyPartialMatches(args[1], currencyNames, completions);
            }
        } else if (args.length == 3) {
            if (currencyNames.contains(args[0])) {
                StringUtil.copyPartialMatches(args[2], currencyNames, completions);
            }
        } else if (args.length == 4) {
            if (playerNames.contains(args[0])) {
                StringUtil.copyPartialMatches(args[3], currencyNames, completions);
            }
        }

        Collections.sort(completions);
        return completions;
    }
}
