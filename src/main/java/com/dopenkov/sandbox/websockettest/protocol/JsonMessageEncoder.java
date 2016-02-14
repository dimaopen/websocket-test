package com.dopenkov.sandbox.websockettest.protocol;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import java.io.StringWriter;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * @author Dmitry Openkov
 */
public class JsonMessageEncoder implements Encoder.Text<Message> {
    @Inject
    private JsonBuilderFactory builderFactory;

    @Override
    public String encode(Message msg) throws EncodeException {
        final JsonObjectBuilder jsonBuilder = builderFactory.createObjectBuilder()
                .add(JsonMessageDecoder.TYPE, msg.getType())
                .add(JsonMessageDecoder.SEQUENCE_ID, convertToEmptyStringIfNull(msg.getSequenceId()));
        final JsonObjectBuilder data = builderFactory.createObjectBuilder();
        switch (msg.getType()) {
            case ErrorMessage.CUSTOMER_ERROR_MESSAGE_TYPE:
                data.add("error_description", ((ErrorMessage) msg).getErrorDescription());
                data.add("error_code", ((ErrorMessage) msg).getErrorCode());
                break;
            case TokenMessage.MESSAGE_TYPE:
                data.add("api_token", ((TokenMessage) msg).getToken());
                data.add("api_token_expiration_date", ((TokenMessage) msg).getExpirationDate()
                        .truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ISO_INSTANT));
                break;
            default:
                throw new EncodeException(msg, "Unknown message type: " + msg.getType());
        }
        StringWriter stWriter = new StringWriter();
        JsonWriter jsonWriter = Json.createWriter(stWriter);
        jsonWriter.writeObject(jsonBuilder.add(JsonMessageDecoder.DATA, data.build()).build());
        jsonWriter.close();
        return stWriter.toString();
    }

    private String convertToEmptyStringIfNull(String str) {
        return str == null ? "" : str;
    }

    @Override
    public void init(EndpointConfig config) {
    }

    @Override
    public void destroy() {
    }
}
