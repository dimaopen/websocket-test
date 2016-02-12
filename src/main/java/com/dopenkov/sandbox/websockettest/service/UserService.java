package com.dopenkov.sandbox.websockettest.service;

import com.dopenkov.sandbox.websockettest.model.User;
import com.dopenkov.sandbox.websockettest.repository.UserRepository;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.persistence.EntityExistsException;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

/**
 * @author Dmitry Openkov
 */
@Singleton
public class UserService {
    @EJB
    private UserRepository userRepository;

    @PostConstruct
    public void init() {
        final Optional<User> user1 = userRepository.findByLogin("fpi@bk.ru");
        if (!user1.isPresent()) {
            userRepository.store(createUser("fpi@bk.ru", "123123"));
        }
    }

    public User createUser(String loginName, String password) throws EntityExistsException {
        final Optional<User> userOptional = userRepository.findByLogin(loginName);
        if (userOptional.isPresent()) {
            throw new EntityExistsException("User with login " + loginName + " already exists.");
        }
        final User user = new User(loginName, hash(password));
        userRepository.store(user);
        return user;
    }

    public Optional<User> findByLogin(String loginName) {
        return userRepository.findByLogin(loginName);
    }

    public String hash(String password) {
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
