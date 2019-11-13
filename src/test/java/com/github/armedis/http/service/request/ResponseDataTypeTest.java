
package com.github.armedis.http.service.request;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.BeforeClass;
import org.junit.Test;

public class ResponseDataTypeTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Test
    public void test() {
        ResponseDataType json = null;

        json = ResponseDataType.of("");

        assertThat(json).isNotNull();
        assertThat(json).isEqualTo(ResponseDataType.JSON);

        json = ResponseDataType.of(null);

        assertThat(json).isNotNull();
        assertThat(json).isEqualTo(ResponseDataType.JSON);

        json = ResponseDataType.of("json");

        assertThat(json).isNotNull();
        assertThat(json).isEqualTo(ResponseDataType.JSON);

        json = ResponseDataType.of("application/json");

        assertThat(json).isNotNull();
        assertThat(json).isEqualTo(ResponseDataType.JSON);

        json = ResponseDataType.of("text/plain");

        assertThat(json).isNotNull();
        assertThat(json).isEqualTo(ResponseDataType.PLAIN_TEXT);

        json = ResponseDataType.of("PLAIN_TEXT");

        assertThat(json).isNotNull();
        assertThat(json).isEqualTo(ResponseDataType.PLAIN_TEXT);

        json = ResponseDataType.of("TEXT");

        assertThat(json).isNotNull();
        assertThat(json).isEqualTo(ResponseDataType.PLAIN_TEXT);
    }
}
