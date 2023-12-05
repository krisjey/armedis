
package com.github.armedis.http.service.request;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.event.annotation.BeforeTestClass;

public class ResponseDataTypeTest {

    @BeforeTestClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Test
    public void test() {
        ResponseDataType responseDataType = null;

        responseDataType = ResponseDataType.of("");

        assertThat(responseDataType).isNotNull();
        assertThat(responseDataType).isEqualTo(ResponseDataType.JSON);

        responseDataType = ResponseDataType.of(null);

        assertThat(responseDataType).isNotNull();
        assertThat(responseDataType).isEqualTo(ResponseDataType.JSON);

        responseDataType = ResponseDataType.of("json");

        assertThat(responseDataType).isNotNull();
        assertThat(responseDataType).isEqualTo(ResponseDataType.JSON);

        responseDataType = ResponseDataType.of("application/json");

        assertThat(responseDataType).isNotNull();
        assertThat(responseDataType).isEqualTo(ResponseDataType.JSON);

        responseDataType = ResponseDataType.of("text/plain");

        assertThat(responseDataType).isNotNull();
        assertThat(responseDataType).isEqualTo(ResponseDataType.PLAIN_TEXT);

        responseDataType = ResponseDataType.of("PLAIN_TEXT");

        assertThat(responseDataType).isNotNull();
        assertThat(responseDataType).isEqualTo(ResponseDataType.PLAIN_TEXT);

        responseDataType = ResponseDataType.of("TEXT");

        assertThat(responseDataType).isNotNull();
        assertThat(responseDataType).isEqualTo(ResponseDataType.PLAIN_TEXT);
    }
}
