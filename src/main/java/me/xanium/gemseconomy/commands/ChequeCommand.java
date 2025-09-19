package me.xanium.gemseconomy.commands;

import me.xanium.gemseconomy.GemsEconomy;
import me.xanium.gemseconomy.account.Account;
import me.xanium.gemseconomy.cheque.ChequeManager;
import me.xanium.gemseconomy.currency.Currency;
import me.xanium.gemseconomy.file.F;
import me.xanium.gemseconomy.utils.ModernChat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ChequeCommand implements CommandExecutor {

    private final ChequeManager chequeManager = GemsEconomy.getInstance().getChequeManager();
    private final GemsEconomy plugin = GemsEconomy.getInstance();

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
}
