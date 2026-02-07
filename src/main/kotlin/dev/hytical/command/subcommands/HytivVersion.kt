package dev.hytical.command.subcommands

import dev.hytical.HyticInv
import dev.hytical.command.CommandContext
import dev.hytical.command.HyticSubCommand
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.command.CommandSender

class HytivVersion(
    private val plugin: HyticInv,
) : HyticSubCommand {
    val i = plugin.buildInfo
    override val name: String = "version"
    override val permission: String = "hyticinv.admin"
    override val requiresPlayer: Boolean = false

    override fun execute(context: CommandContext) {
        val message = MiniMessage.miniMessage().deserialize(
            """
            <gray>──────────────</gray>
            <gold><bold>${plugin.name}</bold></gold> <gray>v</gray><white>${i.buildVersion}</white>
            <gray>Branch:</gray> <aqua>${i.branch}</aqua>
            <gray>Commit:</gray> <yellow>${i.commitIdAbbrev}</yellow>
            <gray>Message:</gray> <white>${i.commitMessage}</white>
            <gray>Built:</gray> <white>${i.buildTime}</white>
            <gray>Dirty:</gray> <${if (i.isDirty) "red" else "green"}>${i.isDirty}</${if (i.isDirty) "red" else "green"}>
            <gray>──────────────</gray>
            """.trimIndent()
        )
        context.sender.sendMessage(message)
    }

    override fun tabComplete(
        sender: CommandSender,
        args: Array<String>
    ): List<String> = mutableListOf()
}