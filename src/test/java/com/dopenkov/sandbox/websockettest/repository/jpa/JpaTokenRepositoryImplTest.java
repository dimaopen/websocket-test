package com.dopenkov.sandbox.websockettest.repository.jpa;

import com.dopenkov.sandbox.websockettest.model.Token;
import com.dopenkov.sandbox.websockettest.model.User;
import com.dopenkov.sandbox.websockettest.repository.TokenRepository;
import com.dopenkov.sandbox.websockettest.service.UserService;
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
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.logging.Logger;

import static org.junit.Assert.*;

/**
 * @author Dmitry Openkov
 */
@RunWith(Arquillian.class)
public class JpaTokenRepositoryImplTest {

    private UUID tokenId;

    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addPackages(true, "com.dopenkov.sandbox.websockettest")
                .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml").addAsWebInfResource("test-ds.xml", "test-ds.xml");
    }

    @EJB
    TokenRepository jpaTokenRepository;

    @EJB
    UserService userService;

    @Inject
    Logger log;

    User user;

    @Before
    public void init() {
        user = userService.findByLogin("user1").orElseGet(() -> userService.createUser("user1", "somePass"));
    }

    @Test
    public void testFindById() throws Exception {
        final OffsetDateTime beforeIssue = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        testStore();
        assertNotNull(tokenId);
        final Token found = jpaTokenRepository.findById(tokenId);
        assertNotNull(found);
        assertEquals(tokenId, found.getId());
        assertTrue(beforeIssue.isEqual(found.getIssueDate()) || beforeIssue.isBefore(found.getIssueDate()));
        assertTrue(found.getIssueDate().isBefore(found.getExpirationDate()));
    }

    @Test
    public void testStore() throws Exception {
        final OffsetDateTime issueDate = OffsetDateTime.now();
        final Token token = new Token(issueDate, issueDate.plusMinutes(60), user);
        jpaTokenRepository.store(token);
        tokenId = token.getId();
        assertNotNull(tokenId);
        log.info("tokenId = " + tokenId);
    }
}