/*
 * Copyright Xanium Development (c) 2013-2018. All Rights Reserved.
 * Any code contained within this document, and any associated APIs with similar branding
 * are the sole property of Xanium Development. Distribution, reproduction, taking snippets or claiming
 * any contents as your own will break the terms of the license, and void any agreements with you, the third party.
 * Thank you.
 */

package me.xanium.gemseconomy.cheque;

import me.xanium.gemseconomy.GemsEconomy;
import me.xanium.gemseconomy.currency.Currency;
import me.xanium.gemseconomy.nbt.NBTItem;
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
    private final String nbt_issuer = "issuer";
    private final String nbt_value = "value";
    private final String nbt_currency = "currency";

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
        if(!currency.isPayable())return null;


        if (creatorName.equals("CONSOLE")) {
            creatorName = UtilString.colorize(plugin.getConfig().getString("cheque.console_name"));
        }
        List<Component> formatLore = new ArrayList<>();

        for (Component baseLore2 : Objects.requireNonNull(chequeBaseItem.getItemMeta().lore())) {
            formatLore.add(LegacyComponentSerializer.legacyAmpersand().deserialize(LegacyComponentSerializer.legacyAmpersand().serialize(baseLore2).replace("{value}", currency.format(amount)).replace("{player}", creatorName)));
        }
        ItemStack ret = chequeBaseItem.clone();
        NBTItem nbt = new NBTItem(ret);
        ItemMeta meta = nbt.getItem().getItemMeta();
        meta.lore(formatLore);
        nbt.getItem().setItemMeta(meta);
        nbt.setString(nbt_issuer, creatorName);
        nbt.setString(nbt_currency, currency.getPlural());
        nbt.setString(nbt_value, String.valueOf(amount));
        return nbt.getItem();
    }

    public boolean isValid(NBTItem itemstack) {
        if(itemstack.getItem().getType() != chequeBaseItem.getType())return false;
        if (itemstack.getString(nbt_value) != null && itemstack.getString(nbt_currency) != null && itemstack.getString(nbt_issuer) != null) {

            Component display = chequeBaseItem.getItemMeta().displayName();
            ItemMeta meta = itemstack.getItem().getItemMeta();

            if(meta == null) return false;

            if(meta.hasDisplayName() && meta.displayName().equals(display)){
                if(meta.hasLore() && meta.lore().size() == chequeBaseItem.getItemMeta().lore().size()){
                    return true;
                }
            }
        }
        return false;
    }

    public double getValue(NBTItem itemstack) {
        if (itemstack.getString(nbt_currency) != null && itemstack.getString(nbt_value) != null) {
            return Double.parseDouble(itemstack.getString(nbt_value));
        }
        return 0;
    }

    /**
     *
     * @param item - The Cheque.
     * @return - Currency it represents.
     */
    public Currency getCurrency(NBTItem item) {
        if (item.getString(nbt_currency) != null && item.getString(nbt_value) != null) {
            return plugin.getCurrencyManager().getCurrency(item.getString(nbt_currency));
        }
        return plugin.getCurrencyManager().getDefaultCurrency();
    }
}
