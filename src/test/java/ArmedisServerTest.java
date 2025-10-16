import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

import com.github.armedis.ArmedisServer;
import com.github.armedis.http.service.AbstractRedisServerTest;

@ActiveProfiles("testbed")
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = ArmedisServer.class)
public class ArmedisServerTest extends AbstractRedisServerTest {

    @Test
    public void test() {
        LocalIpAddress localIpAddress = new LocalIpAddress();

        String ipAddress = localIpAddress.getLocalIpAddress();

        // local IP address, This address connectable at out of the system.
//        assertThat(ipAddress).isEqualTo("175.123.88.73");
        assertThat(ipAddress).isNotNull();
    }

}
