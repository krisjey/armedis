
package com.github.armedis.http.service;

import com.linecorp.armeria.common.HttpStatus;

/**
 * @author krisjey
 */
public enum ResponseCode {
    /**
     * Success
     */
    SUCCESS(200, 1, "Ok"),

    /**
     * Unknown Server error.
     */
    UNKNOWN_ERROR(200, -1, "UnKnown error!"),

    /**
     * Unauthorized error
     */
    UNAUTHORIZED_ERROR(200, 1000, "Unauthorized error!"),

    /**
     * Request field validation error.
     */
    REQUEST_FIELD_ERROR(200, 5000, "Request field validation error!"),

    /**
     * Unsupported operation.
     */
    UNSUPPORTED_OPERATION(200, 501, "Unsupported operation(Not Implemented)!"),

    /**
     * Not supported operation.
     */
    NOTSUPPORTED_OPERATION(200, 405, "Not supported operation(Not Allowed)!"),

    /**
     * Unknown Server error.
     */
    NOT_EXIST(500, 9999, "Error Code Not exist!"),
    ;

    private final HttpStatus statusCode;
    private final int resultCode;
    private final String message;

    /**
     * README if use jRebel then you faced up a problem..
     */
//    private static final Map<Integer, ResponseCode> RESPONSE_CODE_LIST = new HashMap<>();
//    static {
//        for (ResponseCode code : values()) {
//            RESPONSE_CODE_LIST.put(code.getCode(), code);
//        }
//    }

    private ResponseCode(int statusCode, int resultCode, String message) {
        this.statusCode = HttpStatus.valueOf(statusCode);
        this.resultCode = resultCode;
        this.message = message;
    }

    public Integer getResultCode() {
        return this.resultCode;
    }

    public HttpStatus getStatusCode() {
        return this.statusCode;
    }

    public String getMessage() {
        return this.message;
    }
}
