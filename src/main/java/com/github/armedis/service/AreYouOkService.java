package com.github.armedis.service;

import org.springframework.stereotype.Component;

import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.server.annotation.Get;
import com.linecorp.armeria.server.annotation.Path;

@Component
public class AreYouOkService extends BaseService {
    @Get
    @Path(ServiceUrl.RUOK_GET)
    public HttpResponse ruok() {
        return HttpResponse.of(HttpStatus.OK);
    }
}