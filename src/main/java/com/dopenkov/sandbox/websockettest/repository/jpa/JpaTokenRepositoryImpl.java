/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dopenkov.sandbox.websockettest.repository.jpa;

import com.dopenkov.sandbox.websockettest.model.Token;
import com.dopenkov.sandbox.websockettest.model.User;
import com.dopenkov.sandbox.websockettest.repository.TokenRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.UUID;

/**
 * JPA implementation of the {@link TokenRepository} interface.
 *
 * @author Dmitry Openkov
 */
@Stateless
public class JpaTokenRepositoryImpl implements TokenRepository {

    @Inject
    private EntityManager em;

    @Override
    public Token findById(UUID id) {
        return this.em.find(Token.class, id);
    }

    @Override
    public void store(Token token) {
        if (token.getId() == null) {
            this.em.persist(token);
        } else {
            this.em.merge(token);
        }
    }

    @Override
    public int discardAllTokensForUser(User user) {
        return em.createNamedQuery(Token.DISCARD_TOKENS)
                .setParameter("user", user).executeUpdate();
    }

}
