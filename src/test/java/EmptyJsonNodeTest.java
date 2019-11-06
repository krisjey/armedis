import static org.assertj.core.api.Assertions.assertThat;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class EmptyJsonNodeTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Test
    public void test() {
        ObjectMapper mapper = new ObjectMapper();
        assertThat(mapper.createObjectNode().toString()).isNotNull();
        System.out.println(mapper.createObjectNode().toString());
    }
}
