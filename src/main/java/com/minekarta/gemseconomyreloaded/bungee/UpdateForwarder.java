package com.minekarta.gemseconomyreloaded.bungee;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.minekarta.gemseconomyreloaded.GemsEconomyReloaded;
import com.minekarta.gemseconomyreloaded.currency.Currency;
import com.minekarta.gemseconomyreloaded.utils.ModernChat;
import com.minekarta.gemseconomyreloaded.utils.SchedulerUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

public class UpdateForwarder implements PluginMessageListener {

    /**
     * GemsEconomyReloaded Bungee-Spigot Messaging Listener
     *
     * This listener is used to update currencies and balance for players
     * on different servers. This is important to sustain synced balances
     * and currencies on all of the servers.
     */

    private GemsEconomyReloaded plugin;
    private final String channelName = "GemsEconomyReloaded Data Channel";

    public UpdateForwarder(GemsEconomyReloaded plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player notInUse, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        if (subchannel.equals(channelName)) {
            String[] info = in.readUTF().split(",");
            String type = info[0];
            String name = info[1];
            if(plugin.isDebug()){
                ModernChat.send(Bukkit.getConsoleSender(), channelName + " - Received: " + type + " = " + name);
            }

            if(type.equals("currency")){
                UUID uuid = UUID.fromString(name);
                Currency currency = plugin.getCurrencyManager().getCurrency(uuid);
                if(currency != null) {
                    plugin.getDataStore().updateCurrencyLocally(currency);
                    if(GemsEconomyReloaded.getInstance().isDebug()){
                        ModernChat.send(Bukkit.getConsoleSender(), channelName + " - Currency " + name + " updated.");
                    }
                }
            }
            else if(type.equals("account")){
                UUID uuid = UUID.fromString(name);
                plugin.getAccountManager().removeAccount(uuid);
                SchedulerUtils.runAsync(() -> plugin.getDataStore().loadAccount(uuid));
                if(plugin.isDebug()){
                    ModernChat.send(Bukkit.getConsoleSender(), channelName + " - Account " + name + " updated.");
                }
            }
        }
    }


    public void sendUpdateMessage(String type, String name){
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Forward");
        out.writeUTF("ONLINE");
        out.writeUTF(channelName);

        ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
        DataOutputStream msgout = new DataOutputStream(msgbytes);
        try {
            msgout.writeUTF(type + "," + name);
        } catch (IOException exception){
            exception.printStackTrace();
        }

        out.write(msgbytes.toByteArray());

        Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
        if(player == null){
            if(GemsEconomyReloaded.getInstance().isDebug()){
                ModernChat.send(Bukkit.getConsoleSender(), channelName + " - Player is Null. Cancelled.");
            }
            return;
        }
        player.sendPluginMessage(GemsEconomyReloaded.getInstance(), "BungeeCord", out.toByteArray());
    }


}
