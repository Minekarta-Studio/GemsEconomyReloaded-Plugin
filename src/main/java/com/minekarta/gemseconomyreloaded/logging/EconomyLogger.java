package com.minekarta.gemseconomyreloaded.logging;

import com.minekarta.gemseconomyreloaded.GemsEconomyReloaded;

public class EconomyLogger extends AbstractLogger {

    private final GemsEconomyReloaded plugin;

    public EconomyLogger(GemsEconomyReloaded plugin) {
        super(plugin);
        this.plugin = plugin;
    }


}
