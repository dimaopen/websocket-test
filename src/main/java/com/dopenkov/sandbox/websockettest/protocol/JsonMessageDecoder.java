package com.dopenkov.sandbox.websockettest.protocol;

import javax.inject.Inject;
import javax.json.*;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.StringReader;
import java.util.logging.Logger;

/**
 * @author Dmitry Openkov
 */
public class JsonMessageDecoder implements Decoder.Text<Message> {
    public static final String TYPE = "type";
    public static final String DATA = "data";
    public static final String SEQUENCE_ID = "sequence_id";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    @Inject
    private Logger log;

    @Override
    public Message decode(String msg) throws DecodeException {
        JsonReader reader = Json.createReader(new StringReader(msg));
        String sequenceId = null;
        try {
            JsonObject jsonObject = reader.readObject();
            sequenceId = getString(msg, null, jsonObject, SEQUENCE_ID);
            final String type = getString(msg, sequenceId, jsonObject, TYPE);
            if (type == null) {
                throw decodeException(msg, sequenceId, null, "No message type");
            }
            switch (type) {
                case LoginMessage.MESSAGE_TYPE:
                    final JsonObject data = jsonObject.getJsonObject(DATA);
                    final String email = getString(msg, sequenceId, data, EMAIL);
                    final String password = getString(msg, sequenceId, data, PASSWORD);
                    return new LoginMessage(sequenceId, email, password);
                default:
                    throw decodeException(msg, sequenceId, null, "Unknown message type: " + type);
            }
        } catch (NullPointerException | JsonException | ClassCastException e) {
            throw decodeException(msg, sequenceId, e, "Illegal message format");
        }
    }

    private String getString(String msg, String sequenceId, JsonObject jsonObject, String key) throws DecodeException {
        final JsonValue typeValue = jsonObject.get(key);
        if (typeValue == null) {
            return null;
        }
        if (JsonValue.ValueType.STRING != typeValue.getValueType()) {
            throw decodeException(msg, sequenceId, null, "Unknown " + key + " format: " + typeValue + ". It must be a string.");
        }
        return ((JsonString) typeValue).getString();
    }

    private DecodeException decodeException(String msg, String sequenceId, Exception e, final String errorMessage) {
        log.warning("Cannot decode the following message:\n" + msg);
        if (sequenceId != null) {
            return new DecodeException(msg, String.format("%s (sequence_id=%s)", errorMessage, sequenceId), e);
        } else {
            return new DecodeException(msg, errorMessage, e);
        }
    }

    @Override
    public boolean willDecode(String s) {
        return true;
    }

    @Override
    public void init(EndpointConfig config) {
    }

    @Override
    public void destroy() {
    }
}
