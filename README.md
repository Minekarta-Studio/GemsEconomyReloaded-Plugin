# GemsEconomy Reloaded

GemsEconomy Reloaded plugin is a maintained version of the original plugin name GemsEconomy: https://github.com/Xanium1/GemsEconomy

This version is updated to support modern Minecraft versions (1.21+) and is maintained by Minekarta Studio.

## Features

- **Multi-currency Support**: Create and manage multiple types of currencies on your server.
- **Vault Support**: Integrates with Vault for broad compatibility with other economy plugins.
- **MySQL & BungeeCord Support**: Synchronize player balances across multiple servers.
- **Fully Configurable**: Customize messages, currency formats, and nearly every aspect of the plugin.
- **Cheques**: Players can write and redeem physical cheques for in-game currency.
- **PlaceholderAPI Support**: Display player balances on scoreboards, chat, and more.
- **Developer API**: A simple yet powerful API for other plugins to hook into GemsEconomy.
- **Offline Player Support**: Most commands work on players who are offline.

## Setup

1. Install the plugin by placing the `.jar` file in your server's `plugins` folder.
2. Start your server to generate the configuration files.
3. Create your first currency with `/currency create <name> <plural> <symbol>`. This will be the default currency.
4. Customize the plugin and currencies further through the configuration files and in-game commands.

## Commands

| Command | Description |
| --- | --- |
| `/balance (player)` | Checks your own or another player's balance. |
| `/baltop (currency)` | Shows the top balances for a specific currency. |
| `/pay <player> <amount> (currency)` | Pays another player from your balance. |
| `/eco <give/set/take> <player> <amount> (currency)` | Admin command to modify a player's balance. |
| `/cheque write <amount> (currency)` | Writes a cheque for a specific amount. |
| `/cheque redeem` | Redeems a cheque that you are holding. |
| `/currency` | Main command for managing currencies (create, delete, edit). |
| `/exchange` | Exchange between different currencies. |

## Permissions

| Permission | Description |
| --- | --- |
| `gemseconomy.command.balance` | Allows viewing your own balance. |
| `gemseconomy.command.balance.other` | Allows viewing other players' balances. |
| `gemseconomy.command.baltop` | Allows viewing the balance leaderboard. |
| `gemseconomy.command.pay` | Allows paying other players. |
| `gemseconomy.command.cheque` | Allows writing and redeeming cheques. |
| `gemseconomy.command.currency` | **(Admin)** Grants access to all currency management commands. |
| `gemseconomy.command.economy` | **(Admin)** Grants access to the `/eco` command for modifying balances. |
| `gemseconomy.command.give` | **(Admin)** Allows giving currency to players. |
| `gemseconomy.command.set` | **(Admin)** Allows setting a player's balance. |
| `gemseconomy.command.take` | **(Admin)** Allows taking currency from a player. |
| `gemseconomy.command.exchange.preset`| Permission to exchange currency with a preset rate. |
| `gemseconomy.command.exchange.custom`| Permission to exchange with a custom rate. |

## Placeholders

GemsEconomy supports [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/).

- `%gemseconomy_balance_default%` - Shows the player's balance for the default currency.
- `%gemseconomy_balance_default_formatted%` - Shows the formatted balance for the default currency.
- `%gemseconomy_balance_<currency>%` - Shows the player's balance for a specific currency (use plural or singular name).
- `%gemseconomy_balance_<currency>_formatted%` - Shows the formatted balance for a specific currency.

## Developer API

To use the API, make sure your plugin depends on GemsEconomy in your `plugin.yml`.

**Example:**
```java
// Get the API instance
GemsEconomyAPI api = new GemsEconomyAPI();

// Get a player's UUID
UUID playerUuid = player.getUniqueId();

// Get a specific currency
Currency currency = api.getCurrency("gems");

if (currency != null) {
    // Get player's balance
    double balance = api.getBalance(playerUuid, currency);
    System.out.println("Balance: " + balance);

    // Give the player 100 of the currency
    api.deposit(playerUuid, 100, currency);
}
```
