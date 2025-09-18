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
package me.xanium.gemseconomy.commands;

import me.xanium.gemseconomy.GemsEconomy;
import me.xanium.gemseconomy.account.Account;
import me.xanium.gemseconomy.currency.Currency;
import me.xanium.gemseconomy.data.DataStorage;
import me.xanium.gemseconomy.file.F;
import me.xanium.gemseconomy.utils.ModernChat;
import me.xanium.gemseconomy.utils.SchedulerUtils;
import me.xanium.gemseconomy.utils.UtilServer;
import me.xanium.gemseconomy.utils.UtilString;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;

public class CurrencyCommand implements CommandExecutor {

    private final GemsEconomy plugin = GemsEconomy.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s124, String[] args) {
        SchedulerUtils.runAsync(() -> {
            if (!sender.hasPermission("gemseconomy.command.currency")) {
                ModernChat.send(sender, F.getNoPerms());
                return;
            }
            if (args.length == 0) {
                F.sendCurrencyUsage(sender);
            } else {
                String cmd = args[0];
                if (cmd.equalsIgnoreCase("create")) {
                    if (args.length == 3) {
                        String single = args[1];
                        String plural = args[2];
                        if (plugin.getCurrencyManager().currencyExist(single) || plugin.getCurrencyManager().currencyExist(plural)) {
                            ModernChat.send(sender, F.getRawPrefix() + "§cCurrency already exists.");
                            return;
                        }

                        plugin.getCurrencyManager().createNewCurrency(single, plural);
                        ModernChat.send(sender, F.getRawPrefix() + "§7Created currency: §a" + single);
                    } else {
                        ModernChat.send(sender, F.getCurrencyUsage_Create());
                    }
                } else if (cmd.equalsIgnoreCase("list")) {
                    ModernChat.send(sender, F.getRawPrefix() + "§7There are §f" + plugin.getCurrencyManager().getCurrencies().size() + "§7 currencies.");
                    for (Currency currency : plugin.getCurrencyManager().getCurrencies()) {
                        ModernChat.send(sender, "§a§l>> §e" + currency.getSingular());
                    }
                } else if (cmd.equalsIgnoreCase("view")) {
                    if (args.length == 2) {
                        Currency currency = plugin.getCurrencyManager().getCurrency(args[1]);
                        if (currency != null) {
                            ModernChat.send(sender, F.getRawPrefix() + "§7ID: §c" + currency.getUuid().toString());
                            ModernChat.send(sender, F.getRawPrefix() + "§7Singular: §a" + currency.getSingular() + "§7, Plural: §a" + currency.getPlural());
                            ModernChat.send(sender, F.getRawPrefix() + "§7Start Balance: §f" + currency.format(currency.getDefaultBalance()));
                            ModernChat.send(sender, F.getRawPrefix() + "§7Decimals: " + (currency.isDecimalSupported() ? "§aYes" : "§cNo"));
                            ModernChat.send(sender, F.getRawPrefix() + "§7Default: " + (currency.isDefaultCurrency() ? "§aYes" : "§cNo"));
                            ModernChat.send(sender, F.getRawPrefix() + "§7Payable: " + (currency.isPayable() ? "§aYes" : "§cNo"));
                            ModernChat.send(sender, F.getRawPrefix() + "§7Color: §f" + currency.getColor().toString());
                            ModernChat.send(sender, F.getRawPrefix() + "§7Rate: §f" + currency.getExchangeRate());
                        } else {
                            ModernChat.send(sender, F.getUnknownCurrency());
                        }
                    } else {
                        ModernChat.send(sender, F.getCurrencyUsage_View());
                    }
                } else if (cmd.equalsIgnoreCase("startbal")) {
                    if (args.length == 3) {
                        Currency currency = plugin.getCurrencyManager().getCurrency(args[1]);
                        if (currency != null) {
                            double amount;
                            block76:
                            {
                                if (currency.isDecimalSupported()) {
                                    try {
                                        amount = Double.parseDouble(args[2]);
                                        if (amount <= 0.0) {
                                            throw new NumberFormatException();
                                        }
                                        break block76;
                                    } catch (NumberFormatException ex) {
                                        ModernChat.send(sender, F.getUnvalidAmount());
                                        return;
                                    }
                                }
                                try {
                                    amount = Integer.parseInt(args[2]);
                                    if (amount <= 0.0) {
                                        throw new NumberFormatException();
                                    }
                                } catch (NumberFormatException ex) {
                                    ModernChat.send(sender, F.getUnvalidAmount());
                                    return;
                                }
                            }
                            currency.setDefaultBalance(amount);
                            ModernChat.send(sender, F.getRawPrefix() + "§7Starting balance for §f" + currency.getPlural() + " §7set: §a" + UtilString.format(currency.getDefaultBalance()));
                            plugin.getDataStore().saveCurrency(currency);
                        } else {
                            ModernChat.send(sender, F.getUnknownCurrency());
                        }
                    } else {
                        ModernChat.send(sender, F.getCurrencyUsage_Startbal());
                    }
                } else if (cmd.equalsIgnoreCase("color")) {
                    if (args.length == 3) {
                        Currency currency = plugin.getCurrencyManager().getCurrency(args[1]);
                        if (currency != null) {
                            NamedTextColor color = NamedTextColor.NAMES.value(args[2].toLowerCase());
                            if (color != null) {
                                currency.setColor(color);
                                ModernChat.send(sender, F.getRawPrefix() + "§7Color for §f" + currency.getPlural() + " §7updated: §f" + color);
                                plugin.getDataStore().saveCurrency(currency);
                            } else {
                                ModernChat.send(sender, F.getRawPrefix() + "§cInvalid chat color.");
                            }
                        } else {
                            ModernChat.send(sender, F.getUnknownCurrency());
                        }
                    } else {
                        ModernChat.send(sender, F.getCurrencyUsage_Color());
                    }
                } else if (cmd.equalsIgnoreCase("colorlist")) {
                    ModernChat.send(sender, String.join(", ", NamedTextColor.NAMES.keys()));
                } else if (cmd.equalsIgnoreCase("symbol")) {
                    if (args.length == 3) {
                        Currency currency = plugin.getCurrencyManager().getCurrency(args[1]);
                        if (currency != null) {
                            String symbol = args[2];
                            if (symbol.equalsIgnoreCase("remove")) {
                                currency.setSymbol(null);
                                ModernChat.send(sender, F.getRawPrefix() + "§7Currency symbol removed for §f" + currency.getPlural());
                                plugin.getDataStore().saveCurrency(currency);
                            } else if (symbol.length() == 1) {
                                currency.setSymbol(symbol);
                                ModernChat.send(sender, F.getRawPrefix() + "§7Currency symbol for §f" + currency.getPlural() + " §7updated: §a" + symbol);
                                plugin.getDataStore().saveCurrency(currency);
                            } else {
                                ModernChat.send(sender, F.getRawPrefix() + "§7Symbol must be 1 character, or remove it with \"remove\".");
                            }
                        } else {
                            ModernChat.send(sender, F.getUnknownCurrency());
                        }
                    } else {
                        ModernChat.send(sender, F.getCurrencyUsage_Symbol());
                    }
                } else if (cmd.equalsIgnoreCase("default")) {
                    if (args.length == 2) {
                        Currency currency = plugin.getCurrencyManager().getCurrency(args[1]);
                        if (currency != null) {
                            Currency c = plugin.getCurrencyManager().getDefaultCurrency();
                            if (c != null) {
                                c.setDefaultCurrency(false);
                                plugin.getDataStore().saveCurrency(c);
                            }
                            currency.setDefaultCurrency(true);
                            ModernChat.send(sender, F.getRawPrefix() + "§7Set default currency to §f" + currency.getPlural());
                            plugin.getDataStore().saveCurrency(currency);
                        } else {
                            ModernChat.send(sender, F.getUnknownCurrency());
                        }
                    } else {
                        ModernChat.send(sender, F.getCurrencyUsage_Default());
                    }
                } else if (cmd.equalsIgnoreCase("payable")) {
                    if (args.length == 2) {
                        Currency currency = plugin.getCurrencyManager().getCurrency(args[1]);
                        if (currency != null) {
                            currency.setPayable(!currency.isPayable());
                            ModernChat.send(sender, F.getRawPrefix() + "§7Toggled payability for §f" + currency.getPlural() + "§7: " + (currency.isPayable() ? "§aYes" : "§cNo"));
                            plugin.getDataStore().saveCurrency(currency);
                        } else {
                            ModernChat.send(sender, F.getUnknownCurrency());
                        }
                    } else {
                        ModernChat.send(sender, F.getCurrencyUsage_Payable());
                    }
                } else if (cmd.equalsIgnoreCase("decimals")) {
                    if (args.length == 2) {
                        Currency currency = plugin.getCurrencyManager().getCurrency(args[1]);
                        if (currency != null) {
                            currency.setDecimalSupported(!currency.isDecimalSupported());
                            ModernChat.send(sender, F.getRawPrefix() + "§7Toggled Decimal Support for §f" + currency.getPlural() + "§7: " + (currency.isDecimalSupported() ? "§aYes" : "§cNo"));
                            plugin.getDataStore().saveCurrency(currency);
                        } else {
                            ModernChat.send(sender, F.getUnknownCurrency());
                        }
                    } else {
                        ModernChat.send(sender, F.getCurrencyUsage_Decimals());
                    }
                } else if (cmd.equalsIgnoreCase("delete")) {
                    if (args.length == 2) {
                        Currency currency = plugin.getCurrencyManager().getCurrency(args[1]);
                        if (currency != null) {
                            plugin.getAccountManager().getAccounts().stream().filter(account -> account.getBalances().containsKey(currency)).forEach(account -> account.getBalances().remove(currency));
                            plugin.getDataStore().deleteCurrency(currency);
                            plugin.getCurrencyManager().getCurrencies().remove(currency);
                            ModernChat.send(sender, F.getRawPrefix() + "§7Deleted currency: §a" + currency.getPlural());
                        } else {
                            ModernChat.send(sender, F.getUnknownCurrency());
                        }
                    } else {
                        ModernChat.send(sender, F.getCurrencyUsage_Delete());
                    }
                } else if (cmd.equalsIgnoreCase("setrate")) {
                    if (args.length == 3) {
                        Currency currency = plugin.getCurrencyManager().getCurrency(args[1]);
                        if (currency != null) {
                            double amount;

                            try {
                                amount = Double.parseDouble(args[2]);
                                if (amount <= 0.0) {
                                    throw new NumberFormatException();
                                }
                            } catch (NumberFormatException ex) {
                                ModernChat.send(sender, F.getUnvalidAmount());
                                return;
                            }
                            currency.setExchangeRate(amount);
                            plugin.getDataStore().saveCurrency(currency);
                            ModernChat.send(sender, F.getRaw("Messages.exchange_rate_set").replace("{currencycolor}", currency.getColor().toString()).replace("{currency}", currency.getPlural()).replace("{amount}", String.valueOf(amount)));
                        } else {
                            ModernChat.send(sender, F.getUnknownCurrency());
                        }
                    } else {
                        ModernChat.send(sender, F.getCurrencyUsage_Rate());
                    }
                } else if (cmd.equalsIgnoreCase("convert")) {
                    if (args.length == 2) {
                        String method = args[1];
                        DataStorage current = plugin.getDataStore();
                        DataStorage ds = DataStorage.getMethod(method);

                        if (current == null) {
                            ModernChat.send(sender, F.getRawPrefix() + "§7Current Data Store is null. Did something go wrong on startup?");
                            return;
                        }

                        if (ds != null) {
                            if (current.getName().equalsIgnoreCase(ds.getName())) {
                                ModernChat.send(sender, F.getRawPrefix() + "§7You can't convert to the same datastore.");
                                return;
                            }

                            plugin.getConfig().set("storage", ds.getName());
                            plugin.saveConfig();

                            ModernChat.send(sender, F.getRawPrefix() + "§aLoading data..");
                            plugin.getAccountManager().getAccounts().clear();

                            ModernChat.send(sender, F.getRawPrefix() + "§aStored accounts.");
                            ArrayList<Account> offline = new ArrayList<>(plugin.getDataStore().getOfflineAccounts());
                            UtilServer.consoleLog("Stored Accounts: " + offline.size());
                            if (GemsEconomy.getInstance().isDebug()) {
                                for (Account account : offline) {
                                    UtilServer.consoleLog("Account: " + account.getNickname() + " (" + account.getUuid().toString() + ")");
                                    for (Currency currency : account.getBalances().keySet()) {
                                        UtilServer.consoleLog("Balance: " + currency.format(account.getBalance(currency)));
                                    }
                                }
                            }

                            ArrayList<Currency> currencies = new ArrayList<>(plugin.getCurrencyManager().getCurrencies());
                            ModernChat.send(sender, F.getRawPrefix() + "§aStored currencies.");
                            plugin.getCurrencyManager().getCurrencies().clear();

                            if (plugin.isDebug()) {
                                for (Currency c : currencies) {
                                    UtilServer.consoleLog("Currency: " + c.getSingular() + "(" + c.getPlural() + "): " + c.format(1000000));
                                }
                            }

                            ModernChat.send(sender, F.getRawPrefix() + "§aSwitching from §f" + current.getName() + " §ato §f" + ds.getName() + "§a.");

                            if (ds.getName().equalsIgnoreCase("yaml")) {
                                SchedulerUtils.run(() -> {
                                    File data = new File(GemsEconomy.getInstance().getDataFolder() + File.separator + "data.yml");
                                    if (data.exists()) {
                                        data.delete();
                                    }
                                });
                            }

                            if (plugin.getDataStore() != null) {
                                plugin.getDataStore().close();

                                ModernChat.send(sender, F.getRawPrefix() + "§aDataStore is closed. Plugin is essentially dead now.");
                            }

                            plugin.initializeDataStore(ds.getName(), false);
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }

                            ModernChat.send(sender, F.getRawPrefix() + "§aInitialized " + ds.getName() + " Data Store. Check console for wrong username/password if using mysql.");
                            ModernChat.send(sender, F.getRawPrefix() + "§aIf there are sql login errors, you can just retry after you have fixed the credentials, changed the datastore back to what you were using and restarted the server!");

                            if (plugin.getDataStore().getName() != null) {
                                for (Currency c : currencies) {
                                    Currency newCurrency = new Currency(c.getUuid(), c.getSingular(), c.getPlural());
                                    newCurrency.setExchangeRate(c.getExchangeRate());
                                    newCurrency.setDefaultCurrency(c.isDefaultCurrency());
                                    newCurrency.setSymbol(c.getSymbol());
                                    newCurrency.setColor(c.getColor());
                                    newCurrency.setDecimalSupported(c.isDecimalSupported());
                                    newCurrency.setPayable(c.isPayable());
                                    newCurrency.setDefaultBalance(c.getDefaultBalance());
                                    plugin.getDataStore().saveCurrency(newCurrency);
                                }
                                ModernChat.send(sender, F.getRawPrefix() + "§aSaved currencies to storage.");
                                plugin.getDataStore().loadCurrencies();
                                ModernChat.send(sender, F.getRawPrefix() + "§aLoaded all currencies as usual.");

                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException ex) {
                                    ex.printStackTrace();
                                }

                                for (Account a : offline) {
                                    plugin.getDataStore().saveAccount(a);
                                }
                                ModernChat.send(sender, F.getRawPrefix() + "§aAll accounts saved to storage.");

                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException ex) {
                                    ex.printStackTrace();
                                }

                                for (Player players : Bukkit.getOnlinePlayers()) {
                                    plugin.getDataStore().loadAccount(players.getUniqueId(), account -> plugin.getAccountManager().add(account));
                                }
                                ModernChat.send(sender, F.getRawPrefix() + "§aLoaded all accounts for online players.");
                                ModernChat.send(sender, F.getRawPrefix() + "§aData storage conversion is done.");
                            }
                        } else {
                            ModernChat.send(sender, F.getRawPrefix() + "§cData Storing method not found.");
                        }
                    } else {
                        ModernChat.send(sender, F.getCurrencyUsage_Convert());
                    }
                } else if (cmd.equalsIgnoreCase("backend")) {
                    if (args.length == 2) {
                        String method = args[1];
                        DataStorage current = plugin.getDataStore();
                        DataStorage ds = DataStorage.getMethod(method);

                        if (current == null) {
                            ModernChat.send(sender, F.getRawPrefix() + "§7Current Data Store is null. Did something go wrong on startup?");
                            return;
                        }

                        if (ds != null) {
                            if (current.getName().equalsIgnoreCase(ds.getName())) {
                                ModernChat.send(sender, F.getRawPrefix() + "§7You can't convert to the same datastore.");
                                return;
                            }


                            plugin.getConfig().set("storage", ds.getName());
                            plugin.saveConfig();

                            ModernChat.send(sender, F.getRawPrefix() + "§aSaving data and closing up...");

                            if (plugin.getDataStore() != null) {
                                plugin.getDataStore().close();

                                plugin.getAccountManager().getAccounts().clear();
                                plugin.getCurrencyManager().getCurrencies().clear();

                                ModernChat.send(sender, F.getRawPrefix() + "§aSuccessfully shutdown. Booting..");
                            }

                            ModernChat.send(sender, F.getRawPrefix() + "§aSwitching from §f" + current.getName() + " §ato §f" + ds.getName() + "§a.");

                            plugin.initializeDataStore(ds.getName(), true);
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }

                            for (Player players : Bukkit.getOnlinePlayers()) {
                                plugin.getDataStore().loadAccount(players.getUniqueId(), account -> plugin.getAccountManager().add(account));
                            }
                            ModernChat.send(sender, F.getRawPrefix() + "§aLoaded all accounts for online players.");
                        }
                    } else {
                        ModernChat.send(sender, F.getCurrencyUsage_Backend());
                    }
                } else {
                    ModernChat.send(sender, F.getUnknownSubCommand());
                }
            }

        });
        return true;
    }

}

