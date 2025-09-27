/*
 * Copyright Xanium Development (c) 2013-2018. All Rights Reserved.
 * Any code contained within this document, and any associated APIs with similar branding
 * are the sole property of Xanium Development. Distribution, reproduction, taking snippets or claiming
 * any contents as your own will break the terms of the license, and void any agreements with you, the third party.
 * Thank you.
 */

package com.minekarta.gemseconomyreloaded.api;

import com.minekarta.gemseconomyreloaded.GemsEconomyReloaded;
import com.minekarta.gemseconomyreloaded.account.Account;
import com.minekarta.gemseconomyreloaded.currency.Currency;

import java.util.UUID;

public class GemsEconomyAPI {

    public final GemsEconomyReloaded plugin = GemsEconomyReloaded.getInstance();

    public GemsEconomyAPI(){
        if(plugin.getCurrencyManager().getDefaultCurrency() == null){
            GemsEconomyReloaded.getInstance().getLogger().warning("||");
            GemsEconomyReloaded.getInstance().getLogger().warning("||");
            GemsEconomyReloaded.getInstance().getLogger().warning("||");
            GemsEconomyReloaded.getInstance().getLogger().warning("There is no default currency, so therefore none of the API will work!!!");
            GemsEconomyReloaded.getInstance().getLogger().warning("There is no default currency, so therefore none of the API will work!!!");
            GemsEconomyReloaded.getInstance().getLogger().warning("||");
            GemsEconomyReloaded.getInstance().getLogger().warning("||");
            GemsEconomyReloaded.getInstance().getLogger().warning("||");
        }
    }

    /**
     *
     * @param uuid - The users unique ID.
     * @param amount - An amount of the default currency.
     */
    public void deposit(UUID uuid, double amount){
        Account acc = plugin.getAccountManager().getAccount(uuid);
        acc.deposit(plugin.getCurrencyManager().getDefaultCurrency(), amount);
    }

    /**
     *
     * @param uuid - The users unique ID.
     * @param amount - An amount of a currency, if the currency is null, the default will be used.
     * @param currency - A specified currency.
     */
    public void deposit(UUID uuid, double amount, Currency currency){
        Account acc = plugin.getAccountManager().getAccount(uuid);
        if(currency != null) {
            acc.deposit(currency, amount);
        }else{
            acc.deposit(plugin.getCurrencyManager().getDefaultCurrency(), amount);
        }
    }

    /**
     *
     * @param uuid - The users unique ID.
     * @param amount - An amount of the default currency.
     */
    public void withdraw(UUID uuid, double amount){
        Account acc = plugin.getAccountManager().getAccount(uuid);
        acc.withdraw(plugin.getCurrencyManager().getDefaultCurrency(), amount);
    }

    /**
     *
     * @param uuid - The users unique ID.
     * @param amount - An amount of the currency.
     * @param currency - The currency you withdraw from.
     */
    public void withdraw(UUID uuid, double amount, Currency currency){
        Account acc = plugin.getAccountManager().getAccount(uuid);
        if(currency != null) {
            acc.withdraw(currency, amount);
        }else{
            acc.withdraw(plugin.getCurrencyManager().getDefaultCurrency(), amount);
        }
    }

    /**
     *
     * @param uuid - The users unique ID.
     * @return - The default currency balance of the user.
     */
    public double getBalance(UUID uuid){
        Account acc = plugin.getAccountManager().getAccount(uuid);
        return acc.getBalance(plugin.getCurrencyManager().getDefaultCurrency());
    }

    /**
     *
     * @param uuid - The users unique ID.
     * @param currency - An amount of the default currency.
     * @return - The balance of the specified currency.
     */
    public double getBalance(UUID uuid, Currency currency) {
        Account acc = plugin.getAccountManager().getAccount(uuid);
        if (currency != null) {
            return acc.getBalance(currency);
        }else{
            return acc.getBalance(plugin.getCurrencyManager().getDefaultCurrency());
        }
    }

    /**
     *
     * @param name - Currency singular or plural.
     * @return - Currency Object.
     */
    public Currency getCurrency(String name){
        if(plugin.getCurrencyManager().getCurrency(name) != null){
            return plugin.getCurrencyManager().getCurrency(name);
        }
        return null;
    }


}
