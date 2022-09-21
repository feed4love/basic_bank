package com.inrip.bank.service.auth;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author Enrique AC
 *
 */
@Service
public class AuthServiceImpl implements AuthService {

	@Override
	public Boolean validateBasicAuthentication(String appUserName, String appPassword, String basicAuthHeaderValue) {

		if (StringUtils.hasText(basicAuthHeaderValue) && basicAuthHeaderValue.toLowerCase().startsWith("basic")) {
			// Authorization: Basic base64credentials
			String base64Credentials = basicAuthHeaderValue.substring("Basic".length()).trim();
			byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
			String credentials = new String(credDecoded, StandardCharsets.UTF_8);
			final String[] values = credentials.split(":", 2);

			if (values.length == 2) {
				String username = values[0];
				String password = values[1];
				if (appUserName.equals(username) && appPassword.equals(password)) {
					return true;
				}
			}
		}
		return false;

	}

}
