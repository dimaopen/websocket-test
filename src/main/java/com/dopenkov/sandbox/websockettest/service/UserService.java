package com.dopenkov.sandbox.websockettest.service;

import com.dopenkov.sandbox.websockettest.model.Token;
import com.dopenkov.sandbox.websockettest.model.User;
import com.dopenkov.sandbox.websockettest.repository.UserRepository;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * @author Dmitry Openkov
 */
@Singleton
public class UserService {
    @EJB
    private UserRepository userRepository;

    @EJB
    private TokenService tokenService;

    @Inject
    private Logger log;

    @PostConstruct
    public void init() {
        final Optional<User> user1 = userRepository.findByLogin("fpi@bk.ru");
        user1.ifPresent(user -> log.info("User fpi@bk.ru exists"));
        user1.orElseGet(() -> createUser("fpi@bk.ru", "123123"));
    }

    public User createUser(String loginName, String password) throws EntityExistsException {
        Objects.requireNonNull(loginName, "loginName must not be null");
        Objects.requireNonNull(password, "password must not be null");
        final Optional<User> userOptional = userRepository.findByLogin(loginName);
        if (userOptional.isPresent()) {
            throw new EntityExistsException("User with login " + loginName + " already exists.");
        }
        final User user = new User(loginName, hash(password));
        return userRepository.store(user);
    }

    public Optional<Token> authenticate(String loginName, String password) {
        log.info(() -> "Authenticate " + loginName);
        final Optional<User> byLogin = userRepository.findByLogin(loginName);
        byLogin.ifPresent(u -> tokenService.discardAllTokensForUser(u));
        return userRepository.findByLoginAndPassword(loginName, hash(password)).
                map(u -> tokenService.issueNewTokenForUser(u));
    }

    public Optional<User> findByLogin(String loginName) {
        return userRepository.findByLogin(loginName);
    }

    public String hash(String password) {
        if (password == null) {
            return null;
        }
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            //hardly happen
            throw new RuntimeException(e);
        }

        md.update(password.getBytes(StandardCharsets.UTF_8));
        return DatatypeConverter.printHexBinary(md.digest());
    }
}
