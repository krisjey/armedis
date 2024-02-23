package com.github.armedis.redis.info;

import java.util.HashMap;
import java.util.Map;

public final class Cluster extends StatsBaseVo {
    @Override
    public Map<String, String> initOperationKeyList() {
        Map<String, String> keyList = new HashMap<>();

        keyList.put("clusterEnabled", DIFF);

        return keyList;
    }

    private int clusterEnabled;

    public int getClusterEnabled() {
        return clusterEnabled;
    }

    public void setClusterEnabled(int clusterEnabled) {
        this.clusterEnabled = clusterEnabled;
    }
}