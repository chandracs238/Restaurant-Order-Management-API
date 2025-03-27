package com.pcs.restaurantapi;

import jakarta.annotation.PostConstruct;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class RestaurantapiApplication {
	public static void main(String[] args) {

		SpringApplication.run(RestaurantapiApplication.class, args);

	}
}
