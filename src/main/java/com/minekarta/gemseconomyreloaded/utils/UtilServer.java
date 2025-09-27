package com.minekarta.gemseconomyreloaded.utils;

import com.minekarta.gemseconomyreloaded.GemsEconomyReloaded;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;

public class UtilServer {

    private static Server getServer(){
        return Bukkit.getServer();
    }
    private static final String Console_Prefix = "<green>[GemsEconomyReloaded] </green>";
    private static final String Error_Prefix = "<red>[G-Eco-Error] </red>";

    public static void consoleLog(String message){
        if(GemsEconomyReloaded.getInstance().isDebug()) ModernChat.send(getServer().getConsoleSender(), Console_Prefix + message);
    }

    public static void consoleLog(Throwable message){
        ModernChat.send(getServer().getConsoleSender(), Error_Prefix + message.getMessage());
        message.printStackTrace();
    }

    public static OfflinePlayer getOfflinePlayer(String name) {
        return Bukkit.getOfflinePlayer(name);
    }
}
