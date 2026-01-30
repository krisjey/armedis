/**
 * 
 */
package com.github.armedis.http.service.stats;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import com.github.armedis.redis.info.RedisInfoVo;

/**
 * 
 */
public class RedisStatsInfo {
    private Long epochTime;
    private String formatedEpochTime;
    private Map<String, RedisInfoVo> redisInfoList = new HashMap<>();

    public RedisStatsInfo(ZonedDateTime currentTime) {
        epochTime = currentTime.toEpochSecond();
        formatedEpochTime = currentTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * @return the epochTime
     */
    public Long getEpochTime() {
        return epochTime;
    }

    /**
     * @param epochTime the epochTime to set
     */
    public void setEpochTime(Long epochTime) {
        this.epochTime = epochTime;
    }

    /**
     * @return the formatedEpochTime
     */
    public String getFormatedEpochTime() {
        return formatedEpochTime;
    }

    /**
     * @param formatedEpochTime the formatedEpochTime to set
     */
    public void setFormatedEpochTime(String formatedEpochTime) {
        this.formatedEpochTime = formatedEpochTime;
    }

    /**
     * @param tcpPort
     * @param redisInfo
     */
    public void put(String redisInfoId, RedisInfoVo redisInfo) {
        redisInfoList.put(redisInfoId, redisInfo);
    }

    public Map<String, RedisInfoVo> getRedisInfoList() {
        return redisInfoList;
    }

//	private String
}
