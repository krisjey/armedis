package com.github.armedis.http.service.stats;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

class RedisStatInfoBucketTest {
    @Test
    void test() {
        String nodes = """
                705e3b8567283cb7b7ea04307fdee75c59753d1c 192.168.56.105:17003@27003 master - 0 1702912058000 3 connected 10923-16383
                a61d555424b8b0783f8baabccded129824fb1ad1 192.168.56.105:17006@27006 slave 5b71f03fc10fb0966dedc865b824249b1c2d3b3a 0 1702912058240 2 connected
                22c57a0b938a8f034611dc287981438a7b3e0d76 192.168.56.105:17005@27005 slave 3d99533fa02cdaff449a9588fad627c1dee9a0d7 0 1702912056466 8 connected
                5b71f03fc10fb0966dedc865b824249b1c2d3b3a 192.168.56.105:17002@27002 master - 0 1702912057000 2 connected 5461-10922
                b9996ee5a04709093a400e9c6d5a3a4a61e04184 192.168.56.105:17004@27004 slave 705e3b8567283cb7b7ea04307fdee75c59753d1c 0 1702912059369 3 connected
                3d99533fa02cdaff449a9588fad627c1dee9a0d7 192.168.56.105:17001@27001 myself,master - 0 1702912056000 8 connected 0-5460
                """;

        List<RedisClusterNodeInfo> redisNodeInfo = parseNodeInfo(nodes);

        assertThat(redisNodeInfo).isNotEmpty();
        assertThat(redisNodeInfo.get(0).ip()).isEqualTo("192.168.56.105");
    }

    private List<RedisClusterNodeInfo> parseNodeInfo(String nodes) {
        List<RedisClusterNodeInfo> redisNodeInfo = new ArrayList<RedisClusterNodeInfo>();
        List<String> nodeInfoStrings = IOUtils.readLines(new StringReader(nodes));

        for (String nodeInfoString : nodeInfoStrings) {
            RedisClusterNodeInfo nodeInfo = RedisClusterNodeInfo.of(nodeInfoString);
            redisNodeInfo.add(nodeInfo);
            System.out.println(nodeInfo);
        }

        return redisNodeInfo;
    }

}
