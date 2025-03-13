package com.hrworkflow.usersservice;

import com.hrworkflow.usersservice.model.Role;
import com.hrworkflow.usersservice.model.User;
import com.hrworkflow.usersservice.repository.UserRepository;
import com.hrworkflow.usersservice.service.JobApplicationService;
import com.hrworkflow.usersservice.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class UsersServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsersServiceApplication.class, args);
	}

}
