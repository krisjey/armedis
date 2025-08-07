package com.github.armedis.server.decorator;

import java.util.function.Function;

import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.HttpService;

public class IndexHtmlRedirectDecorator implements Function<HttpService, HttpService> {

    // 정적 팩토리 메서드: 데코레이터 인스턴스를 반환
    public static Function<HttpService, HttpService> newDecorator() {
        return new IndexHtmlRedirectDecorator();
    }

    @Override
    public HttpService apply(HttpService delegate) {
        return (ctx, req) -> {
            System.out.println(ctx.path() + "][" + ctx.rawPath() + "][" + ctx.mappedPath() + "][" + ctx.uri());
            if ("/".equals(ctx.path())) {
                // 루트 경로('/')로 요청이 들어오면 '/index.html'로 리다이렉트
                return HttpResponse.ofRedirect("/index.html");
            }
            // 그 외의 요청은 원래 HttpService(FileService)로 전달
            return delegate.serve(ctx, req);
        };
    }
}