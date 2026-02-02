package dev.hytical.command

import dev.hytical.HyticInv
import dev.hytical.managers.ConfigManager
import dev.hytical.managers.EconomyManager
import dev.hytical.messaging.MessageManager
import dev.hytical.storages.StorageManager
import dev.hytical.utils.PlaceholderUtil
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class HyticInvCommand(
    private val plugin: HyticInv,
    private val configManager: ConfigManager,
    private val storageManager: StorageManager,
    private val economyManager: EconomyManager,
    private val messageManager: MessageManager
) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.isEmpty()) {
            sendHelp(sender, 1)
            return true
        }

        when (args[0].lowercase()) {
            "buy" -> handleBuy(sender, args)
            "toggle" -> handleToggle(sender, args)
            "info" -> handleInfo(sender, args)
            "set" -> handleSet(sender, args)
            "setprice" -> handleSetPrice(sender, args)
            "setmax" -> handleSetMax(sender, args)
            "reload" -> handleReload(sender, args)
            "help" -> {
                val page = args.getOrNull(1)?.toIntOrNull() ?: 1
                sendHelp(sender, page)
            }

            else -> sendHelp(sender, 1)
        }

        return true
    }

    private fun handleBuy(sender: CommandSender, args: Array<out String>) {
        if (sender !is Player) {
            messageManager.sendRawMessage(sender, "<red>This command can only be used by players!</red>")
            return
        }

        if (!sender.hasPermission("hyticinv.use")) {
            messageManager.sendMessage(sender, "no-permission")
            return
        }

        if (!economyManager.isAvailable()) {
            messageManager.sendRawMessage(sender, "<red>Economy system is not available!</red>")
            return
        }

        if (args.size < 2) {
            messageManager.sendRawMessage(sender, "<red>Usage: /elyinv buy <amount></red>")
            return
        }

        val amount = args[1].toIntOrNull()
        if (amount == null || amount <= 0) {
            messageManager.sendMessage(sender, "buy-invalid-amount")
            return
        }

        val playerData = storageManager.getPlayerData(sender)
        val maxCharges = configManager.getMaxCharges()
        val pricePerCharge = configManager.getPricePerCharge()
        val totalPrice = amount * pricePerCharge

        if (playerData.charges + amount > maxCharges) {
            val canBuy = maxCharges - playerData.charges
            messageManager.sendMessage(
                sender, "buy-max-exceeded",
                PlaceholderUtil.createResolver("amount" to canBuy.toString())
            )
            return
        }

        val balance = economyManager.getBalance(sender)
        if (!economyManager.hasBalance(sender, totalPrice)) {
            messageManager.sendMessage(
                sender, "buy-insufficient-funds",
                PlaceholderUtil.economyResolver(totalPrice, balance, amount)
            )
            return
        }

        if (economyManager.withdraw(sender, totalPrice)) {
            playerData.addCharges(amount)
            storageManager.savePlayerData(playerData)

            messageManager.sendMessage(
                sender, "buy-success",
                PlaceholderUtil.economyResolver(totalPrice, balance - totalPrice, amount),
                PlaceholderUtil.chargesResolver(playerData.charges, maxCharges)
            )
        } else {
            messageManager.sendRawMessage(sender, "<red>Transaction failed! Please try again.</red>")
        }
    }

    private fun handleToggle(sender: CommandSender, args: Array<out String>) {
        if (args.size >= 2) {
            if (!sender.hasPermission("hyticinv.admin")) {
                messageManager.sendMessage(sender, "no-permission")
                return
            }

            val targetPlayer = Bukkit.getPlayerExact(args[1])
            if (targetPlayer == null) {
                messageManager.sendMessage(
                    sender, "player-not-found",
                    PlaceholderUtil.createResolver("player" to args[1])
                )
                return
            }

            val playerData = storageManager.getPlayerData(targetPlayer)
            playerData.protectionEnabled = !playerData.protectionEnabled
            storageManager.savePlayerData(playerData)

            val messageKey = if (playerData.protectionEnabled) "toggle-on" else "toggle-off"
            messageManager.sendMessage(
                targetPlayer, messageKey,
                PlaceholderUtil.chargesResolver(playerData.charges, configManager.getMaxCharges())
            )

            if (sender != targetPlayer) {
                messageManager.sendRawMessage(
                    sender,
                    "<green>Toggled protection ${if (playerData.protectionEnabled) "on" else "off"} for ${targetPlayer.name}</green>"
                )
            }
        } else {
            if (sender !is Player) {
                messageManager.sendRawMessage(sender, "<red>This command can only be used by players!</red>")
                return
            }

            if (!sender.hasPermission("hyticinv.use")) {
                messageManager.sendMessage(sender, "no-permission")
                return
            }

            val playerData = storageManager.getPlayerData(sender)
            playerData.protectionEnabled = !playerData.protectionEnabled
            storageManager.savePlayerData(playerData)

            val messageKey = if (playerData.protectionEnabled) "toggle-on" else "toggle-off"
            messageManager.sendMessage(
                sender, messageKey,
                PlaceholderUtil.chargesResolver(playerData.charges, configManager.getMaxCharges())
            )
        }
    }

    private fun handleInfo(sender: CommandSender, args: Array<out String>) {
        val targetPlayer: Player
        val targetName: String

        if (args.size >= 2) {
            if (!sender.hasPermission("hyticinv.admin")) {
                messageManager.sendMessage(sender, "no-permission")
                return
            }

            targetPlayer = Bukkit.getPlayerExact(args[1]) ?: run {
                messageManager.sendMessage(
                    sender, "player-not-found",
                    PlaceholderUtil.createResolver("player" to args[1])
                )
                return
            }
            targetName = targetPlayer.name
        } else {
            if (sender !is Player) {
                messageManager.sendRawMessage(sender, "<red>This command can only be used by players!</red>")
                return
            }

            if (!sender.hasPermission("hyticinv.use")) {
                messageManager.sendMessage(sender, "no-permission")
                return
            }

            targetPlayer = sender
            targetName = sender.name
        }

        val playerData = storageManager.getPlayerData(targetPlayer)
        val maxCharges = configManager.getMaxCharges()

        val statusMessage =
            configManager.getMessage(if (playerData.protectionEnabled) "status-enabled" else "status-disabled")

        messageManager.sendMessage(sender, "info-header")
        messageManager.sendMessage(
            sender, "info-player",
            PlaceholderUtil.createResolver("player" to targetName)
        )
        messageManager.sendMessage(
            sender, "info-charges",
            PlaceholderUtil.chargesResolver(playerData.charges, maxCharges)
        )
        messageManager.sendMessage(
            sender, "info-status",
            PlaceholderUtil.createResolver("status" to statusMessage)
        )
        messageManager.sendMessage(
            sender, "info-total-purchases",
            PlaceholderUtil.statsResolver(playerData.totalChargesPurchased, playerData.protectionActivations)
        )
        messageManager.sendMessage(
            sender, "info-usage-count",
            PlaceholderUtil.statsResolver(playerData.totalChargesPurchased, playerData.protectionActivations)
        )
        messageManager.sendMessage(sender, "info-footer")
    }

    private fun handleSet(sender: CommandSender, args: Array<out String>) {
        if (!sender.hasPermission("hyticinv.admin")) {
            messageManager.sendMessage(sender, "no-permission")
            return
        }

        if (args.size < 3) {
            messageManager.sendRawMessage(sender, "<red>Usage: /elyinv set <player> <amount></red>")
            return
        }

        val targetPlayer = Bukkit.getPlayerExact(args[1])
        if (targetPlayer == null) {
            messageManager.sendMessage(
                sender, "player-not-found",
                PlaceholderUtil.createResolver("player" to args[1])
            )
            return
        }

        val amount = args[2].toIntOrNull()
        if (amount == null || amount < 0) {
            messageManager.sendMessage(sender, "invalid-amount")
            return
        }

        val playerData = storageManager.getPlayerData(targetPlayer)
        playerData.updateCharges(amount)
        storageManager.savePlayerData(playerData)

        messageManager.sendMessage(
            sender, "admin-set-success",
            PlaceholderUtil.playerResolver(targetPlayer),
            PlaceholderUtil.createResolver("amount" to amount.toString())
        )
    }

    private fun handleSetPrice(sender: CommandSender, args: Array<out String>) {
        if (!sender.hasPermission("hyticinv.admin")) {
            messageManager.sendMessage(sender, "no-permission")
            return
        }

        if (args.size < 2) {
            messageManager.sendRawMessage(sender, "<red>Usage: /elyinv setprice <amount></red>")
            return
        }

        val price = args[1].toDoubleOrNull()
        if (price == null || price <= 0) {
            messageManager.sendMessage(sender, "invalid-amount")
            return
        }

        configManager.setPricePerCharge(price)
        messageManager.sendMessage(
            sender, "price-updated",
            PlaceholderUtil.createResolver("price" to String.format("%.2f", price))
        )
    }

    private fun handleSetMax(sender: CommandSender, args: Array<out String>) {
        if (!sender.hasPermission("hyticinv.admin")) {
            messageManager.sendMessage(sender, "no-permission")
            return
        }

        if (args.size < 2) {
            messageManager.sendRawMessage(sender, "<red>Usage: /elyinv setmax <amount></red>")
            return
        }

        val max = args[1].toIntOrNull()
        if (max == null || max <= 0) {
            messageManager.sendMessage(sender, "invalid-amount")
            return
        }

        configManager.setMaxCharges(max)
        messageManager.sendMessage(
            sender, "max-updated",
            PlaceholderUtil.createResolver("max" to max.toString())
        )
    }

    private fun handleReload(sender: CommandSender, args: Array<out String>) {
        if (!sender.hasPermission("hyticinv.admin")) {
            messageManager.sendMessage(sender, "no-permission")
            return
        }

        val oldBackend = storageManager.getCurrentBackendName()
        configManager.reload()
        storageManager.reload()

        val newBackend = storageManager.getCurrentBackendName()

        messageManager.sendMessage(sender, "reload-complete")

        if (oldBackend != newBackend) {
            messageManager.sendMessage(
                sender, "reload-storage-changed",
                PlaceholderUtil.methodResolver(newBackend)
            )
        }
    }

    private fun sendHelp(sender: CommandSender, page: Int) {
        val helpCommands = mutableListOf<String>()

        helpCommands.add("help-buy")
        helpCommands.add("help-toggle")
        helpCommands.add("help-info")

        if (sender.hasPermission("hyticinv.admin")) {
            helpCommands.add("help-set")
            helpCommands.add("help-setprice")
            helpCommands.add("help-setmax")
            helpCommands.add("help-reload")
        }

        helpCommands.add("help-help")

        val commandsPerPage = 5
        val totalPages = (helpCommands.size + commandsPerPage - 1) / commandsPerPage
        val actualPage = page.coerceIn(1, totalPages)

        val startIndex = (actualPage - 1) * commandsPerPage
        val endIndex = (startIndex + commandsPerPage).coerceAtMost(helpCommands.size)

        messageManager.sendMessage(
            sender, "help-header",
            PlaceholderUtil.paginationResolver(actualPage, totalPages)
        )

        for (i in startIndex until endIndex) {
            messageManager.sendMessage(sender, helpCommands[i])
        }

        messageManager.sendMessage(sender, "help-footer")
    }
}