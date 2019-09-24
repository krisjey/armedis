package com.github.armedis;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;

import javax.naming.OperationNotSupportedException;

import static java.util.Objects.requireNonNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.service.NewdataServiceImpl;
import com.github.armedis.config.ArmedisConfiguration;
import com.github.armedis.config.ConstantNames;
import com.github.armedis.config.DefaultInstanceInfo;
import com.github.armedis.redis.RedisConnector;
import com.github.armedis.redis.RedisInstance;
import com.github.armedis.service.ArmeriaAnnotatedHttpService;
import com.github.armedis.service.ServerShutdownHook;
import com.github.armedis.utils.LogStringBuilder;
import com.linecorp.armeria.common.CommonPools;
import com.linecorp.armeria.common.SessionProtocol;
import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.ServerBuilder;
import com.linecorp.armeria.server.docs.DocService;
import com.linecorp.armeria.server.grpc.GrpcServiceBuilder;
import com.linecorp.armeria.server.logging.LoggingService;
import com.linecorp.armeria.spring.ArmeriaServerConfigurator;
import com.linecorp.armeria.spring.ArmeriaSettings;
import com.linecorp.armeria.spring.ArmeriaSettings.Port;

/**
 * An example of a configuration which provides beans for customizing the server and client.
 */
@Configuration
public class ArmedisServerConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(ArmedisServerConfiguration.class);

    private DefaultInstanceInfo instanceInfo;

    @Autowired
    ArmeriaSettings settings;

    @Autowired
    ArmedisConfiguration armedisConfiguration;

    /**
     * A user can configure a {@link Server} by providing an {@link ArmeriaServerConfigurator} bean.
     * 
     * prop check(prop or zookeeper)
     * redis check
     * 
     */
    @Bean
    public ArmeriaServerConfigurator armeriaServerConfigurator(ArmeriaAnnotatedHttpService... services) {
        int listenPort = initializeServicePort();

        setArmeriaListenPort(listenPort);

        initializeRedisCluster();

        try {
            instanceInfo = new DefaultInstanceInfo(String.valueOf(listenPort));

            logger.info("Successfully added zookeeper node! [" + instanceInfo.getNodePath() + "] " + instanceInfo.toJsonObject().toString());

            logger.info(LogStringBuilder.makeReadableLine(1));
            logger.info(LogStringBuilder.makeHeader("Add shutdownhook"));
            logger.info(LogStringBuilder.makeBody("ClipboardServerShutdownHook added!" + instanceInfo.getNodePath()));
            logger.info(LogStringBuilder.makeReadableLine(1));
            logger.info(LogStringBuilder.makeFooter());

            Runtime.getRuntime().addShutdownHook(new ServerShutdownHook(instanceInfo.getNodePath()));
        }
        catch (Exception e) {
            throw new RuntimeException("Cannot start " + listenPort + " server at initServer", e);
        }

        logger.info(LogStringBuilder.makeHeader("Product info"));
        logger.info(LogStringBuilder.makeBody(" startup process begin at " + new Date()));
        logger.info(LogStringBuilder.makeBody(" Process ID : " + this.instanceInfo.getPid()));
        logger.info(LogStringBuilder.makeFooter());

        // Customize the server using the given ServerBuilder. For example:
        return builder -> {
            initializeServerBuilderByConfig(builder);

            // Add DocService that enables you to send Thrift and gRPC requests from web browser.
            builder.serviceUnder("/docs", new DocService());

//            // Log every message which the server receives and responds.
            builder.decorator(LoggingService.newDecorator());

//            // Write access log after completing a request.
//            builder.accessLogWriter(AccessLogWriter.combined(), false);

            // ArmeriaAnnotatedHttpService를 impl 하고 type of 로 갈라서 grpc로 등록하기.

            // Add an Armeria annotated HTTP service.
            for (ArmeriaAnnotatedHttpService service : services) {
                builder.annotatedService(service);
            }

            // You can also bind asynchronous RPC services such as Thrift and gRPC:
            // builder.service(THttpService.of(...));
            // builder.service(new GrpcServiceBuilder()...build());

            // with spring
//            builder.service(new GrpcServiceBuilder().addService(newdataServiceImpl).build());

            builder.service(new GrpcServiceBuilder().addService(new NewdataServiceImpl()).build());

        };
    }

    /**
     * 일단 돌아가게
     * 먼저 클러스터
     */
    private void initializeRedisCluster() {
        RedisConnector redisConnector = new RedisConnector(armedisConfiguration.getRedisSeedAddress());
        try {
            // FIXME 연결이 살아있는지 먼저 테스트 후 응답 던지기.
            Set<RedisInstance> redisNodes = redisConnector.lookupNodes();
        }
        catch (OperationNotSupportedException e) {
            e.printStackTrace();
        }
    }

    private ServerBuilder initializeServerBuilderByConfig(ServerBuilder serverBuilder) {
        int requsetTimeout = 5;
        serverBuilder.requestTimeout(Duration.ofSeconds(requsetTimeout));

        int listenPort = Integer.parseInt(getInstanceInfo().getServicePort());
        serverBuilder.http(listenPort);

        ThreadPoolExecutor executor = (ThreadPoolExecutor) CommonPools.blockingTaskExecutor();

        // Disable worker thread shutdown timeout() configuration.
        executor.allowCoreThreadTimeOut(false);
        serverBuilder.blockingTaskExecutor(executor, true);

        return serverBuilder;
    }

    /**
     * When you using spring boot with Armeria, You can not disable default port 8080 on programmatically way.<br/>
     * (Using application.xml is working fine)
     * This situation based on class loading order.<br/>
     * So, You have to set the port number to ArmeriaSettings bean, Before initialize Armeria AutoConfiguration class.
     * 
     * @param listenPort
     */
    private void setArmeriaListenPort(int listenPort) {
        // Change default listen port to custom port.
        List<Port> ports = new ArrayList<>();
        ports.add(new Port().setPort(listenPort).setProtocol(SessionProtocol.HTTP));
        settings.setPorts(ports);
    }

    /**
     * Zookeeper에 등록된 정보와 System properties 정보로 서버의 실행환경을 구성한다.
     */
    private int initializeServicePort() {
        // FIXME 이게 없어서 문제 발생 가능.
        String paramServicePort = System.getProperty(ConstantNames.SERVICE_PORT_PARAM_NAME);
        int listenPort = 0;

        // FIXME 이게 없어서 문제 발생함.
        int servicePortFromParam = listenPort = Integer.parseInt(paramServicePort == null ? "0" : paramServicePort);

        int configServicePort = armedisConfiguration.getServicePort();
        int instanceCount = armedisConfiguration.getInstanceCount();

        if (instanceCount == 0 || configServicePort == 0) {
            throw new RuntimeException(
                    "Cannot start Server. service port[" + configServicePort + "] instance count[" +
                            instanceCount + "] but request is [" + servicePortFromParam + "]");
        }

        if (configServicePort <= servicePortFromParam && (configServicePort + instanceCount - 1) >= servicePortFromParam) {
            // do nothing
        }
        else {
            throw new RuntimeException(
                    "Cannot start Server. service port[" + configServicePort + "] instance count[" +
                            instanceCount + "] but request is [" + servicePortFromParam + "]");
        }

        logger.info("Initialized service port : " + listenPort);

        return listenPort;
    }

    public DefaultInstanceInfo getInstanceInfo() {
        return this.instanceInfo;
    }
}
