package com.github.armedis.server.decorator;

import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.server.DecoratingHttpServiceFunction;
import com.linecorp.armeria.server.HttpService;
import com.linecorp.armeria.server.ServiceRequestContext;

public class IndexHtmlRedirectDecorator implements DecoratingHttpServiceFunction {

    @Override
    public HttpResponse serve(HttpService delegate, ServiceRequestContext ctx, HttpRequest req) throws Exception {
        String path = req.path();

        // 루트 경로로 들어왔을 경우만 리디렉트 수행
        if ("/".equals(path)) {
//            return HttpResponse.ofRedirect("/#/index.html");
            return HttpResponse.ofRedirect(HttpStatus.MOVED_PERMANENTLY, "/#/index.html");
        }

        // favicon.ico나 다른 경로는 원래 서비스로 처리
        return delegate.serve(ctx, req);
    }

    public static DecoratingHttpServiceFunction newDecorator() {
        return new IndexHtmlRedirectDecorator();
    }
}