package com.dopenkov.sandbox.websockettest.protocol;

import java.util.Objects;

/**
 * @author Dmitry Openkov
 */
public class ErrorMessage extends Message {
    public static final String CUSTOMER_ERROR_MESSAGE_TYPE = "CUSTOMER_ERROR";
    public static final String CUSTOMER_NOT_FOUND_CODE = "customer.notFound";
    public static final String UNSUPPORTED_MESSAGE_CODE = "message.unsupported";
    public static final String NO_SEQUENCE_ID_CODE = "sequenceId.notPresented";
    private String errorDescription;
    private String errorCode;

    public ErrorMessage(String type, String sequenceId, String errorDescription, String errorCode) {
        super(type, sequenceId);
        Objects.requireNonNull(errorDescription, "error description must be presented");
        Objects.requireNonNull(errorCode, "error code must be presented");
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
