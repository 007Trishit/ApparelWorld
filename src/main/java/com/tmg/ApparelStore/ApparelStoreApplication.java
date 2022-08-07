package com.tmg.ApparelStore;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@ServletComponentScan
@EnableJpaAuditing
public class ApparelStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApparelStoreApplication.class, args);
	}

	@Bean
	BCryptPasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}



}
