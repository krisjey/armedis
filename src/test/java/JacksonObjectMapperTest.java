import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

public class JacksonObjectMapperTest {
    ObjectMapper mapper = new ObjectMapper();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Test
    public void test() {
        ObjectNode objectNode = mapper.createObjectNode();
        String source = objectNode.toString();

        assertThat(source).isNotNull();
        assertThat(source).isEqualTo("{}");
    }

    @Test
    public void testJsonNode() {
//        Jsonobject
        JsonNode convertResult = null;

        String emptyString = "";
        String justStartBrace = "{";
        String notClosedJsonString = "{asdasdf";
        String emptyJsonString = "{}";
        String helloWorldJsonString = "{\"hello\":\"world\"}";

        convertResult = mapper.valueToTree(null);
        assertThat(convertResult).isEqualTo(NullNode.getInstance());

        // just string
        convertResult = mapper.valueToTree(emptyString);
        assertThat(convertResult.isTextual()).isTrue();
        assertThat(convertResult.asText()).isEqualTo(emptyString);
        assertThat(convertResult).isInstanceOf(TextNode.class);

        final JsonNode testJson = convertResult;
        assertThatThrownBy(() -> {
            ((ObjectNode) testJson).put("hello", "world");
        }).isInstanceOf(ClassCastException.class);

//        assertThat(convertResult.get("hello").asText()).isEqualTo("world");

        // just string
        convertResult = mapper.valueToTree(justStartBrace);
        assertThat(convertResult.isTextual()).isTrue();
        assertThat(convertResult.asText()).isEqualTo(justStartBrace);
        assertThat(convertResult).isInstanceOf(TextNode.class);

        // just string
        convertResult = mapper.valueToTree(notClosedJsonString);
        assertThat(convertResult.isTextual()).isTrue();
        assertThat(convertResult.isValueNode()).isTrue();
        assertThat(convertResult.size()).isEqualTo(0);
        assertThat(convertResult.asText()).isEqualTo(notClosedJsonString);
        assertThat(convertResult).isInstanceOf(TextNode.class);

        // JSON 인지 확인.
        // json string
        convertResult = mapper.valueToTree(emptyJsonString);
        assertThat(convertResult.isTextual()).isTrue();
        assertThat(convertResult.isValueNode()).isTrue();
        assertThat(convertResult.size()).isEqualTo(0);
        assertThat(convertResult.asText()).isEqualTo(emptyJsonString);

        convertResult = mapper.valueToTree(helloWorldJsonString);
        assertThat(convertResult.isTextual()).isTrue();
        assertThat(convertResult.isValueNode()).isTrue();
        assertThat(convertResult.isObject()).isFalse();
        assertThat(convertResult.isContainerNode()).isFalse();
        assertThat(convertResult.size()).isEqualTo(0);
        assertThat(convertResult.asText()).isEqualTo(helloWorldJsonString);

        String from = "asdf";
        String beforeFrom = from;

        from = "new";

        assertThat(from).isNotEqualTo(beforeFrom);

        assertThat(from).isEqualTo("new");
        assertThat(beforeFrom).isEqualTo("asdf");
    }
}
