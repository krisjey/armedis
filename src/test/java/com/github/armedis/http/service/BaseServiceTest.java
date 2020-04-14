
package com.github.armedis.http.service;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class BaseServiceTest {
    private static final String LOCAL_IP_ADDR = getLocalIpAddr();
    protected static final String HTTP_PORT = "8081";

    private static final Gson jsonParser = new Gson();
    
    protected static final ObjectMapper mapper = new ObjectMapper();

    private static final String getLocalIpAddr() {
        String localIpAddr = null;

        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            localIpAddr = socket.getLocalAddress().getHostAddress();
        }
        catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }

        return localIpAddr;
    }

    private String buildPath(String command, String key) {
        if (StringUtils.isBlank(key)) {
            return "/v1/" + command;
        }
        else {
            return "/v1/" + command + "/" + key;
        }
    }

    protected String buildTestUrl(String command, String key) {
        return "http://" + LOCAL_IP_ADDR + ":" + HTTP_PORT + buildPath(command, key);
    }

    protected JsonObject parseToJson(String responseBody) {
        return jsonParser.fromJson(responseBody, JsonObject.class);
    }
}
