/*
 * Copyright Xanium Development (c) 2013-2018. All Rights Reserved.
 * Any code contained within this document, and any associated APIs with similar branding
 * are the sole property of Xanium Development. Distribution, reproduction, taking snippets or claiming
 * any contents as your own will break the terms of the license, and void any agreements with you, the third party.
 * Thank you.
 */

package me.xanium.gemseconomy.file;

import me.xanium.gemseconomy.GemsEconomy;
import me.xanium.gemseconomy.utils.ModernChat;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class F {

    private static final GemsEconomy plugin = GemsEconomy.getInstance();
    private static final FileConfiguration cfg = plugin.getConfig();

    private static Component get(String path){
        return ModernChat.MMRC(cfg.getString(path));
    }

    private static List<String> getList(String path){
        return new ArrayList<>(cfg.getStringList(path));
    }

    public static String getRaw(String path) {
        return cfg.getString(path);
    }

    public static String getRawPrefix() {
        return cfg.getString("Messages.prefix");
    }

    public static Component getPrefix() {
        return ModernChat.MMRC(cfg.getString("Messages.prefix"));
    }

    public static Component getNoPerms() {
        return getPrefix().append(ModernChat.MMRC(cfg.getString("Messages.nopermission")));
    }

    public static Component getNoConsole() {
        return getPrefix().append(ModernChat.MMRC(cfg.getString("Messages.noconsole")));
    }

    public static Component getInsufficientFunds() { return getPrefix().append(ModernChat.MMRC(cfg.getString("Messages.insufficientFunds"))); }

    public static Component getTargetInsufficientFunds() { return getPrefix().append(ModernChat.MMRC(cfg.getString("Messages.targetInsufficientFunds"))); }

    public static Component getPayerMessage() {
        return getPrefix().append(ModernChat.MMRC(cfg.getString("Messages.payer")));
    }

    public static Component getPaidMessage() {
        return getPrefix().append(ModernChat.MMRC(cfg.getString("Messages.paid")));
    }

    public static Component getPayUsage() {
        return ModernChat.MMRC(cfg.getString("Messages.usage.pay_command"));
    }

    public static Component getAddMessage() {
        return getPrefix().append(ModernChat.MMRC(cfg.getString("Messages.add")));
    }

    public static Component getTakeMessage() {
        return getPrefix().append(ModernChat.MMRC(cfg.getString("Messages.take")));
    }

    public static Component getSetMessage() {
        return getPrefix().append(ModernChat.MMRC(cfg.getString("Messages.set")));
    }

    public static Component getPlayerDoesNotExist() { return getPrefix().append(ModernChat.MMRC(cfg.getString("Messages.player_is_null"))); }

    public static Component getPayYourself() {
        return getPrefix().append(ModernChat.MMRC(cfg.getString("Messages.pay_yourself")));
    }

    public static Component getUnknownCurrency() { return getPrefix().append(ModernChat.MMRC(cfg.getString("Messages.unknownCurrency"))); }

    public static Component getUnknownSubCommand() {
        return getPrefix().append(ModernChat.MMRC(cfg.getString("Messages.unknownCommand")));
    }

    public static void getManageHelp(CommandSender sender) {
        for (String s : cfg.getStringList("Messages.help.eco_command")) {
            ModernChat.send(sender, s.replace("{prefix}", getRawPrefix()));
        }
    }

    public static void getExchangeHelp(CommandSender sender) {
        for (String s : cfg.getStringList("Messages.help.exchange_command")) {
            ModernChat.send(sender, s.replace("{prefix}", getRawPrefix()));
        }
    }

    public static Component getBalance() {
        return getPrefix().append(ModernChat.MMRC(cfg.getString("Messages.balance.current")));
    }

    public static Component getBalanceMultiple() { return getPrefix().append(ModernChat.MMRC(cfg.getString("Messages.balance.multiple"))); }

    public static Component getBalanceList() {
        return ModernChat.MMRC(cfg.getString("Messages.balance.list"));
    }

    public static Component getUnvalidAmount() {
        return getPrefix().append(ModernChat.MMRC(cfg.getString("Messages.invalidamount")));
    }

    public static Component getUnvalidPage() {
        return getPrefix().append(ModernChat.MMRC(cfg.getString("Messages.invalidpage")));
    }

    public static void getChequeHelp(CommandSender sender) {
        for (String s : cfg.getStringList("Messages.help.cheque_command")) {
            ModernChat.send(sender, s.replace("{prefix}", getRawPrefix()));
        }
    }

    public static Component getChequeSucess() {
        return getPrefix().append(ModernChat.MMRC(cfg.getString("Messages.cheque.success")));
    }

    public static Component getChequeRedeemed() { return getPrefix().append(ModernChat.MMRC(cfg.getString("Messages.cheque.redeemed"))); }

    public static Component getChequeInvalid() {
        return getPrefix().append(ModernChat.MMRC(cfg.getString("Messages.cheque.invalid")));
    }

    public static Component getGiveUsage(){
        return ModernChat.MMRC(cfg.getString("Messages.usage.give_command"));
    }

    public static Component getTakeUsage(){
        return ModernChat.MMRC(cfg.getString("Messages.usage.take_command"));
    }

    public static Component getSetUsage(){
        return ModernChat.MMRC(cfg.getString("Messages.usage.set_command"));
    }

    public static Component getBalanceTopHeader(){
        return ModernChat.MMRC(cfg.getString("Messages.balance_top.header"));
    }

    public static Component getBalanceTopEmpty(){
        return ModernChat.MMRC(cfg.getString("Messages.balance_top.empty"));
    }

    public static Component getBalanceTopNext(){
        return ModernChat.MMRC(cfg.getString("Messages.balance_top.next"));
    }

    public static Component getBalanceTop(){
        return ModernChat.MMRC(cfg.getString("Messages.balance_top.balance"));
    }

    public static Component getNoDefaultCurrency(){
        return getPrefix().append(ModernChat.MMRC(cfg.getString("Messages.noDefaultCurrency")));
    }

    public static Component getBalanceNone(){
        return getPrefix().append(ModernChat.MMRC(cfg.getString("Messages.balance.none")));
    }

    public static Component getBalanceTopNoSupport(){
        return getPrefix().append(ModernChat.MMRC(cfg.getString("Messages.balance_top.nosupport")));
    }

    public static Component getPayNoPerms(){
        return getPrefix().append(ModernChat.MMRC(cfg.getString("Messages.payNoPermission")));
    }

    public static Component getCurrencyNotPayable(){
        return getPrefix().append(ModernChat.MMRC(cfg.getString("Messages.currencyNotPayable")));
    }

    public static Component getAccountMissing(){
        return getPrefix().append(ModernChat.MMRC(cfg.getString("Messages.accountMissing")));
    }

    public static Component getCannotReceive(){
        return getPrefix().append(ModernChat.MMRC(cfg.getString("Messages.cannotReceiveMoney")));
    }


    public static Component getCurrencyUsage_Create() { return get("Messages.usage.currency_create"); }
    public static Component getCurrencyUsage_Delete() { return get("Messages.usage.currency_delete"); }
    public static Component getCurrencyUsage_View() { return get("Messages.usage.currency_view"); }
    public static Component getCurrencyUsage_Default() { return get("Messages.usage.currency_default"); }
    public static Component getCurrencyUsage_List() { return get("Messages.usage.currency_list"); }
    public static Component getCurrencyUsage_Color() { return get("Messages.usage.currency_color"); }
    public static Component getCurrencyUsage_Colorlist() { return get("Messages.usage.currency_colorlist"); }
    public static Component getCurrencyUsage_Payable() { return get("Messages.usage.currency_payable"); }
    public static Component getCurrencyUsage_Startbal() { return get("Messages.usage.currency_startbal"); }
    public static Component getCurrencyUsage_Decimals() { return get("Messages.usage.currency_decimals"); }
    public static Component getCurrencyUsage_Symbol() { return get("Messages.usage.currency_symbol"); }
    public static Component getCurrencyUsage_Rate() { return get("Messages.usage.currency_setrate"); }
    public static Component getCurrencyUsage_Backend() {
        return get("Messages.usage.currency_backend");
    }
    public static Component getCurrencyUsage_Convert() {
        return get("Messages.usage.currency_convert");
    }

    public static void sendCurrencyUsage(CommandSender sender){
        for(String s : getList("Messages.help.currency_command")){
            ModernChat.send(sender, s.replace("{prefix}", getRawPrefix()));
        }
    }

    public static Component getExchangeSuccess(){
        return getPrefix().append(ModernChat.MMRC(cfg.getString("Messages.exchange_success")));
    }

    public static Component getExchangeSuccessCustom(){
        return getPrefix().append(ModernChat.MMRC(cfg.getString("Messages.exchange_success_custom")));
    }

    public static Component getExchangeSuccessCustomOther(){
        return getPrefix().append(ModernChat.MMRC(cfg.getString("Messages.exchange_success_custom_other")));
    }

    public static Component getExchangeRateSet(){
        return getPrefix().append(ModernChat.MMRC(cfg.getString("Messages.exchange_rate_set")));
    }

    public static Component getExchangeNoPermCustom(){
        return getPrefix().append(ModernChat.MMRC(cfg.getString("Messages.exchange_command.no_perms.custom")));
    }

    public static Component getExchangeNoPermPreset(){
        return getPrefix().append(ModernChat.MMRC(cfg.getString("Messages.exchange_command.no_perms.preset")));
    }
}
