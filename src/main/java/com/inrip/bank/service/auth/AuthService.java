package com.inrip.bank.service.auth;

/**
 * @author Enrique AC
 *
 */
public interface AuthService {

	Boolean validateBasicAuthentication(String userName, String password, String basicAuthHeaderValue);

}
