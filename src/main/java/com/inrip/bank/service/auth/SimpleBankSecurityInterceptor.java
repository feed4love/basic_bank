package com.inrip.bank.service.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 */
@Component
public class SimpleBankSecurityInterceptor extends HandlerInterceptorAdapter {

	private final Logger mLogger = LogManager.getLogger(SimpleBankSecurityInterceptor.class);

	private static final String AUTH_HEADER_PARAMETER_AUTHERIZATION = "authorization";

	@Value("${bank.basic.auth.username}")
	private String userName;

	@Value("${bank.basic.auth.password}")
	private String password;

	@Autowired
	private SimpleBankAuthService authService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		Boolean isValidBasicAuthRequest = false;

		mLogger.info("Init - preHandle [" 
		        + request + "]" 
				+ "[" + request.getMethod() + "]"
				+ request.getRequestURI());

		try {

			String basicAuthHeaderValue = request.getHeader(AUTH_HEADER_PARAMETER_AUTHERIZATION);

			isValidBasicAuthRequest = authService.validateBasicAuthentication(userName, password, basicAuthHeaderValue);

			if (!isValidBasicAuthRequest) {								
				response.setStatus(HttpStatus.UNAUTHORIZED.value());
			}

		} catch (Exception e) {
			mLogger.error("Error occured while authenticating request : " + e.getMessage());
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
		}

		return isValidBasicAuthRequest;

	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		mLogger.info("Init - postHandle" + request.getRequestURI());

	}

}
