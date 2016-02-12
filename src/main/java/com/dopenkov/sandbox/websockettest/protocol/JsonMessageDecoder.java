package com.dopenkov.sandbox.websockettest.protocol;

import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.StringReader;

/**
 * @author Dmitry Openkov
 */
public class JsonMessageDecoder implements Decoder.Text<Message> {
    public static final String TYPE = "type";
    public static final String DATA = "data";
    public static final String SEQUENCE_ID = "sequence_id";

    @Override
    public Message decode(String msg) throws DecodeException {
        JsonReader reader = Json.createReader(new StringReader(msg));
        try {
            JsonObject jsonObject = reader.readObject();
            String type = jsonObject.getString(TYPE);
            switch (type) {
                case LoginMessage.MESSAGE_TYPE:
                    final JsonObject data = jsonObject.getJsonObject(DATA);
                    return new LoginMessage(jsonObject.getString(SEQUENCE_ID), data.getString("email"), data.getString("password"));
                default:
                    throw new DecodeException(msg, "Unknown message type: " + type);
            }
        } catch (NullPointerException | JsonException | ClassCastException e) {
            throw new DecodeException(msg, "Illegal message format", e);
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
