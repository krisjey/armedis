package com.github.armedis.http.service.stats;

import org.apache.commons.lang3.StringUtils;

public class RedisClusterNodeInfoConverter {
    public static RedisClusterNodeInfo convert(String nodeInfoString) {
        // nodeInfoString(12, 10) ==> id, ip, listenPort, clusterBusPort,
        // flags,masterId,
        // pingSend, pongRecv, configEpoch, linkState, shardSlotStart,
        // shardSlotEnd
        String[] nodeInfoArray = StringUtils.split(nodeInfoString, " :@");

        RedisClusterNodeInfo nodeInfo = new RedisClusterNodeInfo();
        nodeInfo.clusterId(nodeInfoArray[0]);
        nodeInfo.ip(nodeInfoArray[1]);
        nodeInfo.listenPort(Integer.parseInt(nodeInfoArray[2]));
        nodeInfo.clusterBusPort(Integer.parseInt(nodeInfoArray[3]));
        nodeInfo.flags(nodeInfoArray[4]);
        nodeInfo.masterId(nodeInfoArray[5]);
        nodeInfo.pingSend(Long.parseLong(nodeInfoArray[6]));
        nodeInfo.pongRecv(Long.parseLong(nodeInfoArray[7]));
        nodeInfo.configEpoch(Integer.parseInt(nodeInfoArray[8]));
        nodeInfo.linkState(nodeInfoArray[9]);
        if (nodeInfoArray.length > 10) {
            nodeInfo.shardSlotStart(Integer.parseInt(StringUtils.split(nodeInfoArray[10], "-")[0]));
            nodeInfo.shardSlotEnd(Integer.parseInt(StringUtils.split(nodeInfoArray[10], "-")[1]));
        }

        return nodeInfo;
    }

}
