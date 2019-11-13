
package com.github.armedis.http.service.request;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.common.MediaType;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JsonObjectNodeTest {
    protected static final ObjectMapper mapper = new ObjectMapper();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Test
    public void test1() throws IOException {
        String jsonString = "{\"userId\":\"ddd051001\",\"location\":\"vod_editor\",\"contentGrade\":0}";

        JsonNode json = mapper.readTree(jsonString);

        assertThat(json).isNotNull();
        assertThat(json.toString()).isEqualTo(jsonString);
    }

    @Test
    public void testPerformanceToString() throws IOException {
        String jsonString = "{\"userId\":\"ddd051001\",\"location\":\"vod_editor\",\"contentGrade\":0}";

        JsonNode json = mapper.readTree(jsonString);

        assertThat(json).isNotNull();

        for (int i = 0; i < 10000000; i++) {
            assertThat(json.toString()).isEqualTo(jsonString);
        }
    }

    @Test
    public void testPerformanceWriteValue() throws IOException {
        String jsonString = "{\"userId\":\"ddd051001\",\"location\":\"vod_editor\",\"contentGrade\":0}";

        JsonNode json = mapper.readTree(jsonString);

        assertThat(json).isNotNull();

        for (int i = 0; i < 10000000; i++) {
            assertThat(mapper.writeValueAsString(json)).isEqualTo(jsonString);
        }
    }

    @Test
    public void testHttpResponse() {
        String data = null;

        HttpResponse response = HttpResponse.of(HttpStatus.OK, MediaType.JSON_UTF_8, data);
        
        assertThat(response).isNotNull();

    }
}
