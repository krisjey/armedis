package com.github.armedis.ui;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

class ManagementUiRegressionTest {

    @Test
    void acceptsOnlyIntegerText() throws IOException {
        String management = resource("static/ajax/pages-armedis-management.html");

        assertTrue(management.contains("type=\"number\" step=\"1\""));
        assertTrue(management.contains("val = val.trim()"));
        assertTrue(management.contains("if (!/^-?\\d+$/.test(val))"));
        assertFalse(management.contains("if (isNaN(val))"));
    }

    @Test
    void categoryTabsDoNotNavigate() throws IOException {
        String management = resource("static/ajax/pages-armedis-management.html");

        assertTrue(management.contains("<button type=\"button\" class=\"nav-link active\" data-cat=\"All\">"));
        assertTrue(management.contains("<button type=\"button\" class=\"nav-link\" data-cat=\"${category}\">"));
        assertTrue(management.contains("var tab = e.target.closest(\".nav-link\")"));
        assertTrue(management.contains("if(tab && ul.contains(tab))"));
        assertTrue(management.contains("renderTable(tab.dataset.cat"));
        assertFalse(management.contains("href=\"#\""));
    }

    @Test
    void preservesRedisMemoryUnitSemantics() throws IOException {
        String management = resource("static/ajax/pages-armedis-management.html");

        assertTrue(management.contains("{ suffix: \"gb\", bytes: 1073741824n }"));
        assertTrue(management.contains("{ suffix: \"g\", bytes: 1000000000n }"));
        assertTrue(management.contains("{ suffix: \"mb\", bytes: 1048576n }"));
        assertTrue(management.contains("{ suffix: \"m\", bytes: 1000000n }"));
        assertTrue(management.contains("{ suffix: \"kb\", bytes: 1024n }"));
        assertTrue(management.contains("{ suffix: \"k\", bytes: 1000n }"));
        assertTrue(management.contains("bytes % units[i].bytes === 0n"));
        assertTrue(management.contains("var memoryPattern = /^\\d+(k|kb|m|mb|g|gb)?$/i"));
        assertTrue(management.contains("refreshCurrentConfigValue(currentEdit)"));
        assertFalse(management.contains("getMemoryCapacityUnit(i.currentValue, \"kbyte\")"));
        assertFalse(management.contains("getMemoryCapacityUnit(currentVal, \"kbyte\")"));
        assertFalse(management.contains("getMemoryCapacityUnit(curr, \"kbyte\")"));
        assertFalse(management.contains(".textContent = val"));
    }

    private static String resource(String path) throws IOException {
        try (InputStream stream = ManagementUiRegressionTest.class.getClassLoader().getResourceAsStream(path)) {
            if (stream == null) {
                throw new IOException("Resource not found: " + path);
            }
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
