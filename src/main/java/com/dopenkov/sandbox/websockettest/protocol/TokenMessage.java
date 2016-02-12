package com.dopenkov.sandbox.websockettest.protocol;

import java.time.OffsetDateTime;

/**
 * @author Dmitry Openkov
 */
public class TokenMessage extends Message {
    public static final String MESSAGE_TYPE = "CUSTOMER_API_TOKEN";
    private String token;
    private OffsetDateTime expirationDate;

    public TokenMessage(String sequenceId, String token, OffsetDateTime expirationDate) {
        super(MESSAGE_TYPE, sequenceId);
        this.token = token;
        this.expirationDate = expirationDate;
    }

    public String getToken() {
        return token;
    }

    public OffsetDateTime getExpirationDate() {
        return expirationDate;
    }
}
