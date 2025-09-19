package me.xanium.gemseconomy.commands;

import me.xanium.gemseconomy.GemsEconomy;
import me.xanium.gemseconomy.account.Account;
import me.xanium.gemseconomy.cheque.ChequeManager;
import me.xanium.gemseconomy.currency.Currency;
import me.xanium.gemseconomy.utils.SchedulerUtils;
import me.xanium.gemseconomy.utils.UtilString;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ChequeCommand extends AbstractCommand {

    private final ChequeManager chequeManager = GemsEconomy.getInstance().getChequeManager();

    public ChequeCommand() {
        super("cheque", "gemseconomy.command.cheque");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!GemsEconomy.getInstance().isChequesEnabled()) {
            sender.sendMessage(GemsEconomy.lang("err_cheques_disabled"));
            return;
        }

        if (args.length == 0) {
            sender.sendMessage(GemsEconomy.lang("msg_cheque_help"));
            return;
        }

        Player player = (Player) sender;
        if (player == null) {
            sender.sendMessage(GemsEconomy.lang("err_player_only"));
            return;
        }

        String subcommand = args[0];
        if (subcommand.equalsIgnoreCase("write")) {
            if (args.length < 2) {
                sender.sendMessage(GemsEconomy.lang("msg_cheque_write_help"));
                return;
            }
            double amount = Double.parseDouble(args[1]);
            Currency currency = GemsEconomy.getInstance().getCurrencyManager().getDefaultCurrency();
            if (args.length == 3) {
                currency = GemsEconomy.getInstance().getCurrencyManager().getCurrency(args[2]);
            }
            if (currency == null) {
                sender.sendMessage(GemsEconomy.lang("err_invalid_currency"));
                return;
            }
            Account account = GemsEconomy.getInstance().getAccountManager().getAccount(player);
            if (account.getBalance(currency) >= amount) {
                account.withdraw(currency, amount);
                ItemStack cheque = chequeManager.write(player.getName(), currency, amount);
                player.getInventory().addItem(cheque);
                sender.sendMessage(GemsEconomy.lang("msg_cheque_written").replace("{value}", currency.format(amount)));
            } else {
                sender.sendMessage(GemsEconomy.lang("err_insufficient_funds"));
            }
        } else if (subcommand.equalsIgnoreCase("redeem")) {
            ItemStack item = player.getInventory().getItemInMainHand();
            if (chequeManager.isValid(item)) {
                double value = chequeManager.getValue(item);
                Currency currency = chequeManager.getCurrency(item);
                Account account = GemsEconomy.getInstance().getAccountManager().getAccount(player);
                account.deposit(currency, value);
                sender.sendMessage(GemsEconomy.lang("msg_cheque_redeemed").replace("{value}", currency.format(value)));
                if (item.getAmount() > 1) {
                    item.setAmount(item.getAmount() - 1);
                } else {
                    player.getInventory().setItemInMainHand(null);
                }
            } else {
                sender.sendMessage(GemsEconomy.lang("err_cheque_invalid"));
            }
        }
    }
}
