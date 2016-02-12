package com.dopenkov.sandbox.websockettest.service;

import com.dopenkov.sandbox.websockettest.model.User;
import com.dopenkov.sandbox.websockettest.protocol.ErrorMessage;
import com.dopenkov.sandbox.websockettest.protocol.LoginMessage;
import com.dopenkov.sandbox.websockettest.protocol.Message;
import com.dopenkov.sandbox.websockettest.protocol.TokenMessage;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import javax.inject.Inject;
import java.time.OffsetDateTime;
import java.util.logging.Logger;

import static org.junit.Assert.*;

/**
 * @author Dmitry Openkov
 */
@RunWith(Arquillian.class)
public class WebSocketServiceTest {
    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addPackages(true, "com.dopenkov.sandbox.websockettest")
                .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml").addAsWebInfResource("test-ds.xml", "test-ds.xml");
    }

    @Inject
    WebSocketService service;

    @Inject
    Logger log;

    @EJB
    UserService userService;

    User user;

    @Before
    public void init() {
        user = userService.findByLogin("test@mail.com").orElseGet(() -> userService.createUser("test@mail.com", "WebSocketServiceTestPwd"));
    }

    @Test
    public void testValidLoginMessage() throws Exception {
        final Message response = service.onMessage(new LoginMessage("1001", "test@mail.com", "WebSocketServiceTestPwd"));
        assertNotNull(response);
        assertEquals("1001", response.getSequenceId());
        assertEquals(TokenMessage.MESSAGE_TYPE, response.getType());
        assertTrue(((TokenMessage) response).getExpirationDate().isAfter(OffsetDateTime.now()));
        log.info(() -> "tokenId = " + ((TokenMessage) response).getToken());
        assertEquals(36, ((TokenMessage) response).getToken().length());
    }

    @Test
    public void testNotValidLoginMessage() throws Exception {
        final Message response = service.onMessage(new LoginMessage("1001", "test@mail.com", "different"));
        assertNotNull(response);
        assertEquals("1001", response.getSequenceId());
        assertEquals(ErrorMessage.CUSTOMER_ERROR_MESSAGE_TYPE, response.getType());
        assertEquals(ErrorMessage.CUSTOMER_NOT_FOUND_CODE, ((ErrorMessage) response).getErrorCode());
    }

    @Test
    public void testLoginMessageWithNullPwd() throws Exception {
        final Message response = service.onMessage(new LoginMessage("1001", "test@mail.com", null));
        assertNotNull(response);
        assertEquals("1001", response.getSequenceId());
        assertEquals(ErrorMessage.CUSTOMER_ERROR_MESSAGE_TYPE, response.getType());
        assertEquals(ErrorMessage.CUSTOMER_NOT_FOUND_CODE, ((ErrorMessage) response).getErrorCode());
    }

    @Test
    public void testLoginMessageWithNullName() throws Exception {
        final Message response = service.onMessage(new LoginMessage("1001", null, "WebSocketServiceTestPwd"));
        assertNotNull(response);
        assertEquals("1001", response.getSequenceId());
        assertEquals(ErrorMessage.CUSTOMER_ERROR_MESSAGE_TYPE, response.getType());
        assertEquals(ErrorMessage.CUSTOMER_NOT_FOUND_CODE, ((ErrorMessage) response).getErrorCode());
    }
}