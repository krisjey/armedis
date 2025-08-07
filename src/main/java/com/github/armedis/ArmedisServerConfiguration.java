
package com.github.armedis;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.armedis.config.ArmedisConfiguration;
import com.github.armedis.config.ConstantNames;
import com.github.armedis.config.DefaultInstanceInfo;
import com.github.armedis.grpc.service.string.RedisStringGrpcService;
import com.github.armedis.http.service.ArmeriaAnnotatedHttpService;
import com.github.armedis.utils.LogStringBuilder;
import com.linecorp.armeria.common.ServerCacheControl;
import com.linecorp.armeria.common.SessionProtocol;
import com.linecorp.armeria.common.util.BlockingTaskExecutor;
import com.linecorp.armeria.common.util.BlockingTaskExecutorBuilder;
import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.ServerBuilder;
import com.linecorp.armeria.server.docs.DocService;
import com.linecorp.armeria.server.encoding.EncodingService;
import com.linecorp.armeria.server.file.FileService;
import com.linecorp.armeria.server.file.FileServiceBuilder;
import com.linecorp.armeria.server.grpc.GrpcService;
import com.linecorp.armeria.server.grpc.GrpcServiceBuilder;
import com.linecorp.armeria.server.logging.AccessLogWriter;
import com.linecorp.armeria.server.logging.LoggingService;
import com.linecorp.armeria.spring.ArmeriaServerConfigurator;
import com.linecorp.armeria.spring.ArmeriaSettings;
import com.linecorp.armeria.spring.ArmeriaSettings.Port;

/**
 * An example of a configuration which provides beans for customizing the server
 * and client.
 */
@Configuration
@EnableAutoConfiguration
public class ArmedisServerConfiguration {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private DefaultInstanceInfo instanceInfo;

    private ArmeriaSettings settings;

    private ArmedisConfiguration armedisConfiguration;

    @Autowired
    public ArmedisServerConfiguration(ArmeriaSettings settings, ArmedisConfiguration armedisConfiguration) {
        this.settings = settings;
        this.armedisConfiguration = armedisConfiguration;
    }

    /**
     * A user can configure a {@link Server} by providing an
     * {@link ArmeriaServerConfigurator} bean.
     * 
     * prop check(prop or zookeeper) redis check
     * 
     */
    @Bean
    public ArmeriaServerConfigurator armeriaServerConfigurator(ArmeriaAnnotatedHttpService... services) {
        int listenPort = initializeServicePort();

        logger.info("Armedis Listen port " + listenPort);

        setArmeriaListenPort(listenPort);

        addShutdownHook(listenPort);

        logger.info(LogStringBuilder.makeHeader("Product info"));
        logger.info(LogStringBuilder.makeBody(" startup process begin at " + new Date()));
        logger.info(LogStringBuilder.makeBody(" Process ID : " + this.instanceInfo.getPid()));
        logger.info(LogStringBuilder.makeFooter());

        // Customize the server using the given ServerBuilder. For example:
        return builder -> {
            // Accept header is a way for a client to specify the media type of the response
            // content it is expecting
            // Content-type is a way to specify the media type of request being sent from
            // the client to the server.

            initializeHttpServerBuilder(builder);

            // Adding a Service
            // Add DocService that enables you to send Thrift and gRPC requests from web
            // browser.
            builder.serviceUnder("/docs", new DocService());

            // Log every message which the server receives and responds.
            builder.decorator(LoggingService.newDecorator());

            // Write access log after completing a request.
            builder.accessLogWriter(AccessLogWriter.combined(), true);

            // ArmeriaAnnotatedHttpService를 impl 하고 type of 로 갈라서 grpc로 등록하기.

            // Add an Armeria annotated HTTP service.
            for (ArmeriaAnnotatedHttpService service : services) {

                builder.annotatedService(service);
            }

            builder.decorator(EncodingService.newDecorator());

            // multiple class is fail.
            // Can not split RedisStringService by redis command.
            // services.put(service.getServiceDescriptor().getName(), service);
            GrpcServiceBuilder grpcServiceBuilder = GrpcService.builder();
            grpcServiceBuilder.addService(new RedisStringGrpcService());
            builder.service(grpcServiceBuilder.build());

            // Add static file serving
            FileServiceBuilder fileServiceBuilder = FileService.builder(ClassLoader.getSystemClassLoader(),
                    "/static");

            // Specify cache control directives.
            ServerCacheControl cc = ServerCacheControl.builder().maxAgeSeconds(86400).cachePublic().build();
            fileServiceBuilder.cacheControl(cc); // /* http cache "max-age=86400, public" */
            fileServiceBuilder.serveCompressedFiles(true); // for compress

            builder.serviceUnder("/", fileServiceBuilder.build());

            // redirect /#/index.html Fragment issue
//            builder.decorator(IndexHtmlRedirectDecorator.newDecorator());

//			builder.decorator(DecodingService.newDecorator());
        };
    }

    private void addShutdownHook(int listenPort) {
        try {
            instanceInfo = new DefaultInstanceInfo(String.valueOf(listenPort));

            // FIXME remove comment.. currently not used zookeeper.
            logger.info("Successfully added zookeeper node! [" + instanceInfo.getNodePath() + "] "
                    + instanceInfo.toJsonObject().toString());

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
    }

    private ServerBuilder initializeHttpServerBuilder(ServerBuilder serverBuilder) {
        int requsetTimeout = 5;
        serverBuilder.requestTimeout(Duration.ofSeconds(requsetTimeout));

        int listenPort = Integer.parseInt(getInstanceInfo().getServicePort());
        serverBuilder.http(listenPort);

        // Disable worker thread shutdown timeout() configuration. default false.
        // if set keepAliveTimeMillis value then auto enable.
        BlockingTaskExecutorBuilder blockingTaskExecutorBuilder = BlockingTaskExecutor.builder();
        blockingTaskExecutorBuilder.numThreads(500);
        blockingTaskExecutorBuilder.build();

        serverBuilder.blockingTaskExecutor(blockingTaskExecutorBuilder.build(), true);

        return serverBuilder;
    }

    /**
     * When you using spring boot with Armeria, You can not disable default port
     * 8080 on programmatically way.<br/>
     * (Using application.xml is working fine) This situation based on class loading
     * order.<br/>
     * So, You have to set the port number to ArmeriaSettings bean, Before
     * initialize Armeria AutoConfiguration class.
     * 
     * @param listenPort
     */
    private void setArmeriaListenPort(int listenPort) {
        // TODO armeria.internal-services.port
        // Change default listen port to custom port.
        List<Port> ports = new ArrayList<>();
        ports.add(new Port().setPort(listenPort).setProtocol(SessionProtocol.HTTP));
        settings.setPorts(ports);
    }

    /**
     * Configuration 정보를 사용하여 armedis 서버의 runtime 을 구성한다.
     * 1. 두 곳 모두 지정되지 않은 경우 에러
     * 2. 한 쪽에 지정된 경우 (별도 로직 필요 없음)
     * 3. 양쪽에 지정된 경우 JVM Option 우선 (Spring Boot의 기본 동작)
     * ex) application.properties에 service.port=8080, JVM Option에 -Dservice.port=9090 설정 시 servicePort 값은 9090이 됩니다.
     */
    private int initializeServicePort() {
        Integer servicePort = armedisConfiguration.getServicePort();
        Integer instanceCount = armedisConfiguration.getInstanceCount();

        if (servicePort == 0 || instanceCount == 0) {
            throw new RuntimeException("Cannot start Server. service port[" + servicePort + "] instance count[" + instanceCount + "]");
        }

        logger.info("Initialized service port : " + servicePort);

        return servicePort;
    }

    public DefaultInstanceInfo getInstanceInfo() {
        return this.instanceInfo;
    }
}
