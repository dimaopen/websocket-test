package com.dopenkov.sandbox.websockettest.service;

import com.dopenkov.sandbox.websockettest.model.Token;
import com.dopenkov.sandbox.websockettest.model.User;
import com.dopenkov.sandbox.websockettest.repository.TokenRepository;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import java.time.OffsetDateTime;

/**
 * @author Dmitry Openkov
 */
@Singleton
public class TokenService {
    private static final int TOKEN_LIFE_TIME_MINUTES = 60;

    @EJB
    private TokenRepository tokenRepository;

    public void discardAllTokensForUser(User user) {
        tokenRepository.discardAllTokensForUser(user);
    }

    public Token issueNewTokenForUser(User user) {
        final OffsetDateTime issueDate = OffsetDateTime.now();
        final Token token = new Token(issueDate, issueDate.plusMinutes(TOKEN_LIFE_TIME_MINUTES), user);
        tokenRepository.store(token);
        return token;
    }

    public Token findToken(String value) {
        //todo
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public boolean validateToken(String token) {
        //todo
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
