/*
 * Copyright Xanium Development (c) 2013-2018. All Rights Reserved.
 * Any code contained within this document, and any associated APIs with similar branding
 * are the sole property of Xanium Development. Distribution, reproduction, taking snippets or claiming
 * any contents as your own will break the terms of the license, and void any agreements with you, the third party.
 * Thank you.
 */

package me.xanium.gemseconomy.nbt;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;

public class NMSVersion {

    public static final String UNSUPPORTED = "Unsupported";

    public static final String V1_9_R1 = "v1_9_R1";

    public static final String V1_9_R2 = "v1_9_R2";

    public static final String V1_10_R1 = "v1_10_R1";

    public static final String V1_11_R1 = "v1_11_R1";

    public static final String V1_12_R1 = "v1_12_R1";

    public static final String V1_13_R1 = "v1_13_R1";

    public static final String V1_13_R2 = "v1_13_R2";

    public static final String V1_14_R1 = "v1_14_R1";

    public static final String V1_15_R1 = "v1_15_R1";

    public static final String V1_16_R1 = "v1_16_R1";

    public static final String V1_16_R2 = "v1_16_R2";

    public static final String V1_16_R3 = "v1_16_R3";

    private Map<Integer, String> versionMap;

    private int versionID;

    public int getVersionID() {
        return versionID;
    }

    public NMSVersion() {
        this.versionMap = new HashMap<>();
        this.loadVersions();

        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        String version = packageName.substring(packageName.lastIndexOf('.') + 1);
        if (this.versionMap.containsValue(version)) {
            this.versionID = getVersionID(version);
        } else {
            this.versionID = 0;
            Bukkit.getConsoleSender().sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&c----------------------------------------------------------"));
            Bukkit.getConsoleSender().sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(""));
            Bukkit.getConsoleSender().sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&4&lYOU ARE RUNNING AN UNSUPPORTED VERSION OF SPIGOT!"));
            Bukkit.getConsoleSender().sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(""));
            Bukkit.getConsoleSender().sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&cGemsEconomy Cheques functionality will at best be limited. Please don't come"));
            Bukkit.getConsoleSender().sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&ccomplaining to me, the developer of GemsEconomy, when something breaks,"));
            Bukkit.getConsoleSender().sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&cbecause running an unsupported version will cause exactly this. I do"));
            Bukkit.getConsoleSender().sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&cin no way accept responsibility for ANY damage caused to a server running"));
            Bukkit.getConsoleSender().sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&can unsupported version of Spigot. It is recommended that you change to"));
            Bukkit.getConsoleSender().sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&ca supported version of Spigot. Supported versions are 1.13, 1.14 & 1.15."));
            Bukkit.getConsoleSender().sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&c Versions marked with an asterisk (*) may have limited functionality."));
            Bukkit.getConsoleSender().sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(""));
            Bukkit.getConsoleSender().sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&c----------------------------------------------------------"));
        }
    }

    private void loadVersions() {
        registerVersion(UNSUPPORTED);
        registerVersion(V1_9_R1);
        registerVersion(V1_9_R2);
        registerVersion(V1_10_R1);
        registerVersion(V1_11_R1);
        registerVersion(V1_12_R1);
        registerVersion(V1_13_R1);
        registerVersion(V1_13_R2);
        registerVersion(V1_14_R1);
        registerVersion(V1_15_R1);
        registerVersion(V1_16_R1);
        registerVersion(V1_16_R2);
        registerVersion(V1_16_R3);
    }

    private void registerVersion(String string) {
        this.versionMap.put(this.versionMap.size(), string);
    }

    public String getVersionString() {
        return this.getVersionString(this.versionID);
    }

    public String getVersionString(int id) {
        return this.versionMap.get(id);
    }

    public int getVersionID(String version) {
        return this.versionMap.entrySet().parallelStream()
                .filter(e -> e.getValue().equalsIgnoreCase(version))
                .map(Map.Entry::getKey).findFirst().orElse(0);
    }

    public boolean runningNewerThan(String version) {
        return this.versionID >= this.getVersionID(version);
    }
}
