
package com.github.armedis.redis.key;

/**
 * Interface of Make a key
 * @author krisjey
 *
 */
public interface KeyMaker {
    static final int SEED_MURMURHASH = 0x1234ABCD;

    public static final String DELIMITER = "::";

    /**
     * build a key
     * @return if the key is already made then return it, otherwise make a key by the implemented method. 
     */
    public String getKey();
}