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
        try {
            JsonObject jsonObject = reader.readObject();
            String type = getString(msg, jsonObject, TYPE);
            if (type == null) {
                throw new DecodeException(msg, "No message type");
            }
            switch (type) {
                case LoginMessage.MESSAGE_TYPE:
                    final JsonObject data = jsonObject.getJsonObject(DATA);
                    final String sequenceId = getString(msg, jsonObject, SEQUENCE_ID);
                    final String email = getString(msg, data, EMAIL);
                    final String password = getString(msg, data, PASSWORD);
                    return new LoginMessage(sequenceId, email, password);
                default:
                    throw new DecodeException(msg, "Unknown message type: " + type);
            }
        } catch (NullPointerException | JsonException | ClassCastException e) {
            log.warning(() -> "Cannot decode the following message:\n" + msg);
            throw new DecodeException(msg, "Illegal message format", e);
        }
    }

    private String getString(String msg, JsonObject jsonObject, String key) throws DecodeException {
        final JsonValue typeValue = jsonObject.get(key);
        if (typeValue == null) {
            return null;
        }
        if (JsonValue.ValueType.STRING != typeValue.getValueType()) {
            throw new DecodeException(msg, "Unknown " + key + " format: " + typeValue);
        }
        return ((JsonString) typeValue).getString();
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
