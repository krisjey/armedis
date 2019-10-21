
package com.github.armedis.http.service.vo;

import com.linecorp.armeria.common.AggregatedHttpRequest;
import com.linecorp.armeria.server.ServiceRequestContext;
import com.linecorp.armeria.server.annotation.RequestConverterFunction;

public class DummyConverter implements RequestConverterFunction {
    @Override
    public Object convertRequest(ServiceRequestContext ctx, AggregatedHttpRequest request,
            Class<?> expectedResultType) {
//        if (expectedResultType == Greeting.class) {
//            // Convert the request to a Java object.
//            return new Greeting(translateToEnglish(request.contentUtf8()));
//        }

        // To the next request converter.
        return RequestConverterFunction.fallthrough();
    }

    private String translateToEnglish(String greetingInAnyLanguage) {

        return greetingInAnyLanguage;

    }
}
