/*
 * Copyright Xanium Development (c) 2013-2018. All Rights Reserved.
 * Any code contained within this document, and any associated APIs with similar branding
 * are the sole property of Xanium Development. Distribution, reproduction, taking snippets or claiming
 * any contents as your own will break the terms of the license, and void any agreements with you, the third party.
 * Thank you.
 */

package com.minekarta.gemseconomyreloaded.data;

import com.minekarta.gemseconomyreloaded.GemsEconomyReloaded;
import com.minekarta.gemseconomyreloaded.account.Account;
import com.minekarta.gemseconomyreloaded.currency.CachedTopListEntry;
import com.minekarta.gemseconomyreloaded.currency.Currency;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.UUID;

public abstract class DataStorage {

    public final GemsEconomyReloaded plugin = GemsEconomyReloaded.getInstance();

    private String name;
    private boolean topSupported;

    public DataStorage(String name, boolean topSupported) {
        this.name = name;
        this.topSupported = topSupported;
    }

    private static ArrayList<DataStorage> methods = new ArrayList<>();

    public static DataStorage getMethod(String name) {
        for (DataStorage store : getMethods()) {
            if (store.getName().equalsIgnoreCase(name)) {
                return store;
            }
        }
        return null;
    }

    public static ArrayList<DataStorage> getMethods() {
        return methods;
    }

    public abstract void initialize();

    public abstract void close();

    public abstract void loadCurrencies();

    public abstract void updateCurrencyLocally(Currency currency);

    public abstract void saveCurrency(Currency currency);

    public abstract void deleteCurrency(Currency currency);

    public abstract void getTopList(Currency currency, int offset, int amount, Callback<LinkedList<CachedTopListEntry>> callback);

    public abstract Account loadAccount(String name);

    public abstract Account loadAccount(UUID uuid);

    public abstract void loadAccount(UUID uuid, Callback<Account> callback);

    public abstract void loadAccount(String name, Callback<Account> callback);

    public abstract void saveAccount(Account account);

    public abstract void deleteAccount(Account account);

    /**
     * This is MYSQL ONLY!
     * @param account - Account to save to sql. See EconomyListener.java
     */
    public abstract void createAccount(Account account);

    public abstract ArrayList<Account> getOfflineAccounts();

    public String getName() {
        return this.name;
    }

    public boolean isTopSupported() {
        return this.topSupported;
    }
}

