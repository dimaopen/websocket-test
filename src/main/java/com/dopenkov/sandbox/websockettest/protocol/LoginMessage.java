package com.dopenkov.sandbox.websockettest.protocol;

/**
 * @author Dmitry Openkov
 */
public class LoginMessage extends Message {
    public static final String MESSAGE_TYPE = "LOGIN_CUSTOMER";
    private String email;
    private String password;

    public LoginMessage(String sequenceId, String email, String password) {
        super(MESSAGE_TYPE, sequenceId);
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
