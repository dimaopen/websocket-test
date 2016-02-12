/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dopenkov.sandbox.websockettest.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "user",
        indexes = @Index(name = "user_login_pwd_idx", columnList = "login_name, pwd_hash", unique = true),
        uniqueConstraints = @UniqueConstraint(columnNames = "login_name")
)
@NamedQueries({
    @NamedQuery(name = User.FIND_BY_LOGIN_NAME, query = "SELECT u FROM User u WHERE u.loginName = :loginName"),
    @NamedQuery(name = User.FIND_BY_LOGIN_NAME_AND_PASSWORD, query = "SELECT u FROM User u " +
            "WHERE u.loginName = :loginname AND u.userPasswordHash = :pwdHash")
})
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String FIND_BY_LOGIN_NAME = "User.findByLoginName";
    public static final String FIND_BY_LOGIN_NAME_AND_PASSWORD = "User.findByLoginNameAndPassword";

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Integer userId;

    @NotNull
    @Size(min = 4)
    @Column(name = "login_name")
    private String loginName;

    @NotNull
    @Column(name = "pwd_hash")
    private String userPasswordHash;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Token> tokens;

    public User() {
    }

    public User(String loginName, String userPasswordHash) {
        this.loginName = loginName;
        this.userPasswordHash = userPasswordHash;
    }

    public Integer getId() {
        return userId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserPasswordHash() {
        return userPasswordHash;
    }

    public void setUserPasswordHash(String userPasswordHash) {
        this.userPasswordHash = userPasswordHash;
    }

    public Set<Token> getTokens() {
        return tokens;
    }
}
