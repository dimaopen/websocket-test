package com.dopenkov.sandbox.websockettest.repository;

import com.dopenkov.sandbox.websockettest.model.User;

import java.util.Optional;

/**
 * @author Dmitry Openkov
 */
public interface UserRepository {
    /**
     * Retrieve a <code>User</code> from the data store by id.
     *
     * @param id the id to search for
     * @return the <code>User</code> if found
     */
    Optional<User> findById(Long id);

    /**
     * Retrieve a <code>User</code> from the data store by user user login name.
     *
     * @param loginName the user login name to search for
     * @return the <code>User</code> if found
     */
    Optional<User> findByLogin(String loginName);

    /**
     * Retrieve a <code>User</code> from the data store by user user login name.
     *
     * @param loginName the user login name to search for
     * @param hashedPassword the hashed password
     * @return the <code>User</code> if found
     */
    Optional<User> findByLoginAndPassword(String loginName, String hashedPassword);

    /**
     * Save a <code>User</code> to the data store, either inserting or updating it.
     *
     * @param user the <code>User</code> to save
     */
    void store(User user);
}
