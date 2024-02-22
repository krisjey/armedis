package com.github.armedis.redis.info;

import java.util.HashMap;
import java.util.Map;

public final class Errorstats extends StatsBaseVo {
    @Override
    public Map<String, String> initOperationKeyList() {
        Map<String, String> keyList = new HashMap<>();

        keyList.put("errorstatClusterDown", SUM);
        keyList.put("errorstatCrossSlot", SUM);
        keyList.put("errorstatErr", SUM);
        keyList.put("errorstatLoading", SUM);
        keyList.put("errorstatMasterDown", SUM);
        keyList.put("errorstatMoved", SUM);
        keyList.put("errorstatNoScript", SUM);
        keyList.put("errorstatWrongPass", SUM);
        keyList.put("errorstatWrongType", SUM);

        return keyList;
    }

    /**
     * errorstat_CLUSTERDOWN:count=8 errorstat_CROSSSLOT:count=3
     * errorstat_ERR:count=169 errorstat_LOADING:count=4284
     * errorstat_MASTERDOWN:count=102 errorstat_MOVED:count=3626
     * errorstat_NOSCRIPT:count=4 errorstat_WRONGPASS:count=2
     * errorstat_WRONGTYPE:count=30
     */
    private long errorstatClusterDown;
    private long errorstatCrossSlot;
    private long errorstatErr;
    private long errorstatLoading;
    private long errorstatMasterDown;
    private long errorstatMoved;
    private long errorstatNoScript;
    private long errorstatWrongPass;
    private long errorstatWrongType;

    // Getter methods for accessing the stored information
    public long getErrorstatClusterDown() {
        return errorstatClusterDown;
    }

    public long getErrorstatCrossSlot() {
        return errorstatCrossSlot;
    }

    public long getErrorstatErr() {
        return errorstatErr;
    }

    public long getErrorstatLoading() {
        return errorstatLoading;
    }

    public long getErrorstatMasterDown() {
        return errorstatMasterDown;
    }

    public long getErrorstatMoved() {
        return errorstatMoved;
    }

    public long getErrorstatNoScript() {
        return errorstatNoScript;
    }

    public long getErrorstatWrongPass() {
        return errorstatWrongPass;
    }

    public long getErrorstatWrongType() {
        return errorstatWrongType;
    }
}