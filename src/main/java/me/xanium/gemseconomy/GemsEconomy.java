package me.xanium.gemseconomy;

import me.xanium.gemseconomy.account.AccountManager;
import me.xanium.gemseconomy.bungee.UpdateForwarder;
import me.xanium.gemseconomy.cheque.ChequeManager;
import me.xanium.gemseconomy.commands.*;
import me.xanium.gemseconomy.currency.CurrencyManager;
import me.xanium.gemseconomy.data.DataStorage;
import me.xanium.gemseconomy.data.MySQLStorage;
import me.xanium.gemseconomy.data.YamlStorage;
import me.xanium.gemseconomy.file.Configuration;
import me.xanium.gemseconomy.listeners.EconomyListener;
import me.xanium.gemseconomy.logging.EconomyLogger;
import me.xanium.gemseconomy.utils.Metrics;
import me.xanium.gemseconomy.utils.SchedulerUtils;
import me.xanium.gemseconomy.utils.ModernChat;
import me.xanium.gemseconomy.utils.Updater;
import me.xanium.gemseconomy.vault.VaultHandler;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class GemsEconomy extends JavaPlugin {

    private static GemsEconomy instance;

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
        getCommand("balance").setExecutor(new BalanceCommand());
        getCommand("baltop").setExecutor(new BalanceTopCommand());
        getCommand("economy").setExecutor(new EconomyCommand());
        getCommand("pay").setExecutor(new PayCommand());
        getCommand("currency").setExecutor(new CurrencyCommand());
        getCommand("cheque").setExecutor(new ChequeCommand());
        getCommand("exchange").setExecutor(new ExchangeCommand());

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

    public static GemsEconomy getInstance() {
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
