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

import com.dopenkov.sandbox.websockettest.model.User;
import com.dopenkov.sandbox.websockettest.repository.UserRepository;

import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * JPA implementation of the {@link UserRepository} interface.
 *
 * @author Dmitry Openkov
 */
@Singleton
public class JpaUserRepositoryImpl implements UserRepository {

    @Inject
    private EntityManager em;

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(this.em.find(User.class, id));
    }

    @Override
    public Optional<User> findByLogin(String loginName) {
        TypedQuery<User> query = em.createNamedQuery(User.FIND_BY_LOGIN_NAME, User.class)
                .setParameter("loginName", loginName);
        return optionalFromQuery(query);
    }

    private Optional<User> optionalFromQuery(TypedQuery<User> query) {
        final List<User> resultList = query.getResultList();
        if (resultList.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(resultList.get(0));
    }

    @Override
    public Optional<User> findByLoginAndPassword(String loginName, String hashedPassword) {
        TypedQuery<User> query = em.createNamedQuery(User.FIND_BY_LOGIN_NAME_AND_PASSWORD, User.class)
                .setParameter("loginName", loginName).setParameter("pwdHash", hashedPassword);
        return optionalFromQuery(query);
    }

    @Override
    public User store(User user) {
        if (user.getId() == null) {
            this.em.persist(user);
            return user;
        } else {
            return this.em.merge(user);
        }
    }

}
