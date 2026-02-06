# HyticInv

HyticInv is a robust inventory protection plugin for modern Minecraft servers (Paper, Spigot, and Folia). It allows players to safeguard their inventory upon death by consuming a "protection charge." These charges can be purchased using an in-game economy, creating a balanced and engaging server feature.

## Features

*   **Keep Inventory on Death:** Prevents item loss by consuming a protection charge.
*   **Economy Integration:** Utilizes Vault for players to buy protection charges.
*   **Toggleable Protection:** Players can enable or disable protection at will.
*   **Flexible Data Storage:** Supports MySQL, SQLite, and JSON for storing player data, with an automatic fallback system to ensure data integrity.
*   **Folia Support:** Built with modern server architectures in mind, including region-based scheduling for Folia.
*   **Admin Management:** Comprehensive commands for administrators to manage player charges, set prices, and reload configurations.
*   **Customizable Messages:** Fully customizable, MiniMessage-supported messages for a seamless server aesthetic.
*   **PlaceholderAPI Support:** Integrates with PlaceholderAPI to display player data.

## Installation

1.  Download the latest release of `HyticInv.jar`.
2.  Place the `.jar` file into your server's `/plugins` directory.
3.  Ensure you have **Vault** and a compatible economy plugin (like EssentialsX) installed.
4.  (Optional) Install **PlaceholderAPI** for placeholder support.
5.  Start or restart your server.
6.  Customize the generated `config.yml` file in `/plugins/HyticInv/` to your liking.

## Commands

HyticInv uses the base command `/hyticinv` (aliases: `/inv`, `/hinv`).

| Command                       | Description                                            | Permission       |
| :---------------------------- | :----------------------------------------------------- | :--------------- |
| `/hyticinv buy <amount>`      | Purchase a specified amount of protection charges.     | `hyticinv.use`   |
| `/hyticinv toggle`            | Enable or disable your inventory protection.           | `hyticinv.use`   |
| `/hyticinv info`              | View your current protection charges and status.       | `hyticinv.use`   |
| `/hyticinv help [page]`       | Displays the help menu.                                | `hyticinv.use`   |
| `/hyticinv reload`            | Reloads the plugin configuration.                      | `hyticinv.admin` |
| `/hyticinv set <player> <amount>`| Set a player's protection charges to a specific amount. | `hyticinv.admin` |
| `/hyticinv setprice <price>`  | Set the price for a single protection charge.          | `hyticinv.admin` |
| `/hyticinv setmax <amount>`   | Set the maximum charges a player can hold.             | `hyticinv.admin` |
| `/hyticinv toggle <player>`   | Toggle inventory protection for another player.        | `hyticinv.admin` |
| `/hyticinv info <player>`     | View another player's protection charges and status.   | `hyticinv.admin` |

## Permissions

*   `hyticinv.use` - Grants access to basic user commands (`/buy`, `/toggle`, `/info`, `/help`). (Default: `true`)
*   `hyticinv.admin` - Grants access to all administrative commands and the ability to manage other players. (Default: `op`)

## Configuration

The `config.yml` is divided into three main sections:

### `storage`

Configure how player data is stored.

*   `method`: The desired storage method. Can be `mysql`, `sqlite`, or `json`. The plugin will attempt to initialize in this order and fall back to a simpler method if the preferred one fails.
*   `mysql`: Credentials and connection pool settings for your MySQL database.
*   `sqlite`: Path for the SQLite database file (relative to the plugin folder).
*   `json`: Path for the JSON data file (relative to the plugin folder).

### `economy`

Manage the economic aspects of the plugin.

*   `price-per-charge`: The cost for one protection charge.
*   `max-charges-per-player`: The maximum number of charges a player can accumulate.

### `messages`

Customize all user-facing messages.

*   `enable-prefix`: Toggles a global message prefix.
*   `prefix`: The prefix to be displayed before messages.
*   All other keys correspond to specific messages for commands, events, and errors. These messages support the [MiniMessage](https://docs.adventure.kyori.net/minimessage/format.html) format for advanced color and style formatting.

## License

This project is licensed under the GNU General Public License v3.0. Please see the `LICENSE` file for more details.