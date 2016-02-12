package com.dopenkov.sandbox.websockettest.protocol;

/**
 * @author Dmitry Openkov
 */
public abstract class Message {
    private String type;
    private String sequenceId;

    public Message(String type, String sequenceId) {
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
