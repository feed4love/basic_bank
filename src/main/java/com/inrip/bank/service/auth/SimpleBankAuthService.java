package com.inrip.bank.service.auth;

/**
 *
 */
public interface SimpleBankAuthService {

	Boolean validateBasicAuthentication(String userName, String password, String basicAuthHeaderValue);

}
