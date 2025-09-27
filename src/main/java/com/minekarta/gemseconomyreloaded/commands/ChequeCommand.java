package com.minekarta.gemseconomyreloaded.commands;

import com.minekarta.gemseconomyreloaded.GemsEconomyReloaded;
import com.minekarta.gemseconomyreloaded.account.Account;
import com.minekarta.gemseconomyreloaded.cheque.ChequeManager;
import com.minekarta.gemseconomyreloaded.currency.Currency;
import com.minekarta.gemseconomyreloaded.file.F;
import com.minekarta.gemseconomyreloaded.utils.ModernChat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ChequeCommand implements CommandExecutor, TabCompleter {

    private final ChequeManager chequeManager = GemsEconomyReloaded.getInstance().getChequeManager();
    private final GemsEconomyReloaded plugin = GemsEconomyReloaded.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!plugin.isChequesEnabled()) {
            ModernChat.send(sender, F.getChequesDisabled());
            return true;
        }

        if (args.length == 0) {
            F.getChequeHelp(sender);
            return true;
        }

        if (!(sender instanceof Player)) {
            ModernChat.send(sender, F.getPlayerOnly());
            return true;
        }

        Player player = (Player) sender;
        String subcommand = args[0];

        if (subcommand.equalsIgnoreCase("write")) {
            if (args.length < 2) {
                ModernChat.send(sender, F.getChequeWriteHelp());
                return true;
            }
            double amount;
            try {
                amount = Double.parseDouble(args[1]);
            } catch (NumberFormatException e) {
                ModernChat.send(sender, F.getInvalidAmount());
                return true;
            }
            Currency currency = plugin.getCurrencyManager().getDefaultCurrency();
            if (args.length == 3) {
                currency = plugin.getCurrencyManager().getCurrency(args[2]);
            }
            if (currency == null) {
                ModernChat.send(sender, F.getInvalidCurrency());
                return true;
            }
            final Currency finalCurrency = currency;
            Account account = plugin.getAccountManager().getAccount(player);
            if (account.getBalance(finalCurrency) >= amount) {
                account.withdraw(finalCurrency, amount);
                ItemStack cheque = chequeManager.write(player.getName(), finalCurrency, amount);
                player.getInventory().addItem(cheque);
                ModernChat.send(sender, F.getChequeWritten().replaceText(builder -> builder.matchLiteral("{value}").replacement(finalCurrency.format(amount))));
            } else {
                ModernChat.send(sender, F.getInsufficientFunds());
            }
        } else if (subcommand.equalsIgnoreCase("redeem")) {
            ItemStack item = player.getInventory().getItemInMainHand();
            if (chequeManager.isValid(item)) {
                double value = chequeManager.getValue(item);
                Currency currency = chequeManager.getCurrency(item);
                Account account = plugin.getAccountManager().getAccount(player);
                account.deposit(currency, value);
                ModernChat.send(sender, F.getChequeRedeemed().replaceText(builder -> builder.matchLiteral("{value}").replacement(currency.format(value))));
                if (item.getAmount() > 1) {
                    item.setAmount(item.getAmount() - 1);
                } else {
                    player.getInventory().setItemInMainHand(null);
                }
            } else {
                ModernChat.send(sender, F.getChequeInvalid());
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], Arrays.asList("write", "redeem"), completions);
        } else if (args.length == 3 && args[0].equalsIgnoreCase("write")) {
            StringUtil.copyPartialMatches(args[2], plugin.getCurrencyManager().getCurrencies().stream().map(Currency::getPlural).collect(Collectors.toList()), completions);
        }
        Collections.sort(completions);
        return completions;
    }
}
