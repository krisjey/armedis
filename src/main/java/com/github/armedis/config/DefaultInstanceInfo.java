package com.github.armedis.config;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonObject;

public class DefaultInstanceInfo {
    private static final String HOST = "host";
    private static final String PROCESS_ID = "pid";
    private static final String START_TIME = "startTime";
    private static final String SERVICE_PORT = "servicePort";

    public static final String UNKNOWN_HOST = "UNKNOWN_HOST";
    public static final String NO_SERVICE_PORT = "NO_SERVICE_PORT";

    private String nodePath;

    private String host;

    private String pid;

    private LocalDateTime startTime = LocalDateTime.now();

    private String servicePort;

    public DefaultInstanceInfo(String servicePort) {
        this.servicePort = StringUtils.defaultString(servicePort, NO_SERVICE_PORT);
    }

    public String getHost() {
        if (host == null) {
            try {
                host = InetAddress.getLocalHost().getHostName();
            }
            catch (UnknownHostException e) {
                host = UNKNOWN_HOST;
            }
        }

        return host;
    }

    public String getPid() {
        if (pid == null) {
            pid = ManagementFactory.getRuntimeMXBean().getName();
        }

        return pid;
    }

    public String getStartTime() {
        return this.startTime.toString();
    }

    public String getServicePort() {
        return this.servicePort;
    }

    public String getNodePath() {
        return this.nodePath;
    }

    public void setNodePath(String nodePath) {
        this.nodePath = nodePath;
    }

    public JsonObject toJsonObject() {
        JsonObject instanceInfo = new JsonObject();

        // host, pid
        instanceInfo.addProperty(PROCESS_ID, getPid());
        instanceInfo.addProperty(SERVICE_PORT, getServicePort());
        instanceInfo.addProperty(START_TIME, getStartTime());
        instanceInfo.addProperty(HOST, getHost());

        return instanceInfo;
    }
}