package dev.hytical.command

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class HyticInvTabCompleter : TabCompleter {

    private val subcommands = listOf(
        "buy", "toggle", "info", "set", "setprice", "setmax", "reload", "help"
    )

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): List<String> {
        when (args.size) {
            1 -> {
                return subcommands.filter { it.startsWith(args[0].lowercase()) }
            }

            2 -> {
                when (args[0].lowercase()) {
                    "toggle", "info", "set" -> {
                        if (sender.hasPermission("elyinv.admin")) {
                            return Bukkit.getOnlinePlayers()
                                .map { it.name }
                                .filter { it.lowercase().startsWith(args[1].lowercase()) }
                        }
                    }

                    "buy" -> {
                        return listOf("1", "5", "10")
                    }

                    "setprice" -> {
                        return listOf("100", "200", "500")
                    }

                    "setmax" -> {
                        return listOf("10", "20", "50")
                    }

                    "help" -> {
                        return listOf("1", "2")
                    }
                }
            }

            3 -> {
                when (args[0].lowercase()) {
                    "set" -> {
                        return listOf("1", "5", "10", "0")
                    }
                }
            }
        }

        return emptyList()
    }
}