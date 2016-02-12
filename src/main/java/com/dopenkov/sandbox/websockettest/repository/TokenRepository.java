package com.dopenkov.sandbox.websockettest.repository;

import com.dopenkov.sandbox.websockettest.model.Token;
import com.dopenkov.sandbox.websockettest.model.User;

import java.util.UUID;

/**
 * @author Dmitry Openkov
 */
public interface TokenRepository {
    /**
     * Retrieve a <code>Token</code> from the data store by id.
     *
     * @param id the id to search for
     * @return the <code>Token</code> if found
     */
    Token findById(UUID id);

    /**
     * Save a <code>Token</code> to the data store, either inserting or updating it.
     *
     * @param token the <code>Token</code> to save
     */
    void store(Token token);

    int discardAllTokensForUser(User user);
}
