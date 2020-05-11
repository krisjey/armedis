import static org.assertj.core.api.Assertions.assertThat;

import org.junit.BeforeClass;
import org.junit.Test;

public class LocalIpAddressTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Test
    public void test() {
        LocalIpAddress localIpAddress = new LocalIpAddress();

        String ipAddress = localIpAddress.getLocalIpAddress();

        // local IP address, This address connectable at out of the system.
        assertThat(ipAddress).isEqualTo("175.123.88.73");
//        assertThat(ipAddress).isEqualTo("192.168.56.101");
    }

}
