/**
 * 
 */
package com.github.armedis.redis.info;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 */
public final class Stats extends StatsBaseVo {
    @Override
    public Map<String, String> initOperationKeyList() {
        Map<String, String> keyList = new HashMap<>();

        keyList.put("totalConnectionsReceived", SUM);
        keyList.put("totalCommandsProcessed", SUM);
        keyList.put("instantaneousOpsPerSec", SUM);
        keyList.put("instantaneousInputKbps", SUM);
        keyList.put("instantaneousOutputKbps", SUM);
        keyList.put("rejectedConnections", SUM);
        keyList.put("syncFull", MAX);
        keyList.put("syncPartialOk", MAX);
        keyList.put("syncPartialErr", MAX);
        keyList.put("expiredKeys", SUM);
        keyList.put("evictedKeys", SUM);
        keyList.put("expiredTimeCapReachedCount", SUM);
        keyList.put("expireCycleCpuMilliseconds", SUM);
        keyList.put("keyspaceHits", SUM);
        keyList.put("keyspaceMisses", SUM);
        keyList.put("pubsubChannels", SUM);
        keyList.put("totalForks", SUM);
        keyList.put("activeDefragKeyHits", SUM);
        keyList.put("activeDefragKeyMisses", SUM);
        keyList.put("unexpectedErrorReplies", SUM);
        keyList.put("totalErrorReplies", SUM);
        keyList.put("totalReadsProcessed", SUM);
        keyList.put("totalWritesProcessed", SUM);
        keyList.put("ioThreadedReadsProcessed", SUM);
        keyList.put("ioThreadedWritesProcessed", SUM);

        return keyList;
    }

    private int totalConnectionsReceived;
    private int totalCommandsProcessed;
    private int instantaneousOpsPerSec;
    private long totalNetInputBytes;
    private long totalNetOutputBytes;
    private double instantaneousInputKbps;
    private double instantaneousOutputKbps;
    private int rejectedConnections;
    private int syncFull;
    private int syncPartialOk;
    private int syncPartialErr;
    private int expiredKeys;
    private double expiredStalePerc;
    private int expiredTimeCapReachedCount;
    private int expireCycleCpuMilliseconds;
    private int evictedKeys;
    private int keyspaceHits;
    private int keyspaceMisses;
    private int pubsubChannels;
    private int pubsubPatterns;
    private long latestForkUsec;
    private int totalForks;
    private int migrateCachedSockets;
    private int slaveExpiresTrackedKeys;
    private int activeDefragHits;
    private int activeDefragMisses;
    private int activeDefragKeyHits;
    private int activeDefragKeyMisses;
    private int trackingTotalKeys;
    private int trackingTotalItems;
    private int trackingTotalPrefixes;
    private int unexpectedErrorReplies;
    private int totalErrorReplies;
    private int dumpPayloadSanitizations;
    private int totalReadsProcessed;
    private int totalWritesProcessed;
    private int ioThreadedReadsProcessed;
    private int ioThreadedWritesProcessed;

    /**
     * @return the totalConnectionsReceived
     */
    public int getTotalConnectionsReceived() {
        return totalConnectionsReceived;
    }

    /**
     * @param totalConnectionsReceived the totalConnectionsReceived to set
     */
    public void setTotalConnectionsReceived(int totalConnectionsReceived) {
        this.totalConnectionsReceived = totalConnectionsReceived;
    }

    /**
     * @return the totalCommandsProcessed
     */
    public int getTotalCommandsProcessed() {
        return totalCommandsProcessed;
    }

    /**
     * @param totalCommandsProcessed the totalCommandsProcessed to set
     */
    public void setTotalCommandsProcessed(int totalCommandsProcessed) {
        this.totalCommandsProcessed = totalCommandsProcessed;
    }

    /**
     * @return the instantaneousOpsPerSec
     */
    public int getInstantaneousOpsPerSec() {
        return instantaneousOpsPerSec;
    }

    /**
     * @param instantaneousOpsPerSec the instantaneousOpsPerSec to set
     */
    public void setInstantaneousOpsPerSec(int instantaneousOpsPerSec) {
        this.instantaneousOpsPerSec = instantaneousOpsPerSec;
    }

    /**
     * @return the totalNetInputBytes
     */
    public long getTotalNetInputBytes() {
        return totalNetInputBytes;
    }

    /**
     * @param totalNetInputBytes the totalNetInputBytes to set
     */
    public void setTotalNetInputBytes(long totalNetInputBytes) {
        this.totalNetInputBytes = totalNetInputBytes;
    }

    /**
     * @return the totalNetOutputBytes
     */
    public long getTotalNetOutputBytes() {
        return totalNetOutputBytes;
    }

    /**
     * @param totalNetOutputBytes the totalNetOutputBytes to set
     */
    public void setTotalNetOutputBytes(long totalNetOutputBytes) {
        this.totalNetOutputBytes = totalNetOutputBytes;
    }

    /**
     * @return the instantaneousInputKbps
     */
    public double getInstantaneousInputKbps() {
        return instantaneousInputKbps;
    }

    /**
     * @param instantaneousInputKbps the instantaneousInputKbps to set
     */
    public void setInstantaneousInputKbps(double instantaneousInputKbps) {
        this.instantaneousInputKbps = instantaneousInputKbps;
    }

    /**
     * @return the instantaneousOutputKbps
     */
    public double getInstantaneousOutputKbps() {
        return instantaneousOutputKbps;
    }

    /**
     * @param instantaneousOutputKbps the instantaneousOutputKbps to set
     */
    public void setInstantaneousOutputKbps(double instantaneousOutputKbps) {
        this.instantaneousOutputKbps = instantaneousOutputKbps;
    }

    /**
     * @return the rejectedConnections
     */
    public int getRejectedConnections() {
        return rejectedConnections;
    }

    /**
     * @param rejectedConnections the rejectedConnections to set
     */
    public void setRejectedConnections(int rejectedConnections) {
        this.rejectedConnections = rejectedConnections;
    }

    /**
     * @return the syncFull
     */
    public int getSyncFull() {
        return syncFull;
    }

    /**
     * @param syncFull the syncFull to set
     */
    public void setSyncFull(int syncFull) {
        this.syncFull = syncFull;
    }

    /**
     * @return the syncPartialOk
     */
    public int getSyncPartialOk() {
        return syncPartialOk;
    }

    /**
     * @param syncPartialOk the syncPartialOk to set
     */
    public void setSyncPartialOk(int syncPartialOk) {
        this.syncPartialOk = syncPartialOk;
    }

    /**
     * @return the syncPartialErr
     */
    public int getSyncPartialErr() {
        return syncPartialErr;
    }

    /**
     * @param syncPartialErr the syncPartialErr to set
     */
    public void setSyncPartialErr(int syncPartialErr) {
        this.syncPartialErr = syncPartialErr;
    }

    /**
     * @return the expiredKeys
     */
    public int getExpiredKeys() {
        return expiredKeys;
    }

    /**
     * @param expiredKeys the expiredKeys to set
     */
    public void setExpiredKeys(int expiredKeys) {
        this.expiredKeys = expiredKeys;
    }

    /**
     * @return the expiredStalePerc
     */
    public double getExpiredStalePerc() {
        return expiredStalePerc;
    }

    /**
     * @param expiredStalePerc the expiredStalePerc to set
     */
    public void setExpiredStalePerc(double expiredStalePerc) {
        this.expiredStalePerc = expiredStalePerc;
    }

    /**
     * @return the expiredTimeCapReachedCount
     */
    public int getExpiredTimeCapReachedCount() {
        return expiredTimeCapReachedCount;
    }

    /**
     * @param expiredTimeCapReachedCount the expiredTimeCapReachedCount to set
     */
    public void setExpiredTimeCapReachedCount(int expiredTimeCapReachedCount) {
        this.expiredTimeCapReachedCount = expiredTimeCapReachedCount;
    }

    /**
     * @return the expireCycleCpuMilliseconds
     */
    public int getExpireCycleCpuMilliseconds() {
        return expireCycleCpuMilliseconds;
    }

    /**
     * @param expireCycleCpuMilliseconds the expireCycleCpuMilliseconds to set
     */
    public void setExpireCycleCpuMilliseconds(int expireCycleCpuMilliseconds) {
        this.expireCycleCpuMilliseconds = expireCycleCpuMilliseconds;
    }

    /**
     * @return the evictedKeys
     */
    public int getEvictedKeys() {
        return evictedKeys;
    }

    /**
     * @param evictedKeys the evictedKeys to set
     */
    public void setEvictedKeys(int evictedKeys) {
        this.evictedKeys = evictedKeys;
    }

    /**
     * @return the keyspaceHits
     */
    public int getKeyspaceHits() {
        return keyspaceHits;
    }

    /**
     * @param keyspaceHits the keyspaceHits to set
     */
    public void setKeyspaceHits(int keyspaceHits) {
        this.keyspaceHits = keyspaceHits;
    }

    /**
     * @return the keyspaceMisses
     */
    public int getKeyspaceMisses() {
        return keyspaceMisses;
    }

    /**
     * @param keyspaceMisses the keyspaceMisses to set
     */
    public void setKeyspaceMisses(int keyspaceMisses) {
        this.keyspaceMisses = keyspaceMisses;
    }

    /**
     * @return the pubsubChannels
     */
    public int getPubsubChannels() {
        return pubsubChannels;
    }

    /**
     * @param pubsubChannels the pubsubChannels to set
     */
    public void setPubsubChannels(int pubsubChannels) {
        this.pubsubChannels = pubsubChannels;
    }

    /**
     * @return the pubsubPatterns
     */
    public int getPubsubPatterns() {
        return pubsubPatterns;
    }

    /**
     * @param pubsubPatterns the pubsubPatterns to set
     */
    public void setPubsubPatterns(int pubsubPatterns) {
        this.pubsubPatterns = pubsubPatterns;
    }

    /**
     * @return the latestForkUsec
     */
    public long getLatestForkUsec() {
        return latestForkUsec;
    }

    /**
     * @param latestForkUsec the latestForkUsec to set
     */
    public void setLatestForkUsec(long latestForkUsec) {
        this.latestForkUsec = latestForkUsec;
    }

    /**
     * @return the totalForks
     */
    public int getTotalForks() {
        return totalForks;
    }

    /**
     * @param totalForks the totalForks to set
     */
    public void setTotalForks(int totalForks) {
        this.totalForks = totalForks;
    }

    /**
     * @return the migrateCachedSockets
     */
    public int getMigrateCachedSockets() {
        return migrateCachedSockets;
    }

    /**
     * @param migrateCachedSockets the migrateCachedSockets to set
     */
    public void setMigrateCachedSockets(int migrateCachedSockets) {
        this.migrateCachedSockets = migrateCachedSockets;
    }

    /**
     * @return the slaveExpiresTrackedKeys
     */
    public int getSlaveExpiresTrackedKeys() {
        return slaveExpiresTrackedKeys;
    }

    /**
     * @param slaveExpiresTrackedKeys the slaveExpiresTrackedKeys to set
     */
    public void setSlaveExpiresTrackedKeys(int slaveExpiresTrackedKeys) {
        this.slaveExpiresTrackedKeys = slaveExpiresTrackedKeys;
    }

    /**
     * @return the activeDefragHits
     */
    public int getActiveDefragHits() {
        return activeDefragHits;
    }

    /**
     * @param activeDefragHits the activeDefragHits to set
     */
    public void setActiveDefragHits(int activeDefragHits) {
        this.activeDefragHits = activeDefragHits;
    }

    /**
     * @return the activeDefragMisses
     */
    public int getActiveDefragMisses() {
        return activeDefragMisses;
    }

    /**
     * @param activeDefragMisses the activeDefragMisses to set
     */
    public void setActiveDefragMisses(int activeDefragMisses) {
        this.activeDefragMisses = activeDefragMisses;
    }

    /**
     * @return the activeDefragKeyHits
     */
    public int getActiveDefragKeyHits() {
        return activeDefragKeyHits;
    }

    /**
     * @param activeDefragKeyHits the activeDefragKeyHits to set
     */
    public void setActiveDefragKeyHits(int activeDefragKeyHits) {
        this.activeDefragKeyHits = activeDefragKeyHits;
    }

    /**
     * @return the activeDefragKeyMisses
     */
    public int getActiveDefragKeyMisses() {
        return activeDefragKeyMisses;
    }

    /**
     * @param activeDefragKeyMisses the activeDefragKeyMisses to set
     */
    public void setActiveDefragKeyMisses(int activeDefragKeyMisses) {
        this.activeDefragKeyMisses = activeDefragKeyMisses;
    }

    /**
     * @return the trackingTotalKeys
     */
    public int getTrackingTotalKeys() {
        return trackingTotalKeys;
    }

    /**
     * @param trackingTotalKeys the trackingTotalKeys to set
     */
    public void setTrackingTotalKeys(int trackingTotalKeys) {
        this.trackingTotalKeys = trackingTotalKeys;
    }

    /**
     * @return the trackingTotalItems
     */
    public int getTrackingTotalItems() {
        return trackingTotalItems;
    }

    /**
     * @param trackingTotalItems the trackingTotalItems to set
     */
    public void setTrackingTotalItems(int trackingTotalItems) {
        this.trackingTotalItems = trackingTotalItems;
    }

    /**
     * @return the trackingTotalPrefixes
     */
    public int getTrackingTotalPrefixes() {
        return trackingTotalPrefixes;
    }

    /**
     * @param trackingTotalPrefixes the trackingTotalPrefixes to set
     */
    public void setTrackingTotalPrefixes(int trackingTotalPrefixes) {
        this.trackingTotalPrefixes = trackingTotalPrefixes;
    }

    /**
     * @return the unexpectedErrorReplies
     */
    public int getUnexpectedErrorReplies() {
        return unexpectedErrorReplies;
    }

    /**
     * @param unexpectedErrorReplies the unexpectedErrorReplies to set
     */
    public void setUnexpectedErrorReplies(int unexpectedErrorReplies) {
        this.unexpectedErrorReplies = unexpectedErrorReplies;
    }

    /**
     * @return the totalErrorReplies
     */
    public int getTotalErrorReplies() {
        return totalErrorReplies;
    }

    /**
     * @param totalErrorReplies the totalErrorReplies to set
     */
    public void setTotalErrorReplies(int totalErrorReplies) {
        this.totalErrorReplies = totalErrorReplies;
    }

    /**
     * @return the dumpPayloadSanitizations
     */
    public int getDumpPayloadSanitizations() {
        return dumpPayloadSanitizations;
    }

    /**
     * @param dumpPayloadSanitizations the dumpPayloadSanitizations to set
     */
    public void setDumpPayloadSanitizations(int dumpPayloadSanitizations) {
        this.dumpPayloadSanitizations = dumpPayloadSanitizations;
    }

    /**
     * @return the totalReadsProcessed
     */
    public int getTotalReadsProcessed() {
        return totalReadsProcessed;
    }

    /**
     * @param totalReadsProcessed the totalReadsProcessed to set
     */
    public void setTotalReadsProcessed(int totalReadsProcessed) {
        this.totalReadsProcessed = totalReadsProcessed;
    }

    /**
     * @return the totalWritesProcessed
     */
    public int getTotalWritesProcessed() {
        return totalWritesProcessed;
    }

    /**
     * @param totalWritesProcessed the totalWritesProcessed to set
     */
    public void setTotalWritesProcessed(int totalWritesProcessed) {
        this.totalWritesProcessed = totalWritesProcessed;
    }

    /**
     * @return the ioThreadedReadsProcessed
     */
    public int getIoThreadedReadsProcessed() {
        return ioThreadedReadsProcessed;
    }

    /**
     * @param ioThreadedReadsProcessed the ioThreadedReadsProcessed to set
     */
    public void setIoThreadedReadsProcessed(int ioThreadedReadsProcessed) {
        this.ioThreadedReadsProcessed = ioThreadedReadsProcessed;
    }

    /**
     * @return the ioThreadedWritesProcessed
     */
    public int getIoThreadedWritesProcessed() {
        return ioThreadedWritesProcessed;
    }

    /**
     * @param ioThreadedWritesProcessed the ioThreadedWritesProcessed to set
     */
    public void setIoThreadedWritesProcessed(int ioThreadedWritesProcessed) {
        this.ioThreadedWritesProcessed = ioThreadedWritesProcessed;
    }

}
