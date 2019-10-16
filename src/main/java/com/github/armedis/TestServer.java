
package com.github.armedis;

import java.util.concurrent.CompletableFuture;

import com.linecorp.armeria.common.AggregatedHttpRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.common.MediaType;
import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.ServerBuilder;
import com.linecorp.armeria.server.annotation.Consumes;
import com.linecorp.armeria.server.annotation.Get;
import com.linecorp.armeria.server.annotation.Param;
import com.linecorp.armeria.server.annotation.Path;
import com.linecorp.armeria.server.annotation.Post;
import com.linecorp.armeria.server.docs.DocService;
import com.linecorp.armeria.server.logging.AccessLogWriter;
import com.linecorp.armeria.server.logging.LoggingService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestServer {
    private static final Logger logger = LoggerFactory.getLogger(TestServer.class);
    protected static final String REDIS_COMMAND = "get";

    protected static final String COMMAND_URL = "/v1/" + REDIS_COMMAND;

    protected static final String COMMAND_URL_WITH_KEY = COMMAND_URL + "/:key";

    protected static final String COMMAND_URL_WITH_KEY_GLOB = "prefix:" + COMMAND_URL;

    public static void main(String[] args) {
        ServerBuilder sb = new ServerBuilder();
        sb.http(8082);

        sb.serviceUnder("/docs", new DocService());

        sb.decorator(LoggingService.newDecorator());

        // Write access log after completing a request.
        sb.accessLogWriter(AccessLogWriter.combined(), false);

        sb.annotatedService(new Object() {
            @Get
            @Post
            @Path("/v1/gets")
            @Consumes("application/json")
            public HttpResponse greetGetWithKey(AggregatedHttpRequest httpRequest) {
                logger.info("prefix " + httpRequest.contentUtf8());
                return HttpResponse.of(HttpStatus.OK, MediaType.JSON_UTF_8, "{\"key\":\"%s\"}", httpRequest.path());
            }

            @Get
            @Post
            @Path("/v1/gets/{key}")
            @Consumes("application/json")
            public HttpResponse greetPost(AggregatedHttpRequest httpRequest, @Param("key") String name) {
                logger.info(name + " with key " + httpRequest.path());
                return HttpResponse.of(HttpStatus.OK);
            }
        });

        Server server = sb.build();
        CompletableFuture<Void> future = server.start();
        future.join();
    }
}
