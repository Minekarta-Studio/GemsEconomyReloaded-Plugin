package me.xanium.gemseconomy.cheque;

import me.xanium.gemseconomy.GemsEconomy;
import me.xanium.gemseconomy.currency.Currency;
import me.xanium.gemseconomy.nbt.NBT;
import me.xanium.gemseconomy.utils.UtilString;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ChequeManager {

    private final GemsEconomy plugin;
    private final ItemStack chequeBaseItem;
    private final String nbt_issuer = "cheque_issuer";
    private final String nbt_value = "cheque_value";
    private final String nbt_currency = "cheque_currency";

    public ChequeManager(GemsEconomy plugin) {
        this.plugin = plugin;

        ItemStack item = new ItemStack(Material.valueOf(plugin.getConfig().getString("cheque.material")), 1);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(LegacyComponentSerializer.legacyAmpersand().deserialize(UtilString.colorize(plugin.getConfig().getString("cheque.name"))));
        meta.lore(plugin.getConfig().getStringList("cheque.lore").stream().map(line -> LegacyComponentSerializer.legacyAmpersand().deserialize(UtilString.colorize(line))).collect(Collectors.toList()));
        item.setItemMeta(meta);
        chequeBaseItem = item;
    }

    public ItemStack write(String creatorName, Currency currency, double amount) {
        if (!currency.isPayable()) return null;

        if (creatorName.equals("CONSOLE")) {
            creatorName = UtilString.colorize(plugin.getConfig().getString("cheque.console_name"));
        }
        List<Component> formatLore = new ArrayList<>();

        for (Component baseLore2 : Objects.requireNonNull(chequeBaseItem.lore())) {
            formatLore.add(LegacyComponentSerializer.legacyAmpersand().deserialize(LegacyComponentSerializer.legacyAmpersand().serialize(baseLore2).replace("{value}", currency.format(amount)).replace("{player}", creatorName)));
        }
        ItemStack ret = chequeBaseItem.clone();
        ItemMeta meta = ret.getItemMeta();
        meta.lore(formatLore);
        ret.setItemMeta(meta);

        ret = NBT.setString(ret, nbt_issuer, creatorName);
        ret = NBT.setString(ret, nbt_currency, currency.getUuid().toString());
        ret = NBT.setDouble(ret, nbt_value, amount);
        return ret;
    }

    public boolean isValid(ItemStack itemstack) {
        if (itemstack == null || itemstack.getType() != chequeBaseItem.getType()) return false;
        if (NBT.hasKey(itemstack, nbt_value) && NBT.hasKey(itemstack, nbt_currency) && NBT.hasKey(itemstack, nbt_issuer)) {
            ItemMeta meta = itemstack.getItemMeta();
            if (meta == null) return false;
            return meta.hasDisplayName() && meta.displayName().equals(chequeBaseItem.displayName());
        }
        return false;
    }

    public double getValue(ItemStack itemstack) {
        Double val = NBT.getDouble(itemstack, nbt_value);
        return val != null ? val : 0;
    }

    public Currency getCurrency(ItemStack item) {
        String currencyUuid = NBT.getString(item, nbt_currency);
        if (currencyUuid != null) {
            return plugin.getCurrencyManager().getCurrency(currencyUuid);
        }
        return plugin.getCurrencyManager().getDefaultCurrency();
    }
}
