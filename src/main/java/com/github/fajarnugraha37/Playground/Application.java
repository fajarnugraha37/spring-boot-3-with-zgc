package com.github.fajarnugraha37.Playground;

import com.github.fajarnugraha37.Playground.client.DummyJsonRemote;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static com.github.fajarnugraha37.Playground.util.RetrofitUtil.invokeCall;

@Slf4j
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(DummyJsonRemote remote) {
		return args -> {
			var response = invokeCall(remote.getProductCategories());
			log.info("response: {}", response);
		};
	}
}
