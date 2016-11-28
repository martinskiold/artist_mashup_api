package com.martinskiold;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Multithreaded REST API that distributes information about artists.
 *
 * Created by martinskiold on 11/24/16.
 */
@SpringBootApplication
@EnableAsync
public class CygniBackendMartinskioldApplication {

	public static void main(String[] args) {
		SpringApplication.run(CygniBackendMartinskioldApplication.class, args);
	}
}
