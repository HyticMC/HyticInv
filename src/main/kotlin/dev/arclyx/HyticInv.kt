package dev.arclyx

import dev.arclyx.managers.ConfigManager
import org.bukkit.plugin.java.JavaPlugin

class HyticInv : JavaPlugin() {
    private var econ: Boolean = false

    private lateinit var configManager: ConfigManager

    override fun onEnable() {
        configManager = ConfigManager(this)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
