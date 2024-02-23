package com.github.armedis.redis.info;

import java.util.HashMap;
import java.util.Map;

public final class Replication extends StatsBaseVo {
    @Override
    public Map<String, String> initOperationKeyList() {
        Map<String, String> keyList = new HashMap<>();

//        keyList.put("masterLinkStatus", CONCAT);
        keyList.put("masterLastIoSecondsAgo", MAX);
        keyList.put("masterSyncInProgress", MAX);
        keyList.put("connectedSlaves", SUM);
        keyList.put("replBacklogActive", SUM);

        return keyList;
    }

    private String role;
    private String masterHost;
    private int masterPort;
    private String masterLinkStatus;
    private long masterLastIoSecondsAgo;
    private int masterSyncInProgress;
    private long slaveReadReplOffset;
    private long slaveReplOffset;
    private int slavePriority;
    private int slaveReadOnly;
    private int replicaAnnounced;
    private int connectedSlaves;
    private String slave0;
    private String masterFailoverState;
    private String masterReplid;
    private String masterReplid2;
    private long masterReplOffset;
    private long secondReplOffset;
    private int replBacklogActive;
    private long replBacklogSize;
    private long replBacklogFirstByteOffset;
    private long replBacklogHistlen;

    /**
     * @return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * @param role the role to set
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * @return the masterHost
     */
    public String getMasterHost() {
        return masterHost;
    }

    /**
     * @param masterHost the masterHost to set
     */
    public void setMasterHost(String masterHost) {
        this.masterHost = masterHost;
    }

    /**
     * @return the masterPort
     */
    public int getMasterPort() {
        return masterPort;
    }

    /**
     * @param masterPort the masterPort to set
     */
    public void setMasterPort(int masterPort) {
        this.masterPort = masterPort;
    }

    /**
     * @return the masterLinkStatus
     */
    public String getMasterLinkStatus() {
        return masterLinkStatus;
    }

    /**
     * @param masterLinkStatus the masterLinkStatus to set
     */
    public void setMasterLinkStatus(String masterLinkStatus) {
        this.masterLinkStatus = masterLinkStatus;
    }

    /**
     * @return the masterLastIoSecondsAgo
     */
    public long getMasterLastIoSecondsAgo() {
        return masterLastIoSecondsAgo;
    }

    /**
     * @param masterLastIoSecondsAgo the masterLastIoSecondsAgo to set
     */
    public void setMasterLastIoSecondsAgo(long masterLastIoSecondsAgo) {
        this.masterLastIoSecondsAgo = masterLastIoSecondsAgo;
    }

    /**
     * @return the masterSyncInProgress
     */
    public int getMasterSyncInProgress() {
        return masterSyncInProgress;
    }

    /**
     * @param masterSyncInProgress the masterSyncInProgress to set
     */
    public void setMasterSyncInProgress(int masterSyncInProgress) {
        this.masterSyncInProgress = masterSyncInProgress;
    }

    /**
     * @return the slaveReadReplOffset
     */
    public long getSlaveReadReplOffset() {
        return slaveReadReplOffset;
    }

    /**
     * @param slaveReadReplOffset the slaveReadReplOffset to set
     */
    public void setSlaveReadReplOffset(long slaveReadReplOffset) {
        this.slaveReadReplOffset = slaveReadReplOffset;
    }

    /**
     * @return the slaveReplOffset
     */
    public long getSlaveReplOffset() {
        return slaveReplOffset;
    }

    /**
     * @param slaveReplOffset the slaveReplOffset to set
     */
    public void setSlaveReplOffset(long slaveReplOffset) {
        this.slaveReplOffset = slaveReplOffset;
    }

    /**
     * @return the slavePriority
     */
    public int getSlavePriority() {
        return slavePriority;
    }

    /**
     * @param slavePriority the slavePriority to set
     */
    public void setSlavePriority(int slavePriority) {
        this.slavePriority = slavePriority;
    }

    /**
     * @return the slaveReadOnly
     */
    public int getSlaveReadOnly() {
        return slaveReadOnly;
    }

    /**
     * @param slaveReadOnly the slaveReadOnly to set
     */
    public void setSlaveReadOnly(int slaveReadOnly) {
        this.slaveReadOnly = slaveReadOnly;
    }

    /**
     * @return the replicaAnnounced
     */
    public int getReplicaAnnounced() {
        return replicaAnnounced;
    }

    /**
     * @param replicaAnnounced the replicaAnnounced to set
     */
    public void setReplicaAnnounced(int replicaAnnounced) {
        this.replicaAnnounced = replicaAnnounced;
    }

    /**
     * @return the connectedSlaves
     */
    public int getConnectedSlaves() {
        return connectedSlaves;
    }

    /**
     * @param connectedSlaves the connectedSlaves to set
     */
    public void setConnectedSlaves(int connectedSlaves) {
        this.connectedSlaves = connectedSlaves;
    }

    /**
     * @return the slave0
     */
    public String getSlave0() {
        return slave0;
    }

    /**
     * @param slave0 the slave0 to set
     */
    public void setSlave0(String slave0) {
        this.slave0 = slave0;
    }

    /**
     * @return the masterFailoverState
     */
    public String getMasterFailoverState() {
        return masterFailoverState;
    }

    /**
     * @param masterFailoverState the masterFailoverState to set
     */
    public void setMasterFailoverState(String masterFailoverState) {
        this.masterFailoverState = masterFailoverState;
    }

    /**
     * @return the masterReplid
     */
    public String getMasterReplid() {
        return masterReplid;
    }

    /**
     * @param masterReplid the masterReplid to set
     */
    public void setMasterReplid(String masterReplid) {
        this.masterReplid = masterReplid;
    }

    /**
     * @return the masterReplid2
     */
    public String getMasterReplid2() {
        return masterReplid2;
    }

    /**
     * @param masterReplid2 the masterReplid2 to set
     */
    public void setMasterReplid2(String masterReplid2) {
        this.masterReplid2 = masterReplid2;
    }

    /**
     * @return the masterReplOffset
     */
    public long getMasterReplOffset() {
        return masterReplOffset;
    }

    /**
     * @param masterReplOffset the masterReplOffset to set
     */
    public void setMasterReplOffset(long masterReplOffset) {
        this.masterReplOffset = masterReplOffset;
    }

    /**
     * @return the secondReplOffset
     */
    public long getSecondReplOffset() {
        return secondReplOffset;
    }

    /**
     * @param secondReplOffset the secondReplOffset to set
     */
    public void setSecondReplOffset(long secondReplOffset) {
        this.secondReplOffset = secondReplOffset;
    }

    /**
     * @return the replBacklogActive
     */
    public int getReplBacklogActive() {
        return replBacklogActive;
    }

    /**
     * @param replBacklogActive the replBacklogActive to set
     */
    public void setReplBacklogActive(int replBacklogActive) {
        this.replBacklogActive = replBacklogActive;
    }

    /**
     * @return the replBacklogSize
     */
    public long getReplBacklogSize() {
        return replBacklogSize;
    }

    /**
     * @param replBacklogSize the replBacklogSize to set
     */
    public void setReplBacklogSize(long replBacklogSize) {
        this.replBacklogSize = replBacklogSize;
    }

    /**
     * @return the replBacklogFirstByteOffset
     */
    public long getReplBacklogFirstByteOffset() {
        return replBacklogFirstByteOffset;
    }

    /**
     * @param replBacklogFirstByteOffset the replBacklogFirstByteOffset to set
     */
    public void setReplBacklogFirstByteOffset(long replBacklogFirstByteOffset) {
        this.replBacklogFirstByteOffset = replBacklogFirstByteOffset;
    }

    /**
     * @return the replBacklogHistlen
     */
    public long getReplBacklogHistlen() {
        return replBacklogHistlen;
    }

    /**
     * @param replBacklogHistlen the replBacklogHistlen to set
     */
    public void setReplBacklogHistlen(long replBacklogHistlen) {
        this.replBacklogHistlen = replBacklogHistlen;
    }
}