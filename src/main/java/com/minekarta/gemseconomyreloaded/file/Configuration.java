/*
 * Copyright Xanium Development (c) 2013-2018. All Rights Reserved.
 * Any code contained within this document, and any associated APIs with similar branding
 * are the sole property of Xanium Development. Distribution, reproduction, taking snippets or claiming
 * any contents as your own will break the terms of the license, and void any agreements with you, the third party.
 * Thank you.
 */

package com.minekarta.gemseconomyreloaded.file;

import com.minekarta.gemseconomyreloaded.GemsEconomyReloaded;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;

public class Configuration {

    private GemsEconomyReloaded plugin;

    public Configuration(GemsEconomyReloaded plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("deprecation")
    public void loadDefaultConfig() {

        FileConfiguration config = plugin.getConfig();

        config.options().header(plugin.getDescription().getName()
                + "\n"
                + "Version: " + plugin.getDescription().getVersion()
                + "\nGemsEconomyReloaded Main Configuration file."
                + "\n"
                + "Developer(s): " + plugin.getDescription().getAuthors()
                + "\n\n"
                + "You have three valid storage methods, yaml, mysql or sqlite. If you choose mysql you would have to enter the database credentials down below."
                + "\n"
                + "All messages below are configurable, I hope you use them because it took 1 hour to make all of them into the plugin and configurable.");

        String path = "Messages.";

        config.addDefault("storage", "yaml");
        config.addDefault("debug", false);
        config.addDefault("vault", false);
        config.addDefault("transaction_log", true);

        config.addDefault("mysql.database", "minecraft");
        config.addDefault("mysql.tableprefix", "gemseconomy");
        config.addDefault("mysql.host", "localhost");
        config.addDefault("mysql.port", 3306);
        config.addDefault("mysql.username", "root");
        config.addDefault("mysql.password", "password");

      //  config.addDefault("sqlite.file", "database.sqlite");

        config.addDefault("cheque.material", Material.PAPER.toString());
        config.addDefault("cheque.name", "<green>Bank Note");
        config.addDefault("cheque.lore", Arrays.asList("<gray>Worth: {value}.", "<gray><i>Written by {player}"));
        config.addDefault("cheque.console_name", "Console");
        config.addDefault("cheque.enabled", true);

        config.addDefault(path + "prefix", "<#4CAF50><b>GemsEconomyReloaded</b> <gray>»</gray> ");
        config.addDefault(path + "nopermission", "<gray>You don't have permission to do this.");
        config.addDefault(path + "noconsole", "<gray>Console cannot do this.");
        config.addDefault(path + "invalidamount", "<gray>Not a valid amount.");
        config.addDefault(path + "invalidpage", "<gray>Not a valid page number.");
        config.addDefault(path + "pay_yourself", "<gray>You can't pay yourself.");
        config.addDefault(path + "player_is_null", "<gray>The specified player does not exist.");
        config.addDefault(path + "unknownCurrency", "<gray>Unknown Currency.");
        config.addDefault(path + "unknownCommand", "<gray>Unknown sub-command.");
        config.addDefault(path + "noDefaultCurrency", "<gray>No default currency.");
        config.addDefault(path + "currencyExists", "<gray>Currency already exists.");
        config.addDefault(path + "accountMissing", "<gray>Your account is missing. Please relog the server.");
        config.addDefault(path + "cannotReceiveMoney", "<green>{player}</green><gray> can't receive money.");
        config.addDefault(path + "insufficientFunds", "<gray>You don't have enough {currencycolor}{currency}</gray>!");
        config.addDefault(path + "targetInsufficientFunds", "<yellow>{target} </yellow><gray>don't have enough {currencycolor}{currency}</gray>!");
        config.addDefault(path + "paid", "<gray>You were paid {currencycolor}{amount} <gray>from <green>{player}</green>.");
        config.addDefault(path + "payer", "<gray>You paid {currencycolor}{amount} <gray>to <green>{player}</green>.");
        config.addDefault(path + "payNoPermission", "<gray>You don't have permission to pay {currencycolor}{currency}</gray>.");
        config.addDefault(path + "currencyNotPayable", "{currencycolor}{currency} <gray>is not payable.");
        config.addDefault(path + "add", "<gray>You gave <green>{player}</green>: {currencycolor}{amount}. ");
        config.addDefault(path + "take", "<gray>You took {currencycolor}{amount} <gray>from <green>{player}</green>.");
        config.addDefault(path + "set", "<gray>You set <green>{player}</green>'s balance to {currencycolor}{amount}</gray>.");

        config.addDefault(path + "exchange_rate_set", "<gray>Set the exchange rate for {currencycolor}{currency} <gray>to <green>{amount}</green>.");
        config.addDefault(path + "exchange_success_custom_other", "<gray>Successfully exchanged {currencycolor}({currEx}) <gray>for {currencycolor2}{receivedCurr}<gray> to player {player}</gray>.");
        config.addDefault(path + "exchange_success_custom", "<gray>Successfully exchanged {currencycolor}({currEx}) <gray>for {currencycolor2}{receivedCurr}</gray>.");
        config.addDefault(path + "exchange_success", "<gray>Successfully exchanged {currencycolor}{ex_curr} <gray>for equivalent value in {currencycolor2}{re_curr}</gray>.");
        config.addDefault(path + "exchange_command.no_perms.preset", "<gray>You don't have permission to exchange currencies with a preset rate.");
        config.addDefault(path + "exchange_command.no_perms.custom", "<gray>You don't have permission to exchange currencies with a custom rate.");

        config.addDefault(path + "balance.current", "<green>{player}</green><gray>'s balance is: {currencycolor}{balance}");
        config.addDefault(path + "balance.multiple", "<green>{player}</green><gray>'s balances:");
        config.addDefault(path + "balance.list", "<green><b>»</b></green> {currencycolor}{format}");
        config.addDefault(path + "balance.none", "<gray>No balances to show for <red>{player}</red>.</gray>");

        config.addDefault(path + "balance_top.balance", "<green><b>-></b></green> {number}. {currencycolor}{player} <gray>- {currencycolor}{balance}");
        config.addDefault(path + "balance_top.header", "<white>-----</white> {currencycolor} Top Balances for {currencyplural} <gray>(Page {page})</gray><white> -----");
        config.addDefault(path + "balance_top.empty", "<gray>No accounts to display.");
        config.addDefault(path + "balance_top.next", "{currencycolor}/gbaltop {currencyplural} {page} <gray>for more.");
        config.addDefault(path + "balance_top.nosupport", "<green>{storage} <gray>doesn't support /baltop.");

        config.addDefault(path + "cheque.success", "<gray>Cheque successfully written.");
        config.addDefault(path + "cheque.redeemed", "<gray>Cheque has been cashed in.");
        config.addDefault(path + "cheque.invalid", "<gray>This is not a valid cheque.");

        config.addDefault(path + "help.eco_command", Arrays.asList(
                "{prefix}<yellow><b>Economy Help</b></yellow>",
                "<dark_green><b>»</b></dark_green> <green>/eco give <user> <amount> [currency]</green> <dark_gray>-</dark_gray> <gray>Give a player an amount of a currency.</gray>",
                "<dark_green><b>»</b></dark_green> <green>/eco take <user> <amount> [currency]</green> <dark_gray>-</dark_gray> <gray>Take an amount of a currency from a player.</gray>",
                "<dark_green><b>»</b></dark_green> <green>/eco set <user> <amount> [currency]</green> <dark_gray>-</dark_gray> <gray>Set a players amount of a currency.</gray>"));

        config.addDefault(path + "help.exchange_command", Arrays.asList(
                "{prefix}<aqua><b>Exchange Help</b></aqua>",
                "<dark_green><b>»</b></dark_green> <green>/exchange <account> <currency_to_exchange> <amount> <currency_to_receive> <amount></green> <dark_gray>-</dark_gray> <gray>Exchange between currencies with a custom rate for an account.</gray>",
                "<dark_green><b>»</b></dark_green> <green>/exchange <currency_to_exchange> <amount> <currency_to_receive> <amount></green> <dark_gray>-</dark_gray> <gray>Exchange between currencies with a custom rate.</gray>",
                "<dark_green><b>»</b></dark_green> <green>/exchange <currency_to_exchange> <amount> <currency_to_receive></green> <dark_gray>-</dark_gray> <gray>Exchange with a pre-set exchange rate.</gray>"));

        config.addDefault(path + "usage.pay_command", "<dark_green><b>»</b></dark_green> <green>/pay <user> <amount> [currency]</green> <dark_gray>-</dark_gray> <gray>Pay the specified user the specified amount.</gray>");
        config.addDefault(path + "usage.give_command", "<dark_green><b>»</b></dark_green> <green>/eco give <user> <amount> [currency]</green> <dark_gray>-</dark_gray> <gray>Give a player an amount of a currency.</gray>");
        config.addDefault(path + "usage.take_command", "<dark_green><b>»</b></dark_green> <green>/eco take <user> <amount> [currency]</green> <dark_gray>-</dark_gray> <gray>Take an amount of a currency from a player.</gray>");
        config.addDefault(path + "usage.set_command", "<dark_green><b>»</b></dark_green> <green>/eco set <user> <amount> [currency]</green> <dark_gray>-</dark_gray> <gray>Set a players amount of a currency.</gray>");

        config.addDefault(path + "help.cheque_command", Arrays.asList("{prefix}<yellow><b>Cheque Help</b></yellow>",
                "<dark_green><b>»</b></dark_green> <green>/cheque write <amount> [currency]</green> <dark_gray>-</dark_gray> <gray>Write a cheque with a specified amount and currency.</gray>",
                "<dark_green><b>»</b></dark_green> <green>/cheque redeem</green> <dark_gray>-</dark_gray> <gray>Redeem the cheque.</gray>"));

        config.addDefault(path + "help.currency_command", Arrays.asList("{prefix}<yellow><b>Currency Help</b></yellow>",
                "<dark_green><b>»</b></dark_green> <green>/currency create <singular> <plural></green> <dark_gray>-</dark_gray> <gray>Create a currency.</gray>",
                "<dark_green><b>»</b></dark_green> <green>/currency delete <plural></green> <dark_gray>-</dark_gray> <gray>Delete a currency.</gray>",
                "<dark_green><b>»</b></dark_green> <green>/currency convert <method></green> <dark_gray>-</dark_gray> <gray>Convert storage method. WARN: Take backups first and make sure the storage you are switching to is empty!</gray>",
                "<dark_green><b>»</b></dark_green> <green>/currency backend <method></green> <dark_gray>-</dark_gray> <gray>Switch backend. This does not convert.</gray>",
                "<dark_green><b>»</b></dark_green> <green>/currency view <plural></green> <dark_gray>-</dark_gray> <gray>View information about a currency.</gray>",
                "<dark_green><b>»</b></dark_green> <green>/currency list</green> <dark_gray>-</dark_gray> <gray>List of currencies.</gray>",
                "<dark_green><b>»</b></dark_green> <green>/currency symbol <plural> <char|remove></green> <dark_gray>-</dark_gray> <gray>Select a symbol for a currency or remove it.</gray>",
                "<dark_green><b>»</b></dark_green> <green>/currency color <plural> <color></green> <dark_gray>-</dark_gray> <gray>Select a color for a currency.</gray>",
                "<dark_green><b>»</b></dark_green> <green>/currency colorlist</green> <dark_gray>-</dark_gray> <gray>List of Colors.</gray>",
                "<dark_green><b>»</b></dark_green> <green>/currency decimals <plural></green> <dark_gray>-</dark_gray> <gray>Enable decimals for a currency.</gray>",
                "<dark_green><b>»</b></dark_green> <green>/currency payable <plural></green> <dark_gray>-</dark_gray> <gray>Set a currency payable or not.</gray>",
                "<dark_green><b>»</b></dark_green> <green>/currency default <plural></green> <dark_gray>-</dark_gray> <gray>Set a currency as default.</gray>",
                "<dark_green><b>»</b></dark_green> <green>/currency startbal <plural> <amount></green> <dark_gray>-</dark_gray> <gray>Set the starting balance for a currency.</gray>",
                "<dark_green><b>»</b></dark_green> <green>/currency setrate <plural> <amount></green> <dark_gray>-</dark_gray> <gray>Sets the currency's exchange rate.</gray>"));

        config.addDefault(path + "usage.currency_create", "<dark_green><b>»</b></dark_green> <green>/currency create <singular> <plural></green> <dark_gray>-</dark_gray> <gray>Create a currency.</gray>");
        config.addDefault(path + "usage.currency_delete", "<dark_green><b>»</b></dark_green> <green>/currency delete <plural></green> <dark_gray>-</dark_gray> <gray>Delete a currency.</gray>");
        config.addDefault(path + "usage.currency_convert", "<dark_green><b>»</b></dark_green> <green>/currency convert <method></green> <dark_gray>-</dark_gray> <gray>Convert storage method. WARN: Take backups first and make sure the storage you are switching to is empty!</gray>");
        config.addDefault(path + "usage.currency_backend", "<dark_green><b>»</b></dark_green> <green>/currency backend <method></green> <dark_gray>-</dark_gray> <gray>Switch backend. This does not convert.</gray>");
        config.addDefault(path + "usage.currency_view", "<dark_green><b>»</b></dark_green> <green>/currency view <plural></green> <dark_gray>-</dark_gray> <gray>View information about a currency.</gray>");
        config.addDefault(path + "usage.currency_list", "<dark_green><b>»</b></dark_green> <green>/currency list</green> <dark_gray>-</dark_gray> <gray>List of currencies.</gray>");
        config.addDefault(path + "usage.currency_symbol", "<dark_green><b>»</b></dark_green> <green>/currency symbol <plural> <char|remove></green> <dark_gray>-</dark_gray> <gray>Select a symbol for a currency or remove it.</gray>");
        config.addDefault(path + "usage.currency_color", "<dark_green><b>»</b></dark_green> <green>/currency color <plural> <color></green> <dark_gray>-</dark_gray> <gray>Select a color for a currency.</gray>");
        config.addDefault(path + "usage.currency_colorlist", "<dark_green><b>»</b></dark_green> <green>/currency colorlist</green> <dark_gray>-</dark_gray> <gray>List of Colors.</gray>");
        config.addDefault(path + "usage.currency_payable", "<dark_green><b>»</b></dark_green> <green>/currency payable <plural></green> <dark_gray>-</dark_gray> <gray>Set a currency payable or not.</gray>");
        config.addDefault(path + "usage.currency_default", "<dark_green><b>»</b></dark_green> <green>/currency default <plural></green> <dark_gray>-</dark_gray> <gray>Set a currency as default.</gray>");
        config.addDefault(path + "usage.currency_decimals", "<dark_green><b>»</b></dark_green> <green>/currency decimals <plural></green> <dark_gray>-</dark_gray> <gray>Enable decimals for a currency.</gray>");
        config.addDefault(path + "usage.currency_startbal", "<dark_green><b>»</b></dark_green> <green>/currency startbal <plural> <amount></green> <dark_gray>-</dark_gray> <gray>Set the starting balance for a currency.</gray>");
        config.addDefault(path + "usage.currency_setrate", "<dark_green><b>»</b></dark_green> <green>/currency setrate <plural> <amount></green> <dark_gray>-</dark_gray> <gray>Sets the currency's exchange rate.</gray>");

        config.options().copyDefaults(true);
        plugin.saveConfig();
        plugin.reloadConfig();
    }

}
