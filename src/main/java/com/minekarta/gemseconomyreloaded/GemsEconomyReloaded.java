package com.minekarta.gemseconomyreloaded;

import com.minekarta.gemseconomyreloaded.account.AccountManager;
import com.minekarta.gemseconomyreloaded.bungee.UpdateForwarder;
import com.minekarta.gemseconomyreloaded.cheque.ChequeManager;
import com.minekarta.gemseconomyreloaded.commands.*;
import com.minekarta.gemseconomyreloaded.currency.CurrencyManager;
import com.minekarta.gemseconomyreloaded.data.DataStorage;
import com.minekarta.gemseconomyreloaded.data.MySQLStorage;
import com.minekarta.gemseconomyreloaded.data.YamlStorage;
import com.minekarta.gemseconomyreloaded.file.Configuration;
import com.minekarta.gemseconomyreloaded.listeners.EconomyListener;
import com.minekarta.gemseconomyreloaded.logging.EconomyLogger;
import com.minekarta.gemseconomyreloaded.utils.Metrics;
import com.minekarta.gemseconomyreloaded.utils.SchedulerUtils;
import com.minekarta.gemseconomyreloaded.utils.ModernChat;
import com.minekarta.gemseconomyreloaded.utils.Updater;
import com.minekarta.gemseconomyreloaded.vault.VaultHandler;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class GemsEconomyReloaded extends JavaPlugin {

    private static GemsEconomyReloaded instance;

    private DataStorage dataStorage = null;
    private AccountManager accountManager;
    private ChequeManager chequeManager;
    private CurrencyManager currencyManager;
    private VaultHandler vaultHandler;
    private Metrics metrics;
    private EconomyLogger economyLogger;
    private UpdateForwarder updateForwarder;

    private boolean debug = false;
    private boolean vault = false;
    private boolean logging = false;
    private boolean cheques = true;

    private boolean disabling = false;

    @Override
    public void onLoad() {
        Configuration configuration = new Configuration(this);
        configuration.loadDefaultConfig();

        setDebug(getConfig().getBoolean("debug"));
        setVault(getConfig().getBoolean("vault"));
        setLogging(getConfig().getBoolean("transaction_log"));
        setCheques(getConfig().getBoolean("cheque.enabled"));
    }

    @Override
    public void onEnable() {
        instance = this;

        accountManager = new AccountManager(this);
        currencyManager = new CurrencyManager(this);
        economyLogger = new EconomyLogger(this);
        metrics = new Metrics(this);
        updateForwarder = new UpdateForwarder(this);

        initializeDataStore(getConfig().getString("storage"), true);

        getServer().getPluginManager().registerEvents(new EconomyListener(), this);

        BalanceCommand balanceCommand = new BalanceCommand();
        getCommand("balance").setExecutor(balanceCommand);
        getCommand("balance").setTabCompleter(balanceCommand);

        BalanceTopCommand balanceTopCommand = new BalanceTopCommand();
        getCommand("baltop").setExecutor(balanceTopCommand);
        getCommand("baltop").setTabCompleter(balanceTopCommand);

        EconomyCommand economyCommand = new EconomyCommand();
        getCommand("economy").setExecutor(economyCommand);
        getCommand("economy").setTabCompleter(economyCommand);

        PayCommand payCommand = new PayCommand();
        getCommand("pay").setExecutor(payCommand);
        getCommand("pay").setTabCompleter(payCommand);

        CurrencyCommand currencyCommand = new CurrencyCommand();
        getCommand("currency").setExecutor(currencyCommand);
        getCommand("currency").setTabCompleter(currencyCommand);

        ChequeCommand chequeCommand = new ChequeCommand();
        getCommand("cheque").setExecutor(chequeCommand);
        getCommand("cheque").setTabCompleter(chequeCommand);

        ExchangeCommand exchangeCommand = new ExchangeCommand();
        getCommand("exchange").setExecutor(exchangeCommand);
        getCommand("exchange").setTabCompleter(exchangeCommand);

        if (isVault()) {
            vaultHandler = new VaultHandler(this);
            vaultHandler.hook();
        } else {
            ModernChat.send(getServer().getConsoleSender(), "<yellow>Vault link is disabled.</yellow>");
        }

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", updateForwarder);

        if (isLogging()) {
            getEconomyLogger().save();
        }

        if(isChequesEnabled()){
            chequeManager = new ChequeManager(this);
        }

        SchedulerUtils.runAsync(this::checkForUpdate);
    }

    @Override
    public void onDisable() {
        disabling = true;

        if (isVault()) getVaultHandler().unhook();

        if (getDataStore() != null) {
            SchedulerUtils.runAsync(getDataStore()::close);
        }
    }

    public void initializeDataStore(String strategy, boolean load) {

        DataStorage.getMethods().add(new YamlStorage(new File(getDataFolder(), "data.yml")));
        DataStorage.getMethods().add(new MySQLStorage(getConfig().getString("mysql.host"), getConfig().getInt("mysql.port"), getConfig().getString("mysql.database"), getConfig().getString("mysql.username"), getConfig().getString("mysql.password")));

        if (strategy != null) {
            dataStorage = DataStorage.getMethod(strategy);
        } else {
            ModernChat.send(getServer().getConsoleSender(), "<red>No valid storage method provided.</red>");
            ModernChat.send(getServer().getConsoleSender(), "<red>Check your files, then try again.</red>");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        try {
            ModernChat.send(getServer().getConsoleSender(), "Initializing data store \"" + getDataStore().getName() + "\"...");
            getDataStore().initialize();

            if (load) {
                ModernChat.send(getServer().getConsoleSender(), "Loading currencies...");
                getDataStore().loadCurrencies();
                ModernChat.send(getServer().getConsoleSender(), "Loaded " + getCurrencyManager().getCurrencies().size() + " currencies!");
            }
        } catch (Throwable e) {
            ModernChat.send(getServer().getConsoleSender(), "<red>Cannot load initial data from DataStore.</red>");
            ModernChat.send(getServer().getConsoleSender(), "<red>Check your files, then try again.</red>");
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    private void checkForUpdate() {
        Updater updater = new Updater(this);
        try {
            if (updater.checkForUpdates()) {
                ModernChat.send(getServer().getConsoleSender(), "<green>-------------------------------------------</green>");
                ModernChat.send(getServer().getConsoleSender(), "<green>New Version: " + updater.getNewVersion() + "</green>");
                ModernChat.send(getServer().getConsoleSender(), "<green>Current Version: " + updater.getCurrentVersion() + "</green>");
                ModernChat.send(getServer().getConsoleSender(), "<green>Download link: " + updater.getResourceURL() + "</green>");
                ModernChat.send(getServer().getConsoleSender(), "<green>--------------------------------------------</green>");
            }
        } catch (IOException e) {
            ModernChat.send(getServer().getConsoleSender(), "<red>Could not check for updates! Error log will follow if debug is enabled.</red>");
            if (isDebug()) {
                e.printStackTrace();
            }
        }
    }

    public DataStorage getDataStore() {
        return dataStorage;
    }

    public static GemsEconomyReloaded getInstance() {
        return instance;
    }

    public CurrencyManager getCurrencyManager() {
        return currencyManager;
    }

    public AccountManager getAccountManager() {
        return accountManager;
    }

    public VaultHandler getVaultHandler() {
        return vaultHandler;
    }

    public EconomyLogger getEconomyLogger() {
        return economyLogger;
    }

    public Metrics getMetrics() {
        return metrics;
    }

    public ChequeManager getChequeManager() {
        return chequeManager;
    }

    public UpdateForwarder getUpdateForwarder() {
        return updateForwarder;
    }

    public boolean isDebug() {
        return debug;
    }

    private void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean isVault() {
        return vault;
    }

    private void setVault(boolean vault) {
        this.vault = vault;
    }

    public boolean isLogging() {
        return logging;
    }

    public void setLogging(boolean logging) {
        this.logging = logging;
    }

    public boolean isDisabling() {
        return disabling;
    }

    public boolean isChequesEnabled() {
        return cheques;
    }

    public void setCheques(boolean cheques) {
        this.cheques = cheques;
    }
}
