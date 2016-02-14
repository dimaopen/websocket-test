package com.dopenkov.sandbox.websockettest.protocol;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.websocket.DecodeException;
import javax.websocket.EncodeException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * @author Dmitry Openkov
 */
public class JsonMessageEncoderTest {

    @Test
    public void testEncodeTokenMessage() throws EncodeException {
        final JsonMessageEncoder encoder = createEncoder();
        final String msgJson = encoder.encode(new TokenMessage("100", "token100"
                , OffsetDateTime.of(2050, 2, 3, 10, 10, 10, 0, ZoneOffset.ofHours(0))));
        System.out.println("msgJson = " + msgJson);
        assertTrue(msgJson.startsWith("{"));
        assertTrue(Pattern.compile("\"type\"\\s*:\\s*\"CUSTOMER_API_TOKEN\"").matcher(msgJson).find());
        assertTrue(Pattern.compile("\"sequence_id\"\\s*:\\s*\"100\"").matcher(msgJson).find());
        assertTrue(Pattern.compile("\"api_token\"\\s*:\\s*\"token100\"").matcher(msgJson).find());
        assertTrue(Pattern.compile("\"api_token_expiration_date\"\\s*:\\s*\"2050-02-03T10:10:10Z\"").matcher(msgJson).find());
    }

    @Test
    public void testEncodeErrorMessage() throws EncodeException {
        final JsonMessageEncoder encoder = createEncoder();
        final String msgJson = encoder.encode(new ErrorMessage(ErrorMessage.CUSTOMER_ERROR_MESSAGE_TYPE, "100"
                , "error description", ErrorMessage.CUSTOMER_NOT_FOUND_CODE));
        System.out.println("msgJson = " + msgJson);
        assertTrue(msgJson.startsWith("{"));
        assertTrue(Pattern.compile("\"type\"\\s*:\\s*\"CUSTOMER_ERROR\"").matcher(msgJson).find());
        assertTrue(Pattern.compile("\"sequence_id\"\\s*:\\s*\"100\"").matcher(msgJson).find());
        assertTrue(Pattern.compile("\"error_description\"\\s*:\\s*\"error description\"").matcher(msgJson).find());
        assertTrue(Pattern.compile("\"error_code\"\\s*:\\s*\"customer.notFound\"").matcher(msgJson).find());
    }

    @Test
    public void testEncodeErrorMessageWithEmptySequenceId() throws EncodeException {
        final JsonMessageEncoder encoder = createEncoder();
        final String msgJson = encoder.encode(new ErrorMessage(ErrorMessage.CUSTOMER_ERROR_MESSAGE_TYPE, null
                , "error description", ErrorMessage.CUSTOMER_NOT_FOUND_CODE));
        System.out.println("msgJson = " + msgJson);
        assertTrue(msgJson.startsWith("{"));
        assertTrue(Pattern.compile("\"type\"\\s*:\\s*\"CUSTOMER_ERROR\"").matcher(msgJson).find());
        assertTrue(Pattern.compile("\"sequence_id\"\\s*:\\s*\"\"").matcher(msgJson).find());
        assertTrue(Pattern.compile("\"error_description\"\\s*:\\s*\"error description\"").matcher(msgJson).find());
        assertTrue(Pattern.compile("\"error_code\"\\s*:\\s*\"customer.notFound\"").matcher(msgJson).find());
    }

    private JsonMessageEncoder createEncoder() {
        final JsonMessageEncoder jsonMessageEncoder = new JsonMessageEncoder();
        final JsonBuilderFactory builderFactory = Json.createBuilderFactory(Collections.EMPTY_MAP);
        try {
        Field field = JsonMessageEncoder.class.getDeclaredField("builderFactory");
        field.setAccessible(true);
            field.set(jsonMessageEncoder, builderFactory);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            //hardly happen in test env
            throw new RuntimeException(e);
        }
        return jsonMessageEncoder;
    }


}