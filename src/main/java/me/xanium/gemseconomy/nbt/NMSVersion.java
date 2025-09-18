/*
 * Copyright Xanium Development (c) 2013-2018. All Rights Reserved.
 * Any code contained within this document, and any associated APIs with similar branding
 * are the sole property of Xanium Development. Distribution, reproduction, taking snippets or claiming
 * any contents as your own will break the terms of the license, and void any agreements with you, the third party.
 * Thank you.
 */

package me.xanium.gemseconomy.nbt;

import me.xanium.gemseconomy.utils.ModernChat;
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

    public static final String V1_17_R1 = "v1_17_R1";

    public static final String V1_18_R1 = "v1_18_R1";

    public static final String V1_18_R2 = "v1_18_R2";

    public static final String V1_19_R1 = "v1_19_R1";

    public static final String V1_19_R2 = "v1_19_R2";

    public static final String V1_19_R3 = "v1_19_R3";

    public static final String V1_20_R1 = "v1_20_R1";

    public static final String V1_20_R2 = "v1_20_R2";

    public static final String V1_20_R3 = "v1_20_R3";

    public static final String V1_20_R4 = "v1_20_R4";

    public static final String V1_21_R1 = "v1_21_R1";

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
            ModernChat.send(Bukkit.getConsoleSender(), "<red>----------------------------------------------------------</red>");
            ModernChat.send(Bukkit.getConsoleSender(), "");
            ModernChat.send(Bukkit.getConsoleSender(), "<dark_red><b>YOU ARE RUNNING AN UNSUPPORTED VERSION OF SPIGOT!</b></dark_red>");
            ModernChat.send(Bukkit.getConsoleSender(), "");
            ModernChat.send(Bukkit.getConsoleSender(), "<red>GemsEconomy Cheques functionality may be limited. Please don't come</red>");
            ModernChat.send(Bukkit.getConsoleSender(), "<red>complaining to the developer of GemsEconomy when something breaks,</red>");
            ModernChat.send(Bukkit.getConsoleSender(), "<red>because running an unsupported version can cause issues.</red>");
            ModernChat.send(Bukkit.getConsoleSender(), "<red>It is recommended that you change to a supported version of Spigot.</red>");
            ModernChat.send(Bukkit.getConsoleSender(), "<red>Please check the plugin's official page for a list of supported versions.</red>");
            ModernChat.send(Bukkit.getConsoleSender(), "");
            ModernChat.send(Bukkit.getConsoleSender(), "<red>----------------------------------------------------------</red>");
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
        registerVersion(V1_17_R1);
        registerVersion(V1_18_R1);
        registerVersion(V1_18_R2);
        registerVersion(V1_19_R1);
        registerVersion(V1_19_R2);
        registerVersion(V1_19_R3);
        registerVersion(V1_20_R1);
        registerVersion(V1_20_R2);
        registerVersion(V1_20_R3);
        registerVersion(V1_20_R4);
        registerVersion(V1_21_R1);
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
