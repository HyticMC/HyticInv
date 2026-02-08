package dev.hytical.economy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("EconomyProviderType Tests")
class EconomyProviderTypeTest {

    @Test
    @DisplayName("VAULT should be parsed from uppercase 'VAULT'")
    void fromString_vault_uppercase() {
        assertEquals(EconomyProviderType.VAULT, EconomyProviderType.Companion.fromString("VAULT"));
    }

    @Test
    @DisplayName("PLAYER_POINTS should be parsed from uppercase 'PLAYER_POINTS'")
    void fromString_playerPoints_uppercase() {
        assertEquals(EconomyProviderType.PLAYER_POINTS, EconomyProviderType.Companion.fromString("PLAYER_POINTS"));
    }

    @ParameterizedTest
    @DisplayName("Should handle case-insensitive parsing for VAULT")
    @ValueSource(strings = { "vault", "Vault", "VAULT", "VaUlT", " vault ", " VAULT " })
    void fromString_vault_caseInsensitive(String input) {
        assertEquals(EconomyProviderType.VAULT, EconomyProviderType.Companion.fromString(input));
    }

    @ParameterizedTest
    @DisplayName("Should handle case-insensitive parsing for PLAYER_POINTS")
    @ValueSource(strings = { "player_points", "Player_Points", "PLAYER_POINTS", " player_points " })
    void fromString_playerPoints_caseInsensitive(String input) {
        assertEquals(EconomyProviderType.PLAYER_POINTS, EconomyProviderType.Companion.fromString(input));
    }

    @ParameterizedTest
    @DisplayName("Invalid values should fallback to VAULT")
    @ValueSource(strings = { "invalid", "NONE", "money", "", "   " })
    void fromString_invalid_fallbacksToVault(String input) {
        assertEquals(EconomyProviderType.VAULT, EconomyProviderType.Companion.fromString(input));
    }

    @ParameterizedTest
    @DisplayName("Null should fallback to VAULT")
    @NullSource
    void fromString_null_fallbacksToVault(String input) {
        assertEquals(EconomyProviderType.VAULT, EconomyProviderType.Companion.fromString(input));
    }
}
