package com.github.armedis.http.service.stats;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

/**
 * VO of redis cluster nodes command result.
 * id, ip, listenPort, clusterBusPort, flags, masterId, pingSend, pongRecv,
 * configEpoch, linkState, shardSlotStart, shardSlotEnd
 */
public class RedisClusterNodeInfo {
    private String id;
    private String ip;
    private int listenPort;
    private int clusterBusPort;
    private String flags;
    private String masterId;
    private long pingSend;
    private long pongRecv;
    private long configEpoch;
    private String linkState;
    private int shardSlotStart;
    private int shardSlotEnd;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param ip the ip to
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @return the port
     */
    public int getListenPort() {
        return listenPort;
    }

    /**
     * @param port the port to
     */
    public void setListenPort(int listenPort) {
        this.listenPort = listenPort;
    }

    /**
     * @return the clusterBusPort
     */
    public int getClusterBusPort() {
        return clusterBusPort;
    }

    /**
     * @param clusterBusPort the clusterBusPort to
     */
    public void setClusterBusPort(int clusterBusPort) {
        this.clusterBusPort = clusterBusPort;
    }

    /**
     * @return the flags
     */
    public String getFlags() {
        return flags;
    }

    /**
     * @param flags the flags to
     */
    public void setFlags(String flags) {
        this.flags = flags;
    }

    /**
     * @return the masterId
     */
    public String getMasterId() {
        return masterId;
    }

    /**
     * @param masterId the masterId to
     */
    public void setMasterId(String masterId) {
        this.masterId = masterId;
    }

    /**
     * @return the pingSend
     */
    public long getPingSend() {
        return pingSend;
    }

    /**
     * @param pingSend the pingSend to
     */
    public void setPingSend(long pingSend) {
        this.pingSend = pingSend;
    }

    /**
     * @return the pongRecv
     */
    public long getPongRecv() {
        return pongRecv;
    }

    /**
     * @param pongRecv the pongRecv to
     */
    public void setPongRecv(long pongRecv) {
        this.pongRecv = pongRecv;
    }

    /**
     * @return the configEpoch
     */
    public long getConfigEpoch() {
        return configEpoch;
    }

    /**
     * @param configEpoch the configEpoch to
     */
    public void setConfigEpoch(long configEpoch) {
        this.configEpoch = configEpoch;
    }

    /**
     * @return the linkState
     */
    public String getLinkState() {
        return linkState;
    }

    /**
     * @param linkState the linkState to
     */
    public void setLinkState(String linkState) {
        this.linkState = linkState;
    }

    /**
     * @return the shardSlotStart
     */
    public int getShardSlotStart() {
        return shardSlotStart;
    }

    /**
     * @param shardSlotStart the shardSlotStart to
     */
    public void setShardSlotStart(int shardSlotStart) {
        this.shardSlotStart = shardSlotStart;
    }

    /**
     * @return the shardSlotEnd
     */
    public int getShardSlotEnd() {
        return shardSlotEnd;
    }

    /**
     * @param shardSlotEnd the shardSlotEnd to
     */
    public void setShardSlotEnd(int shardSlotEnd) {
        this.shardSlotEnd = shardSlotEnd;
    }

    @Override
    public String toString() {
        return "RedisClusterNodeInfo [id=" + id + ", ip=" + ip + ", listenPort=" + listenPort + ", clusterBusPort="
                + clusterBusPort + ", flags=" + flags + ", masterId=" + masterId + ", pingSend=" + pingSend
                + ", pongRecv=" + pongRecv + ", configEpoch=" + configEpoch + ", linkState=" + linkState
                + ", shardSlotStart=" + shardSlotStart + ", shardSlotEnd=" + shardSlotEnd + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(clusterBusPort, configEpoch, flags, id, ip, linkState, listenPort, masterId, pingSend,
                pongRecv, shardSlotEnd, shardSlotStart);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RedisClusterNodeInfo other = (RedisClusterNodeInfo) obj;
        return clusterBusPort == other.clusterBusPort && configEpoch == other.configEpoch
                && Objects.equals(flags, other.flags) && Objects.equals(id, other.id) && Objects.equals(ip, other.ip)
                && Objects.equals(linkState, other.linkState) && listenPort == other.listenPort
                && Objects.equals(masterId, other.masterId) && pingSend == other.pingSend && pongRecv == other.pongRecv
                && shardSlotEnd == other.shardSlotEnd && shardSlotStart == other.shardSlotStart;
    }
    
    public static RedisClusterNodeInfo of(String nodeInfoString) {
        // nodeInfoString(12, 10) ==> id, ip, listenPort, clusterBusPort,
        // flags,masterId,
        // pingSend, pongRecv, configEpoch, linkState, shardSlotStart,
        // shardSlotEnd
        String[] nodeInfoArray = StringUtils.split(nodeInfoString, " :@");

        RedisClusterNodeInfo nodeInfo = new RedisClusterNodeInfo();
        nodeInfo.setId(nodeInfoArray[0]);
        nodeInfo.setIp(nodeInfoArray[1]);
        nodeInfo.setListenPort(Integer.parseInt(nodeInfoArray[2]));
        nodeInfo.setClusterBusPort(Integer.parseInt(nodeInfoArray[3]));
        nodeInfo.setFlags(nodeInfoArray[4]);
        nodeInfo.setMasterId(nodeInfoArray[5]);
        nodeInfo.setPingSend(Long.parseLong(nodeInfoArray[6]));
        nodeInfo.setPongRecv(Long.parseLong(nodeInfoArray[7]));
        nodeInfo.setConfigEpoch(Integer.parseInt(nodeInfoArray[8]));
        nodeInfo.setLinkState(nodeInfoArray[9]);
        if (nodeInfoArray.length > 10) {
            nodeInfo.setShardSlotStart(Integer.parseInt(StringUtils.split(nodeInfoArray[10], "-")[0]));
            nodeInfo.setShardSlotEnd(Integer.parseInt(StringUtils.split(nodeInfoArray[10], "-")[1]));
        }

        return nodeInfo;
    }
}
