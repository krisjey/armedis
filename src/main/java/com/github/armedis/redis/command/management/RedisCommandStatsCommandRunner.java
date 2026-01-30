
package com.github.armedis.redis.command.management;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.github.armedis.config.RedisCallbackFactory;
import com.github.armedis.http.service.stats.RedisClusterNodeInfo;
import com.github.armedis.http.service.stats.RedisStatInfoBucket;
import com.github.armedis.redis.RedisInstanceType;
import com.github.armedis.redis.RedisNode;
import com.github.armedis.redis.command.AbstractRedisCommandRunner;
import com.github.armedis.redis.command.RedisCommandEnum;
import com.github.armedis.redis.command.RedisCommandExecuteResult;
import com.github.armedis.redis.command.RedisCommandExecuteResultFactory;
import com.github.armedis.redis.command.RequestRedisCommandName;
import com.github.armedis.redis.connection.RedisServerDetector;
import com.github.armedis.redis.info.RedisInfoVo;

@Component
@Scope("prototype")
@RequestRedisCommandName(RedisCommandEnum.COMMANDSTATS)
public class RedisCommandStatsCommandRunner extends AbstractRedisCommandRunner {
    private final Logger logger = LoggerFactory.getLogger(RedisCommandStatsCommandRunner.class);

    @SuppressWarnings("unused")
    private static final boolean classLoaded = detectAnnotation(RedisCommandStatsCommandRunner.class);

    private RedisCommandStatsRequest redisRequest;
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisServerDetector redisServerDetector;

    @Autowired
    private RedisStatInfoBucket bucket;

    public RedisCommandStatsCommandRunner(RedisCommandStatsRequest redisRequest, RedisTemplate<String, Object> redisTemplate) {
        this.redisRequest = redisRequest;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public RedisCommandExecuteResult executeAndGet() {
        logger.info(redisRequest.toString());

        List<RedisClusterNodeInfo> redisNodeInfo = new ArrayList<RedisClusterNodeInfo>();

        if (RedisInstanceType.CLUSTER.equals(redisServerDetector.getRedisInstanceType())) {
            String clusterNodes = this.redisTemplate.execute(RedisCallbackFactory.getClusterNodesCallback());

            List<String> nodeInfoStrings = IOUtils.readLines(new StringReader(clusterNodes));

            for (String nodeInfoString : nodeInfoStrings) {
                redisNodeInfo.add(RedisClusterNodeInfo.of(nodeInfoString));
            }
        }
        else {
            Set<RedisNode> nodes = redisServerDetector.getAllNodes();
            Map<String, RedisInfoVo> redisStatInfoMap = bucket.getFirstRedisInfoMap();

            for (RedisNode node : nodes) {
                String runId = redisStatInfoMap.get(node.getHost() + ":" + node.getPort()).getServer().getRunId();
                RedisClusterNodeInfo nodeInfo = new RedisClusterNodeInfo();
                nodeInfo.setId(runId);
                nodeInfo.setIp(node.getHost());
                nodeInfo.setListenPort(node.getPort());
                nodeInfo.setClusterBusPort(node.getPort());
                nodeInfo.setFlags(node.getRole().name());
                nodeInfo.setMasterId(runId);
                nodeInfo.setPingSend(System.currentTimeMillis());
                nodeInfo.setPongRecv(System.currentTimeMillis());
                nodeInfo.setConfigEpoch(0);
                nodeInfo.setLinkState("connected");
                nodeInfo.setShardSlotStart(0);
                nodeInfo.setShardSlotEnd(16383);

                redisNodeInfo.add(nodeInfo);
            }
        }

        return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult(bucket.getCommandStats());
    }
}
