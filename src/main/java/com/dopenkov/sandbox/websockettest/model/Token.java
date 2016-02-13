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

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Entity
@Table(name = "token")
@NamedQueries({
    @NamedQuery(name = Token.DISCARD_TOKENS, query = "UPDATE Token t SET t.valid = false WHERE t.user = :user")
})

public class Token implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String DISCARD_TOKENS = "Token.discartTokens";

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID tokenId;

    @NotNull
    @Column(name = "issue_date")
    private OffsetDateTime issueDate;

    @NotNull
    @Column(name = "expiration_date")
    private OffsetDateTime expirationDate;

    @NotNull
    @Column(name = "token_valid")
    private boolean valid;

    @NotNull
    @JoinColumn(name = "token_user")
    @ManyToOne
    private User user;

    public Token() {
    }

    public Token(OffsetDateTime issueDate, OffsetDateTime expirationDate, User user) {
        this.issueDate = issueDate.truncatedTo(ChronoUnit.SECONDS);
        this.expirationDate = expirationDate.truncatedTo(ChronoUnit.SECONDS);
        this.user = user;
        this.valid = true;
    }

    public UUID getId() {
        return tokenId;
    }

    public OffsetDateTime getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(OffsetDateTime issueDate) {
        this.issueDate = issueDate;
    }

    public OffsetDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(OffsetDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isExpired() {
        return OffsetDateTime.now().isBefore(expirationDate);
    }

    public User getUser() {
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Token token = (Token) o;

        return tokenId.equals(token.tokenId);

    }

    @Override
    public int hashCode() {
        return tokenId.hashCode();
    }
}
