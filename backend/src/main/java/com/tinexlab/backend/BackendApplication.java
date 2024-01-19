package com.tinexlab.backend;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication
@SecurityScheme(
		name = "bearerAuth",
		scheme = "bearer",
		bearerFormat = "JWT",
		type = SecuritySchemeType.HTTP,
		in = SecuritySchemeIn.HEADER
)
@SuppressWarnings({"unused"})
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner createPasswordsCommand(){
		return args -> {
			System.out.println("Prueba de control de sesiones");
			System.out.println("*****************************");
			System.out.println("Aseguramiento de claves - ini");
			/*System.out.println("Clave u$uari0CRM (usuario): " + passwordEncoder.encode("u$uari0CRM"));
			System.out.println("Clave Tr20010878 (admin): " + passwordEncoder.encode("Tr20010878"));*/
			System.out.println("Aeguramiento de claves - fin");
			System.out.println("Hola mundo! :)");
		};
	}

}
