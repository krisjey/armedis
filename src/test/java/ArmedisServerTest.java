import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.github.armedis.ArmedisServer;
import com.github.armedis.http.service.AbstractRedisServerTest;
import com.github.armedis.utils.NetworkUtils;

@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = ArmedisServer.class)
public class ArmedisServerTest extends AbstractRedisServerTest {

    @Test
    public void testStartup() {
        NetworkUtils networkUtils = new NetworkUtils();
        assertThat(networkUtils.getLocalIpAddress()).isNotNull();
    }

}
