package com.inrip.bank.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.inrip.bank.service.auth.SimpleBankSecurityInterceptor;

/*import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;*/

/**
 * @author Enrique AC
 *
 */
//@Configuration
public class SpringMVCConfig implements WebMvcConfigurer  {

	/* 	
	
		Commented as far is using JWT filters

	@Autowired
	SimpleBankSecurityInterceptor transactionSecurityInterceptor;
	@Override
	public void addInterceptors(final InterceptorRegistry registry) {
		registry.addInterceptor(transactionSecurityInterceptor);
	} */

}
