package io.confluent.developer.springccloud;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Configuration
public class Config {

	@Bean
	public Consumer<String> myConsumer1(List<String> myList1) {
		return myList1::add;
	}

	@Bean
	public Consumer<String> myConsumer2(List<String> myList2) {
		return myList2::add;
	}

	@Bean
	public List<String> myList1() {
		return new ArrayList<>();
	}

	@Bean
	public List<String> myList2() {
		return new ArrayList<>();
	}
}
