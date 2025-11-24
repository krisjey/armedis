/**
 * 
 */
package com.github.armedis.http.service.management.configs;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.armedis.http.service.management.configs.AllowedConfigCommands.ConfigCommand;

class AllowedConfigCommandsTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    @DisplayName("모든 config commands 조회")
    void testGetAllCommands() {
        Collection<ConfigCommand> commands = AllowedConfigCommands.all();

        assertNotNull(commands);
        assertThat(commands.size()).isGreaterThan(10);
    }

    @Test
    @DisplayName("존재하는 config key 조회")
    void testGetExistingCommand() {
        ConfigCommand cmd = AllowedConfigCommands.get("maxmemory");

        assertNotNull(cmd);
        assertEquals("maxmemory", cmd.getKey());
        assertEquals("Memory", cmd.getCategory());
        assertEquals("memoryunit", cmd.getDataType());
    }

    @Test
    @DisplayName("대소문자 구분 없이 config key 조회")
    void testGetCommandCaseInsensitive() {
        ConfigCommand cmd1 = AllowedConfigCommands.get("MAXMEMORY");
        ConfigCommand cmd2 = AllowedConfigCommands.get("maxMemory");
        ConfigCommand cmd3 = AllowedConfigCommands.get("maxmemory");

        assertNotNull(cmd1);
        assertNotNull(cmd2);
        assertNotNull(cmd3);
        assertEquals(cmd1.getKey(), cmd2.getKey());
        assertEquals(cmd2.getKey(), cmd3.getKey());
    }

    @Test
    @DisplayName("존재하지 않는 config key 조회 시 예외 발생")
    void testGetNonExistentCommand() {
        assertThrows(NoSuchElementException.class, () -> AllowedConfigCommands.get("invalid-key"));
    }

    @Test
    @DisplayName("config key 존재 여부 확인")
    void testContains() {
        assertTrue(AllowedConfigCommands.contains("maxmemory"));
        assertTrue(AllowedConfigCommands.contains("LOGLEVEL"));
        assertFalse(AllowedConfigCommands.contains("invalid-key"));
    }

    @Test
    @DisplayName("MEMORYUNIT 타입 validation - 유효한 값")
    void testMemoryUnitValidation() {
        ConfigCommand cmd = AllowedConfigCommands.get("maxmemory");

        assertTrue(cmd.isValid("1024"));
        assertTrue(cmd.isValid("1k"));
        assertTrue(cmd.isValid("1K"));
        assertTrue(cmd.isValid("512m"));
        assertTrue(cmd.isValid("2gb"));
        assertTrue(cmd.isValid("1GB"));
    }

    @Test
    @DisplayName("MEMORYUNIT 타입 validation - 무효한 값")
    void testMemoryUnitInvalidValidation() {
        ConfigCommand cmd = AllowedConfigCommands.get("maxmemory");

        assertFalse(cmd.isValid("abc"));
        assertFalse(cmd.isValid("1x"));
        assertFalse(cmd.isValid("-100"));
    }

    @Test
    @DisplayName("MEMORYUNIT 타입 normalization")
    void testMemoryUnitNormalization() {
        ConfigCommand cmd = AllowedConfigCommands.get("maxmemory");

        cmd.setCurrentValue("1GB");
        assertEquals("1gb", cmd.getCurrentValue());

        cmd.setCurrentValue("512M");
        assertEquals("512m", cmd.getCurrentValue());
    }

    @Test
    @DisplayName("INTEGER 타입 validation")
    void testIntegerValidation() {
        ConfigCommand cmd = AllowedConfigCommands.get("maxclients");

        assertTrue(cmd.isValid("100"));
        assertTrue(cmd.isValid("0"));
        assertTrue(cmd.isValid("-1"));
        assertFalse(cmd.isValid("abc"));
        assertFalse(cmd.isValid("12.5"));
    }

    @Test
    @DisplayName("DROPDOWN 타입 validation - 유효한 값")
    void testDropdownValidation() {
        ConfigCommand cmd = AllowedConfigCommands.get("maxmemory-policy");

        assertTrue(cmd.isValid("noeviction"));
        assertTrue(cmd.isValid("allkeys-lru"));
        assertTrue(cmd.isValid("VOLATILE-LRU")); // 대소문자 무시
    }

    @Test
    @DisplayName("DROPDOWN 타입 validation - 무효한 값")
    void testDropdownInvalidValidation() {
        ConfigCommand cmd = AllowedConfigCommands.get("maxmemory-policy");

        assertFalse(cmd.isValid("invalid-policy"));
        assertFalse(cmd.isValid(""));
    }

    @Test
    @DisplayName("DROPDOWN 타입 무효한 값 설정 시 예외 발생")
    void testDropdownInvalidValueThrowsException() {
        ConfigCommand cmd = AllowedConfigCommands.get("loglevel");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> cmd.setCurrentValue("invalid-level"));

        assertTrue(ex.getMessage().contains("Invalid value"));
        assertTrue(ex.getMessage().contains("allowed:"));
    }

    @Test
    @DisplayName("DROPDOWN options 포함 여부")
    void testDropdownHasOptions() {
        ConfigCommand cmd = AllowedConfigCommands.get("appendfsync");

        assertNotNull(cmd.getOptions());
        assertEquals(3, cmd.getOptions().size());
        assertTrue(cmd.getOptions().contains("always"));
        assertTrue(cmd.getOptions().contains("everysec"));
        assertTrue(cmd.getOptions().contains("no"));
    }

    @Test
    @DisplayName("non-DROPDOWN 타입은 options가 null")
    void testNonDropdownHasNoOptions() {
        ConfigCommand cmd = AllowedConfigCommands.get("maxmemory");

        assertNull(cmd.getOptions());
    }

    @Test
    @DisplayName("currentValue 설정 및 조회")
    void testSetAndGetCurrentValue() {

        ConfigCommand cmd = AllowedConfigCommands.get("timeout");

        // TODO 현재 값 확인. 두번 호출되고 첫 번째는 "" 두 번째는 "0"
        System.out.println(cmd.getKey() + "[" + cmd.getCurrentValue() + "]");
        if (AllowedConfigCommands.isInitialized()) {
            // do nothing.
        }
        else {
            assertThat(cmd.getCurrentValue()).isEqualTo("");
        }

        cmd.setCurrentValue("300");
        assertEquals("300", cmd.getCurrentValue());
    }

    @Test
    @DisplayName("null 값 설정 시 예외 발생")
    void testSetNullValueThrowsException() {
        ConfigCommand cmd = AllowedConfigCommands.get("maxclients");

        assertThrows(IllegalArgumentException.class, () -> cmd.setCurrentValue(null));
    }

    @Test
    @DisplayName("전체 config commands JSON 직렬화")
    void testToJson() throws JsonProcessingException {
        String json = AllowedConfigCommands.toJsonString();

        assertNotNull(json);
        assertFalse(json.isEmpty());

        JsonNode root = MAPPER.readTree(json);
        assertTrue(root.has("key-description"));
        assertTrue(root.has("dataType-description"));
        assertTrue(root.has("configKeys"));

        JsonNode configKeys = root.get("configKeys");
        assertTrue(configKeys.isArray());
        assertThat(configKeys.size()).isGreaterThan(10);
    }

    @Test
    @DisplayName("JSON 출력 포맷 검증")
    void testJsonFormat() throws JsonProcessingException {
        String json = AllowedConfigCommands.toJsonString();
        JsonNode root = MAPPER.readTree(json);

        assertEquals("key, currentValue, category, description, dataType, options",
                root.get("key-description").asText());

        assertEquals("number, text, dropdown, memoryunit", root.get("dataType-description").asText());
    }

    @Test
    @DisplayName("개별 config command JSON 직렬화")
    void testToJsonSingleCommand() throws JsonProcessingException {
        String json = AllowedConfigCommands.toJson("maxmemory");

        assertNotNull(json);
        JsonNode node = MAPPER.readTree(json);

        assertEquals("maxmemory", node.get("key").asText());
        assertEquals("Memory", node.get("category").asText());
        assertEquals("memoryunit", node.get("dataType").asText());
        assertEquals("최대 메모리 사용량 (바이트)", node.get("description").asText());
    }

    @Test
    @DisplayName("JSON에 options 필드 포함 여부 확인")
    void testJsonOptionsField() throws JsonProcessingException {
        // DROPDOWN 타입은 options 포함
        String json1 = AllowedConfigCommands.toJson("loglevel");
        JsonNode node1 = MAPPER.readTree(json1);
        assertTrue(node1.has("options"));
        assertTrue(node1.get("options").isArray());

        // non-DROPDOWN 타입은 options 미포함
        String json2 = AllowedConfigCommands.toJson("maxmemory");
        JsonNode node2 = MAPPER.readTree(json2);
        assertFalse(node2.has("options"));
        System.out.println(AllowedConfigCommands.all());
    }

    @ParameterizedTest
    @ValueSource(strings = { "maxmemory", "maxmemory-policy", "maxmemory-samples", "activedefrag",
            "active-defrag-threshold-upper", "active-defrag-threshold-lower", "active-defrag-ignore-bytes", "hz",
            "maxclients", "timeout", "save", "appendfsync", "tcp-keepalive", "loglevel", "slowlog-log-slower-than",
            "cluster-node-timeout" })
    @DisplayName("모든 정의된 config keys 존재 확인")
    void testAllDefinedKeysExist(String key) {
        assertTrue(AllowedConfigCommands.contains(key));
        assertDoesNotThrow(() -> AllowedConfigCommands.get(key));
    }

    @Test
    @DisplayName("DataType enum to string 변환")
    void testDataTypeToString() {
        ConfigCommand cmd1 = AllowedConfigCommands.get("maxmemory");
        assertEquals("memoryunit", cmd1.getDataType());

        ConfigCommand cmd2 = AllowedConfigCommands.get("maxclients");
        assertEquals("integer", cmd2.getDataType());

        ConfigCommand cmd3 = AllowedConfigCommands.get("save");
        assertEquals("string", cmd3.getDataType());

        ConfigCommand cmd4 = AllowedConfigCommands.get("loglevel");
        assertEquals("dropdown", cmd4.getDataType());
    }

    @Test
    @DisplayName("thread-safety: 동시에 currentValue 변경")
    void testThreadSafety() throws InterruptedException {
        ConfigCommand cmd = AllowedConfigCommands.get("maxclients");

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                cmd.setCurrentValue(String.valueOf(i));
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 100; i < 200; i++) {
                cmd.setCurrentValue(String.valueOf(i));
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        // 값이 정상적으로 설정되었는지 확인
        assertNotNull(cmd.getCurrentValue());
        assertDoesNotThrow(() -> Integer.parseInt(cmd.getCurrentValue()));
    }
}
