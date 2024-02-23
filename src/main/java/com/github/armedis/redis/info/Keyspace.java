package com.github.armedis.redis.info;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.google.common.base.CaseFormat;

final class Keyspace extends StatsBaseVo {
    @Override
    public Map<String, String> initOperationKeyList() {
        Map<String, String> keyList = new HashMap<>();

        keyList.put("keys", SUM);
        keyList.put("expires", SUM);
        keyList.put("avgTtl", SUM);

        return keyList;
    }

    private int no;
    private int keys;
    private int expires;
    private int avgTtl;

    /**
     * TODO Generic으로 처리.
     * 
     * @param content
     * @return
     */
    public static Map<Integer, Keyspace> fromString(String content, boolean addContentSection) {
        Map<Integer, Keyspace> keyspaceMap = new TreeMap<>();
        if (content == null) {
            return keyspaceMap;
        }

        String[] lines = content.split("\r\n");

        for (String line : lines) {
            Keyspace keyspace = new Keyspace();

            // set section content string for
            if (addContentSection) {
                keyspace.setSctionContent(content);
            }

            String[] dbNumber = line.split(":");

            String dbNo = null;
            // db0:keys=1121,exp....
            if (dbNumber.length == 2) {
                dbNo = dbNumber[0].substring(2);
                keyspace.setNo(Integer.parseInt(dbNo));

                String[] keyMember = dbNumber[1].split(",");

                for (String kv : keyMember) {
                    String[] kvArray = kv.split("=");

                    String key = kvArray[0];
                    String value = kvArray[1];

                    key = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, key);
                    ReflectionManipulator.setFieldValue(keyspace, key, value);
                }

                keyspaceMap.put(keyspace.getNo(), keyspace);
            }
        }

        return keyspaceMap;
    }

    /**
     * @return the no
     */
    public int getNo() {
        return no;
    }

    /**
     * @param no the no to set
     */
    public void setNo(int no) {
        this.no = no;
    }

    public int getKeys() {
        return keys;
    }

    public void setKeys(int keys) {
        this.keys = keys;
    }

    public int getExpires() {
        return expires;
    }

    public void setExpires(int expires) {
        this.expires = expires;
    }

    public int getAvgTtl() {
        return avgTtl;
    }

    public void setAvgTtl(int avgTtl) {
        this.avgTtl = avgTtl;
    }
}