package com.github.armedis.redis.info;

import java.util.HashMap;
import java.util.Map;

public final class Clients extends StatsBaseVo {
    @Override
    public Map<String, String> initOperationKeyList() {
        Map<String, String> keyList = new HashMap<>();

        keyList.put("connectedClients", SUM);
        keyList.put("clusterConnections", SUM);
        keyList.put("maxclients", SUM);
        keyList.put("blockedClients", SUM);
        keyList.put("trackingClients", SUM);
        keyList.put("clientsInTimeoutTable", SUM);

        return keyList;
    }

    private int connectedClients;
    private int clusterConnections;
    private int maxclients;
    private int clientRecentMaxInputBuffer;
    private int clientRecentMaxOutputBuffer;
    private int blockedClients;
    private int trackingClients;
    private int clientsInTimeoutTable;

    /**
     * @return the connectedClients
     */
    public int getConnectedClients() {
        return connectedClients;
    }

    /**
     * @param connectedClients the connectedClients to set
     */
    public void setConnectedClients(int connectedClients) {
        this.connectedClients = connectedClients;
    }

    /**
     * @return the clusterConnections
     */
    public int getClusterConnections() {
        return clusterConnections;
    }

    /**
     * @param clusterConnections the clusterConnections to set
     */
    public void setClusterConnections(int clusterConnections) {
        this.clusterConnections = clusterConnections;
    }

    /**
     * @return the maxClients
     */
    public int getMaxclients() {
        return maxclients;
    }

    /**
     * @param maxClients the maxClients to set
     */
    public void setMaxclients(int maxclients) {
        this.maxclients = maxclients;
    }

    /**
     * @return the clientRecentMaxInputBuffer
     */
    public int getClientRecentMaxInputBuffer() {
        return clientRecentMaxInputBuffer;
    }

    /**
     * @param clientRecentMaxInputBuffer the clientRecentMaxInputBuffer to set
     */
    public void setClientRecentMaxInputBuffer(int clientRecentMaxInputBuffer) {
        this.clientRecentMaxInputBuffer = clientRecentMaxInputBuffer;
    }

    /**
     * @return the clientRecentMaxOutputBuffer
     */
    public int getClientRecentMaxOutputBuffer() {
        return clientRecentMaxOutputBuffer;
    }

    /**
     * @param clientRecentMaxOutputBuffer the clientRecentMaxOutputBuffer to set
     */
    public void setClientRecentMaxOutputBuffer(int clientRecentMaxOutputBuffer) {
        this.clientRecentMaxOutputBuffer = clientRecentMaxOutputBuffer;
    }

    /**
     * @return the blockedClients
     */
    public int getBlockedClients() {
        return blockedClients;
    }

    /**
     * @param blockedClients the blockedClients to set
     */
    public void setBlockedClients(int blockedClients) {
        this.blockedClients = blockedClients;
    }

    /**
     * @return the trackingClients
     */
    public int getTrackingClients() {
        return trackingClients;
    }

    /**
     * @param trackingClients the trackingClients to set
     */
    public void setTrackingClients(int trackingClients) {
        this.trackingClients = trackingClients;
    }

    /**
     * @return the clientsInTimeoutTable
     */
    public int getClientsInTimeoutTable() {
        return clientsInTimeoutTable;
    }

    /**
     * @param clientsInTimeoutTable the clientsInTimeoutTable to set
     */
    public void setClientsInTimeoutTable(int clientsInTimeoutTable) {
        this.clientsInTimeoutTable = clientsInTimeoutTable;
    }
}