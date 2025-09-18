package me.xanium.gemseconomy.utils;

import me.xanium.gemseconomy.GemsEconomy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;

public class UtilServer {

    private static Server getServer(){
        return Bukkit.getServer();
    }
    private static final String Console_Prefix = "<green>[GemsEconomy] </green>";
    private static final String Error_Prefix = "<red>[G-Eco-Error] </red>";

    public static void consoleLog(String message){
        if(GemsEconomy.getInstance().isDebug()) ModernChat.send(getServer().getConsoleSender(), Console_Prefix + message);
    }

    public static void consoleLog(Throwable message){
        ModernChat.send(getServer().getConsoleSender(), Error_Prefix + message.getMessage());
        message.printStackTrace();
    }

    public static OfflinePlayer getOfflinePlayer(String name) {
        return Bukkit.getOfflinePlayer(name);
    }
}
