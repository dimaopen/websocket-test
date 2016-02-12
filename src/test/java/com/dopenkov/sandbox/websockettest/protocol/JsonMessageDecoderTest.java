package com.dopenkov.sandbox.websockettest.protocol;

import org.junit.Test;

import javax.websocket.DecodeException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * @author Dmitry Openkov
 */
public class JsonMessageDecoderTest {

    @Test
    public void testDecodeValidMessage() throws DecodeException {
        final JsonMessageDecoder jsonMessageDecoder = new JsonMessageDecoder();
        final Message msg = jsonMessageDecoder.decode(getResourceAsString("/messages/validLoginRequest.json"));
        assertNotNull(msg);
        assertEquals(LoginMessage.MESSAGE_TYPE, msg.getType());
        assertEquals("a29e4fd0-581d-e06b-c837-4f5f4be7dd18", msg.getSequenceId());
        assertThat(msg, is(instanceOf(LoginMessage.class)));
        assertEquals("fpi@bk.ru", ((LoginMessage) msg).getEmail());
        assertEquals("123123", ((LoginMessage) msg).getPassword());
    }

    @Test
    public void testDecodeValidMessageNoFields() throws DecodeException {
        final JsonMessageDecoder jsonMessageDecoder = new JsonMessageDecoder();
        final Message msg = jsonMessageDecoder.decode(getResourceAsString("/messages/validLoginRequestNoFields.json"));
        assertNotNull(msg);
        assertEquals(LoginMessage.MESSAGE_TYPE, msg.getType());
        assertNull(msg.getSequenceId());
        assertThat(msg, is(instanceOf(LoginMessage.class)));
        assertEquals("fpi@bk.ru", ((LoginMessage) msg).getEmail());
        assertNull(((LoginMessage) msg).getPassword());
    }

    @Test(expected = DecodeException.class)
    public void testDecodeValidJsonInvalidRequest() throws DecodeException {
        final JsonMessageDecoder jsonMessageDecoder = new JsonMessageDecoder();
        jsonMessageDecoder.decode(getResourceAsString("/messages/validJsonInvalidRequest.json"));
    }

    private String getResourceAsString(String name) {
        final URL resource = getClass().getResource(name);
        if (resource == null) {
            throw new IllegalArgumentException("No such resource: " + name);
        }
        try {
            return new String(Files.readAllBytes(Paths.get(resource.toURI())));
        } catch (IOException | URISyntaxException e) {
            //hardly happen
            throw new RuntimeException(e);
        }
    }


}