# armedis
An HTTP2/gRPC interface for Redis, Support Redis stand-alone/cluster mode.

# currently not implemented
# In progress.

# Load map
## Redis Http interface, Support redis cluster, stand alone mode.

<!--
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
