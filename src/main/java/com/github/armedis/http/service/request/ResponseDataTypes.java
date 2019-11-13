
package com.github.armedis.http.service.request;

public interface ResponseDataTypes {
    /**
     * Convert data from Accept http request header.  
     * Currently support {@link ResponseDataType#JSON}, {@link ResponseDataType#PLAIN_TEXT} 
     * @return ResponseDataType
     */
    ResponseDataType getResponseDataType();
}
