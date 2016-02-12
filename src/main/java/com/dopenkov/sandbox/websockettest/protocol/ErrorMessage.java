package com.dopenkov.sandbox.websockettest.protocol;

/**
 * @author Dmitry Openkov
 */
public class ErrorMessage extends Message {
    public static final String CUSTOMER_ERROR_MESSAGE_TYPE = "CUSTOMER_ERROR";
    private String errorDescription;
    private String errorCode;

    public ErrorMessage(String type, String sequenceId, String errorDescription, String errorCode) {
        super(type, sequenceId);
        this.errorDescription = errorDescription;
        this.errorCode = errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
