package com.github.armedis.redis.info;

import java.util.HashMap;
import java.util.Map;

public class Server extends StatsBaseVo {
    @Override
    public Map<String, String> initOperationKeyList() {
        Map<String, String> keyList = new HashMap<>();

        keyList.put("redisVersion", DIFF);
        keyList.put("redisMode", DIFF);
        keyList.put("os", DIFF);
        keyList.put("archBits", DIFF);
        keyList.put("gccVersion", DIFF);
        keyList.put("processId", CONCAT);
        keyList.put("processSupervised", DIFF);
        keyList.put("multiplexingApi", DIFF);
        keyList.put("host", DIFF);
        keyList.put("tcpPort", DIFF);
        keyList.put("uptimeInSeconds", MIN);
        keyList.put("uptimeInDays", MIN);
        keyList.put("hz", DIFF);
        keyList.put("configuredHz", DIFF);
        keyList.put("ioThreadsActive", DIFF);
        keyList.put("configFile", DIFF);
        keyList.put("redisBuildId", DIFF);
        keyList.put("monotonicClock", DIFF);

        return keyList;
    }

    private String redisVersion;
    private String redisGitSha1;
    private int redisGitDirty;
    private String redisBuildId;
    private String redisMode;
    private String os;
    private String archBits;
    private String monotonicClock;
    private String multiplexingApi;
    private String atomicvarApi;
    private String gccVersion;
    private int processId;
    private String processSupervised;
    private String runId;
    private String host;
    private int tcpPort;
    private long serverTimeUsec;
    private int uptimeInSeconds;
    private int uptimeInDays;
    private int hz;
    private int configuredHz;
    private long lruClock;
    private String executable;
    private String configFile;
    private int ioThreadsActive;

    /**
     * @return the redisVersion
     */
    public String getRedisVersion() {
        return redisVersion;
    }

    /**
     * @param redisVersion the redisVersion to set
     */
    public void setRedisVersion(String redisVersion) {
        this.redisVersion = redisVersion;
    }

    /**
     * @return the redisGitSha1
     */
    public String getRedisGitSha1() {
        return redisGitSha1;
    }

    /**
     * @param redisGitSha1 the redisGitSha1 to set
     */
    public void setRedisGitSha1(String redisGitSha1) {
        this.redisGitSha1 = redisGitSha1;
    }

    /**
     * @return the redisGitDirty
     */
    public int getRedisGitDirty() {
        return redisGitDirty;
    }

    /**
     * @param redisGitDirty the redisGitDirty to set
     */
    public void setRedisGitDirty(int redisGitDirty) {
        this.redisGitDirty = redisGitDirty;
    }

    /**
     * @return the redisBuildId
     */
    public String getRedisBuildId() {
        return redisBuildId;
    }

    /**
     * @param redisBuildId the redisBuildId to set
     */
    public void setRedisBuildId(String redisBuildId) {
        this.redisBuildId = redisBuildId;
    }

    /**
     * @return the redisMode
     */
    public String getRedisMode() {
        return redisMode;
    }

    /**
     * @param redisMode the redisMode to set
     */
    public void setRedisMode(String redisMode) {
        this.redisMode = redisMode;
    }

    /**
     * @return the os
     */
    public String getOs() {
        return os;
    }

    /**
     * @param os the os to set
     */
    public void setOs(String os) {
        this.os = os;
    }

    /**
     * @return the archBits
     */
    public String getArchBits() {
        return archBits;
    }

    /**
     * @param archBits the archBits to set
     */
    public void setArchBits(String archBits) {
        this.archBits = archBits;
    }

    /**
     * @return the monotonicClock
     */
    public String getMonotonicClock() {
        return monotonicClock;
    }

    /**
     * @param monotonicClock the monotonicClock to set
     */
    public void setMonotonicClock(String monotonicClock) {
        this.monotonicClock = monotonicClock;
    }

    /**
     * @return the multiplexingApi
     */
    public String getMultiplexingApi() {
        return multiplexingApi;
    }

    /**
     * @param multiplexingApi the multiplexingApi to set
     */
    public void setMultiplexingApi(String multiplexingApi) {
        this.multiplexingApi = multiplexingApi;
    }

    /**
     * @return the atomicvarApi
     */
    public String getAtomicvarApi() {
        return atomicvarApi;
    }

    /**
     * @param atomicvarApi the atomicvarApi to set
     */
    public void setAtomicvarApi(String atomicvarApi) {
        this.atomicvarApi = atomicvarApi;
    }

    /**
     * @return the gccVersion
     */
    public String getGccVersion() {
        return gccVersion;
    }

    /**
     * @param gccVersion the gccVersion to set
     */
    public void setGccVersion(String gccVersion) {
        this.gccVersion = gccVersion;
    }

    /**
     * @return the processId
     */
    public int getProcessId() {
        return processId;
    }

    /**
     * @param processId the processId to set
     */
    public void setProcessId(int processId) {
        this.processId = processId;
    }

    /**
     * @return the processSupervised
     */
    public String getProcessSupervised() {
        return processSupervised;
    }

    /**
     * @param processSupervised the processSupervised to set
     */
    public void setProcessSupervised(String processSupervised) {
        this.processSupervised = processSupervised;
    }

    /**
     * @return the runId
     */
    public String getRunId() {
        return runId;
    }

    /**
     * @param runId the runId to set
     */
    public void setRunId(String runId) {
        this.runId = runId;
    }

    /**
     * @return the tcpPort
     */
    public int getTcpPort() {
        return tcpPort;
    }

    /**
     * @param tcpPort the tcpPort to set
     */
    public void setTcpPort(int tcpPort) {
        this.tcpPort = tcpPort;
    }

    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @param host the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @return the serverTimeUsec
     */
    public long getServerTimeUsec() {
        return serverTimeUsec;
    }

    /**
     * @param serverTimeUsec the serverTimeUsec to set
     */
    public void setServerTimeUsec(long serverTimeUsec) {
        this.serverTimeUsec = serverTimeUsec;
    }

    /**
     * @return the uptimeInSeconds
     */
    public int getUptimeInSeconds() {
        return uptimeInSeconds;
    }

    /**
     * @param uptimeInSeconds the uptimeInSeconds to set
     */
    public void setUptimeInSeconds(int uptimeInSeconds) {
        this.uptimeInSeconds = uptimeInSeconds;
    }

    /**
     * @return the uptimeInDays
     */
    public int getUptimeInDays() {
        return uptimeInDays;
    }

    /**
     * @param uptimeInDays the uptimeInDays to set
     */
    public void setUptimeInDays(int uptimeInDays) {
        this.uptimeInDays = uptimeInDays;
    }

    /**
     * @return the hz
     */
    public int getHz() {
        return hz;
    }

    /**
     * @param hz the hz to set
     */
    public void setHz(int hz) {
        this.hz = hz;
    }

    /**
     * @return the configuredHz
     */
    public int getConfiguredHz() {
        return configuredHz;
    }

    /**
     * @param configuredHz the configuredHz to set
     */
    public void setConfiguredHz(int configuredHz) {
        this.configuredHz = configuredHz;
    }

    /**
     * @return the lruClock
     */
    public long getLruClock() {
        return lruClock;
    }

    /**
     * @param lruClock the lruClock to set
     */
    public void setLruClock(long lruClock) {
        this.lruClock = lruClock;
    }

    /**
     * @return the executable
     */
    public String getExecutable() {
        return executable;
    }

    /**
     * @param executable the executable to set
     */
    public void setExecutable(String executable) {
        this.executable = executable;
    }

    /**
     * @return the configFile
     */
    public String getConfigFile() {
        return configFile;
    }

    /**
     * @param configFile the configFile to set
     */
    public void setConfigFile(String configFile) {
        this.configFile = configFile;
    }

    /**
     * @return the ioThreadsActive
     */
    public int getIoThreadsActive() {
        return ioThreadsActive;
    }

    /**
     * @param ioThreadsActive the ioThreadsActive to set
     */
    public void setIoThreadsActive(int ioThreadsActive) {
        this.ioThreadsActive = ioThreadsActive;
    }
}