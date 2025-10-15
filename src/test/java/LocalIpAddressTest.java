import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.event.annotation.BeforeTestClass;

public class LocalIpAddressTest {

    @BeforeTestClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Test
    public void test() {
        LocalIpAddress localIpAddress = new LocalIpAddress();

        String ipAddress = localIpAddress.getLocalIpAddress();

        // local IP address, This address connectable at out of the system.
        //assertThat(ipAddress).isEqualTo("175.123.88.73");
    }
}
