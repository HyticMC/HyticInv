package dev.hytical;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("HyticInv Plugin Class Tests")
class HyticInvPluginTest {

    @Test
    @DisplayName("HyticInv class should be open (not final) for MockBukkit compatibility")
    void hyticInv_isOpenClass() {
        int modifiers = HyticInv.class.getModifiers();
        assertFalse(java.lang.reflect.Modifier.isFinal(modifiers),
                "HyticInv should be an open class (not final) for MockBukkit to subclass");
    }

    @Test
    @DisplayName("HyticInv should extend JavaPlugin")
    void hyticInv_extendsJavaPlugin() {
        assertTrue(org.bukkit.plugin.java.JavaPlugin.class.isAssignableFrom(HyticInv.class),
                "HyticInv should extend JavaPlugin");
    }
}
