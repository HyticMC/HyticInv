package dev.hytical.economy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("NoneEconomyProvider Tests")
class NoneEconomyProviderTest {

    @Test
    @DisplayName("isAvailable should always return false")
    void isAvailable_returnsFalse() {
        assertFalse(NoneEconomyProvider.INSTANCE.isAvailable());
    }

    @Test
    @DisplayName("formatAmount should return properly formatted string with 2 decimal places")
    void formatAmount_returnsFormattedString() {
        assertEquals("100.00", NoneEconomyProvider.INSTANCE.formatAmount(100.0));
        assertEquals("0.00", NoneEconomyProvider.INSTANCE.formatAmount(0.0));
        assertEquals("99.99", NoneEconomyProvider.INSTANCE.formatAmount(99.99));
    }

    @Test
    @DisplayName("formatAmount should round to 2 decimal places")
    void formatAmount_roundsCorrectly() {
        String result = NoneEconomyProvider.INSTANCE.formatAmount(1234.5678);
        assertTrue(result.startsWith("1234.5"), "Expected format to start with 1234.5, got: " + result);
    }
}
