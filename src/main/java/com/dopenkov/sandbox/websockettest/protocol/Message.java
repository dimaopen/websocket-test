package com.dopenkov.sandbox.websockettest.protocol;

import java.util.Objects;

/**
 * @author Dmitry Openkov
 */
public abstract class Message {
    private String type;
    private String sequenceId;

    public Message(String type, String sequenceId) {
        Objects.requireNonNull(type, "type must be presented");
        this.type = type;
        this.sequenceId = sequenceId;
    }

    public String getType() {
        return type;
    }

    public String getSequenceId() {
        return sequenceId;
    }
}
