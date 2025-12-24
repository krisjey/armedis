package com.github.armedis.utils;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetworkUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(NetworkUtils.class);
    
    private static final String[] INTERNET_TEST_HOSTS = {
        "1.1.1.1",
        "8.8.8.8"
    };
    
    /**
     * 외부망 통신 가능한 모든 로컬 IP 목록 반환
     */
    public List<String> getInternetAccessibleIps() {
        Set<String> accessibleIps = new LinkedHashSet<>();
        
        // 1. 실제 외부 연결 테스트로 확인
        for (String testHost : INTERNET_TEST_HOSTS) {
            String ip = tryConnectAndGetLocalIp(testHost, 53);
            if (ip != null && isValidPublicIp(ip)) {
                accessibleIps.add(ip);
                logger.debug("Found internet-accessible IP via {}: {}", testHost, ip);
            }
        }
        
        // 2. 결과가 없으면 네트워크 인터페이스 직접 검사
        if (accessibleIps.isEmpty()) {
            accessibleIps.addAll(scanNetworkInterfaces());
        }
        
        return new ArrayList<>(accessibleIps);
    }
    
    private String tryConnectAndGetLocalIp(String remoteHost, int port) {
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName(remoteHost), port);
            socket.setSoTimeout(2000);
            
            return socket.getLocalAddress().getHostAddress();
        } catch (Exception e) {
            logger.trace("Connection test to {} failed: {}", remoteHost, e.getMessage());
            return null;
        }
    }
    
    private boolean isValidPublicIp(String ip) {
        // 127.x.x.x (loopback) 제외
        if (ip.startsWith("127.")) {
            return false;
        }
        
        // 0.0.0.0 제외
        if ("0.0.0.0".equals(ip)) {
            return false;
        }
        
        return true;
    }
    
    private List<String> scanNetworkInterfaces() {
        List<String> result = new ArrayList<>();
        
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();
                
                // DOWN 상태이거나 loopback이면 제외
                if (!ni.isUp() || ni.isLoopback()) {
                    continue;
                }
                
                // Virtual 인터페이스 제외 (virbr 등)
                String name = ni.getName();
                if (name.startsWith("virbr") || name.startsWith("docker")) {
                    continue;
                }
                
                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    
                    // IPv4만 처리
                    if (addr instanceof Inet4Address) {
                        String ip = addr.getHostAddress();
                        
                        if (isValidPublicIp(ip)) {
                            result.add(ip);
                            logger.debug("Found IP on interface {}: {}", name, ip);
                        }
                    }
                }
            }
        } catch (SocketException e) {
            logger.error("Failed to scan network interfaces", e);
        }
        
        return result;
    }
    
    /**
     * 기존 메서드 - 첫 번째 외부망 통신 가능 IP 반환
     */
    public String getLocalIpAddress() {
        List<String> ips = getInternetAccessibleIps();
        
        if (!ips.isEmpty()) {
            String selectedIp = ips.get(0);
            logger.info("Selected local IP: {}", selectedIp);
            return selectedIp;
        }
        
        logger.warn("No internet-accessible IP found, using fallback");
        return "127.0.0.1";
    }
}