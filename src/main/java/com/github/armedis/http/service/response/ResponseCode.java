package com.github.armedis.http.service.response;

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
    KNOWEN_ERROR(200, -1, "UnKnown error!"),

    /**
     * Unknown Server error.
     */
    DATA_NOT_EXIST(200, -3001, "Data not exist!"),

    /**
     * Unauthorized error
     */
    UNAUTHORIZED_ERROR(200, -1000, "Unauthorized error!"),

    /**
     * Request field validation error.
     */
    REQUEST_FIELD_ERROR(200, -5000, "Request field validation error!"),

    /**
     * Unsupported operation.
     */
    UNSUPPORTED_OPERATION(200, -501, "Unsupported operation!"),

    /**
     * Unknown Server error.
     */
    NOT_EXIST(500, -999, "Error Code Not exist!"),
    ;

    private final int statusCode;
    private final int resultCode;
    private final String message;

    /**
     * if use jRebel then you faced up a problem..
     */
//    private static final Map<Integer, ResponseCode> RESPONSE_CODE_LIST = new HashMap<>();
//    static {
//        for (ResponseCode code : values()) {
//            RESPONSE_CODE_LIST.put(code.getCode(), code);
//        }
//    }

    private ResponseCode(int statusCode, int resultCode, String message) {
        this.statusCode = statusCode;
        this.resultCode = resultCode;
        this.message = message;
    }

    public Integer getResultCode() {
        return this.resultCode;
    }

    public Integer getStatusCode() {
        return this.statusCode;
    }

    public String getMessage() {
        return this.message;
    }
}
