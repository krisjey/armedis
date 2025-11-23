package com.github.armedis.utils;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class LocalIpAddress {

    public String getLocalIpAddress() {
        String localIpAddr = null;

        // 1. 외부 DNS 서버로 시도 (인터넷 가능 환경)
        localIpAddr = tryGetIpByConnection("1.1.1.1", 53);
        if (localIpAddr != null) {
            return localIpAddr;
        }

        // 2. 내부망용 Private IP 대역 게이트웨이로 시도
        localIpAddr = tryGetIpByConnection("10.0.0.1", 53);
        if (localIpAddr == null) {
            localIpAddr = tryGetIpByConnection("172.16.0.1", 53);
        }
        if (localIpAddr == null) {
            localIpAddr = tryGetIpByConnection("192.168.1.1", 53);
        }
        if (localIpAddr != null) {
            return localIpAddr;
        }

        // 3. 네트워크 인터페이스 직접 스캔 (최후의 방법)
        localIpAddr = getIpFromNetworkInterface();
        if (localIpAddr != null) {
            return localIpAddr;
        }

        // 4. 모든 방법 실패 시 기본값
        return "127.0.0.1";
    }

    private String tryGetIpByConnection(String remoteHost, int port) {
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName(remoteHost), port);
            String ip = socket.getLocalAddress().getHostAddress();
            
            // loopback이 아닌 경우만 반환
            if (ip != null && !ip.startsWith("127.")) {
                return ip;
            }
        } catch (Exception e) {
            // 연결 실패는 정상적인 시나리오 (내부망/외부망 환경 차이)
            // 로깅은 DEBUG 레벨로만
        }
        return null;
    }

    private String getIpFromNetworkInterface() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                
                // 비활성화되었거나 loopback 인터페이스 제외
                if (!networkInterface.isUp() || networkInterface.isLoopback()) {
                    continue;
                }

                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    
                    // IPv4이면서 site-local 주소 (private IP) 우선 반환
                    if (address instanceof Inet4Address && address.isSiteLocalAddress()) {
                        return address.getHostAddress();
                    }
                }
            }
            
            // site-local이 없으면 일반 IPv4 주소 반환
            interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                
                if (!networkInterface.isUp() || networkInterface.isLoopback()) {
                    continue;
                }

                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    
                    if (address instanceof Inet4Address && !address.isLoopbackAddress()) {
                        return address.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            // 최후의 fallback도 실패
            e.printStackTrace();
        }
        
        return null;
    }

}
