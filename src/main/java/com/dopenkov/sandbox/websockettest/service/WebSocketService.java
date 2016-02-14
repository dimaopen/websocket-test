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
package com.dopenkov.sandbox.websockettest.service;

import com.dopenkov.sandbox.websockettest.protocol.*;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ServerEndpoint(value = "/messages", encoders = {JsonMessageEncoder.class}, decoders = {JsonMessageDecoder.class})
@Stateless
public class WebSocketService {
    @Inject
    private Logger log;

    @EJB
    private UserService userService;

    @OnMessage
    public Message onMessage(Message msg) {
        if (msg.getSequenceId() == null) {
            return new ErrorMessage(ErrorMessage.CUSTOMER_ERROR_MESSAGE_TYPE, msg.getSequenceId(),
                    "No sequence id in message", ErrorMessage.NO_SEQUENCE_ID_CODE);
        }
        if (msg instanceof LoginMessage) {
            LoginMessage loginMessage = (LoginMessage) msg;
            return userService.authenticate(loginMessage.getEmail(), loginMessage.getPassword())
                    .<Message>map(t -> new TokenMessage(msg.getSequenceId(), t.getId().toString(), t.getExpirationDate()))
                    .orElse(
                            new ErrorMessage(ErrorMessage.CUSTOMER_ERROR_MESSAGE_TYPE, msg.getSequenceId(), "Customer not found"
                                    , ErrorMessage.CUSTOMER_NOT_FOUND_CODE)
                    );
        }
        return new ErrorMessage(ErrorMessage.CUSTOMER_ERROR_MESSAGE_TYPE, msg.getSequenceId(),
                "Unsupported message", ErrorMessage.UNSUPPORTED_MESSAGE_CODE);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        if (error instanceof DecodeException) {
            //extract sequence_id if possible
            final Matcher m = Pattern.compile("\\(sequence_id=([\\w-]+)\\)").matcher(error.getMessage());
            String sequenceId = m.find() ? m.group(1) : "no_sequence_id";
            try {
                session.getBasicRemote().sendObject(new ErrorMessage(ErrorMessage.CUSTOMER_ERROR_MESSAGE_TYPE, sequenceId,
                    error.getMessage(), ErrorMessage.UNSUPPORTED_MESSAGE_CODE));
            } catch (IOException e) {
                log.log(Level.WARNING, "Cannot send message to client", e);
            } catch (EncodeException e) {
                //hardly happen
                log.log(Level.SEVERE, "Cannot encode error message", e);
            }
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        log.info("WebSocket opened: " + session.getId());
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        log.info("WebSocket closed: " + reason.getCloseCode());
    }
}
