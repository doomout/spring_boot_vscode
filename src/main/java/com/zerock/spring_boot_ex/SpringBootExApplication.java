package com.zerock.spring_boot_ex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing //AuditingEntityListener 를 활성
public class SpringBootExApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootExApplication.class, args);
	}

}
