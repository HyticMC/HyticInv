package dev.hytical;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("InsureInv Plugin Class Tests")
class InsureInvPluginTest {

    @Test
    @DisplayName("InsureInv class should be open (not final) for MockBukkit compatibility")
    void insureInv_isOpenClass() {
        int modifiers = InsureInv.class.getModifiers();
        assertFalse(java.lang.reflect.Modifier.isFinal(modifiers),
                "InsureInv should be an open class (not final) for MockBukkit to subclass");
    }

    @Test
    @DisplayName("InsureInv should extend JavaPlugin")
    void insureInv_extendsJavaPlugin() {
        assertTrue(org.bukkit.plugin.java.JavaPlugin.class.isAssignableFrom(InsureInv.class),
                "InsureInv should extend JavaPlugin");
    }
}
