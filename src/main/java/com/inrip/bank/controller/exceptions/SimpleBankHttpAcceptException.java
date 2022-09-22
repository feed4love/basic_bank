package com.inrip.bank.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *  Enrique AC
 */
@ResponseStatus(value=HttpStatus.ACCEPTED, reason="The request has been accepted to serve")
public class SimpleBankHttpAcceptException extends SimpleBankHTTPException {
    private static final long serialVersionUID = 1L;
    public SimpleBankHttpAcceptException(String s) {
        super(s);
    }
    public SimpleBankHttpAcceptException(String s, String d) {
        super(s,d);
    }
    public SimpleBankHttpAcceptException(String s, String d, String f) {
        super(s,d,f);
    }
}
