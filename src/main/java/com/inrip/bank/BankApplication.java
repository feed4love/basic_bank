package com.inrip.bank;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "com.inrip.bank" })
public class BankApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankApplication.class, args);
	}

	@PostConstruct
    public void init(){      
      TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
	
}
