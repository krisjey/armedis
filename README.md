# armedis
- An HTTP2/gRPC interface for Redis, Support Redis stand-alone/cluster mode.
## Support for Redis monitoring.

# currently not implemented
# In progress.

# Load map
## Redis Http interface, Support redis cluster, stand alone mode.

# How to test
- gradlew -Dspring.profiles.active=single cleanTest test
- gradlew -Dspring.profiles.active=replication cleanTest test
- gradlew -Dspring.profiles.active=cluster cleanTest test
- gradlew -Dspring.profiles.active=sentinel cleanTest test

<!--
java -DSERVICE_PORT=8080 -Dservice.instanceCount=1 -Dlogging.config=./logback-spring.xml -jar armedis-1.0.0-SNAPSHOT.jar


/ config
config 명령어는 http 메서드로 분리(get/post)
/v1/management/settings/config  -- config get key
키 목록 조회 API get
/v1/management/settings/configkeys

Client connections

Command statistics

cluster nodes


/v1/management/slowlog/get  -- slowlog get 10
/v1/management/slowlog/len  -- slowlog len
/v1/management/slowlog/reset  -- slowlog reset

slow log id, time, duration, key, value, 

add testcase for service.
for description

// # https://github.com/moon4311/gradle_boot

0. established every type of redis.

1. type of redis config
- single master
- master-slave
- master-slave with sentinel
- cluster

2. test
- connect each type of redis and test
- test : set, get, hget, mget, mset etc...

3. support grpc.

4. support topology provider for each type of redis, except single node.

5. Add circuit breaker feature.

6. Add spring actuator feature and configuration.
	- done.

7. What the difference in applying gradle plugin
plugins vs apply plugin  
apply plugin: 'someplugin1'

plugins {
   id 'org.hidetake.ssh' version '1.1.2'
}

https://stackoverflow.com/questions/32352816/what-the-difference-in-applying-gradle-plugin
	
----------- known issue ----------
* READONLY You can't write against a read only slave.
Need RedisServerDetector debugging

http://192.168.56.1:8080/v1/get/hello
-->
