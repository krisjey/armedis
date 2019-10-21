
package com.github.armedis.http.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.context.ApplicationContext;

import com.github.armedis.ArmedisServerConfiguration;
import com.github.armedis.config.ConstantNames;
import com.github.armedis.config.DefaultInstanceInfo;
import com.github.armedis.spring.ApplicationContextProvider;
import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.DecoratingServiceFunction;
import com.linecorp.armeria.server.Service;
import com.linecorp.armeria.server.ServiceRequestContext;

/**
 * TransactionSeqDecorator
 * @author krisjey
 */
public class TransactionSeqDecoratorImpl implements DecoratingServiceFunction<HttpRequest, HttpResponse> {

    private static final AtomicLong TRANSACTION_SEQ_VALUE = new AtomicLong();

    private static final DateTimeFormatter formatPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.nnnnnnnnn");

    private static final String instanceInfo = getInstanceInfo();

    private static final String getInstanceInfo() {
        ApplicationContext context = ApplicationContextProvider.getApplicationContext();

        String instanceInfoString = null;

        ArmedisServerConfiguration armeriaHttpServerConfiguration = context.getBean(ArmedisServerConfiguration.class);
        DefaultInstanceInfo instanceInfo = armeriaHttpServerConfiguration.getInstanceInfo();

        instanceInfoString = instanceInfo.getHost() + ":" + instanceInfo.getServicePort();

        return instanceInfoString;
    }

    @Override
    public HttpResponse serve(Service<HttpRequest, HttpResponse> delegate,
            ServiceRequestContext ctx, HttpRequest req) throws Exception {
        ctx.addAdditionalResponseHeader(ConstantNames.SERVICE_TRANSACTION_SEQ,
                String.valueOf(TRANSACTION_SEQ_VALUE.incrementAndGet()));
        ctx.addAdditionalResponseHeader(ConstantNames.HTTP_REQUEST_START, LocalDateTime.now().format(formatPattern));
        ctx.addAdditionalResponseHeader(ConstantNames.ARMEDIS_INSTANCE_INFO, instanceInfo);

        return delegate.serve(ctx, req);
    }
}
