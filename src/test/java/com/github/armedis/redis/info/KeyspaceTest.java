/**
 * 
 */
package com.github.armedis.redis.info;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.Test;

/**
 * 
 */
class KeyspaceTest {

	@Test
	void test() {
		Map<Integer, Keyspace> obj1 = Keyspace.fromString("# Keyspace\r\n" + "db0:keys=1121,expires=4,avg_ttl=4143\r\n"
				+ "db1:keys=32,expires=345,avg_ttl=45\r\n" + "");
		assertThat(obj1).isNotNull();
		assertThat(obj1.size()).isEqualTo(2);

		Map<Integer, Keyspace> obj2 = Keyspace.fromString("# Keyspace\r\n" + "db0:keys=1,expires=0,avg_ttl=0\r\n" + "");
		assertThat(obj2).isNotNull();
		assertThat(obj2.size()).isEqualTo(1);
	}

}
