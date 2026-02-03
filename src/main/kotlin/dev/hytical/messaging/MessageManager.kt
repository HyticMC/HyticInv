package dev.hytical.messaging

import dev.hytical.HyticInv
import dev.hytical.managers.ConfigManager
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class MessageManager(
    private val plugin: HyticInv,
    private val configManager: ConfigManager
) {
    private val miniMessage: MiniMessage = MiniMessage.miniMessage()

    fun shutdown() {
        // No cleanup needed - Paper provides Adventure natively
    }

    fun sendMessage(sender: CommandSender, messageKey: String, vararg resolvers: TagResolver) {
        val rawMessage = configManager.getMessage(messageKey)
        val component = parseMessage(rawMessage, *resolvers)
        sender.sendMessage(component)
    }

    fun sendMessage(player: Player, messageKey: String, vararg resolvers: TagResolver) {
        val rawMessage = configManager.getMessage(messageKey)
        val component = parseMessage(rawMessage, *resolvers)
        player.sendMessage(component)
    }

    fun parseMessage(message: String, vararg resolvers: TagResolver): Component {
        return if (resolvers.isEmpty()) {
            miniMessage.deserialize(message)
        } else {
            miniMessage.deserialize(message, TagResolver.resolver(*resolvers))
        }
    }

    fun sendRawMessage(sender: CommandSender, message: String) {
        val component = miniMessage.deserialize(message)
        sender.sendMessage(component)
    }
}