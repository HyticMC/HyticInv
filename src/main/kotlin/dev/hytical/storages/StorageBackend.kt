package dev.hytical.storages

import dev.hytical.model.PlayerData
import java.util.UUID

interface StorageBackend {

    fun initialize(): Boolean

    fun close()

    fun loadPlayerData(uuid: UUID): PlayerData?

    fun savePlayerData(playerData: PlayerData)

    fun savePlayerDataAsync(playerData: PlayerData)

    fun isHealthy(): Boolean

    fun getName(): String
}