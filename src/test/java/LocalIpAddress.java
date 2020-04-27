import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class LocalIpAddress {

    public String getLocalIpAddress() {
        String localIpAddr = null;

        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("1.1.1.1"), 10002);
            localIpAddr = socket.getLocalAddress().getHostAddress();
        }
        catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }

        return localIpAddr;
    }

}
