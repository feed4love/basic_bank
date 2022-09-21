package com.inrip.bank.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *  Enrique AC
 */
@ResponseStatus(value=HttpStatus.ACCEPTED, reason="The request has been accepted to serve")
public class HttpAcceptException extends HTTPException {
    private static final long serialVersionUID = 1L;
    public HttpAcceptException(String s) {
        super(s);
    }
    public HttpAcceptException(String s, String d) {
        super(s,d);
    }
    public HttpAcceptException(String s, String d, String f) {
        super(s,d,f);
    }
}
