package com.hrworkflow.usersservice;

import com.hrworkflow.usersservice.model.Role;
import com.hrworkflow.usersservice.model.User;
import com.hrworkflow.usersservice.repository.UserRepository;
import com.hrworkflow.usersservice.service.JobApplicationService;
import com.hrworkflow.usersservice.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class UsersServiceApplication {

	public static void main(String[] args) {
		var ctx = SpringApplication.run(UsersServiceApplication.class, args);

		UserService userService = ctx.getBean(UserService.class);
		JobApplicationService jobApplicationService = ctx.getBean(JobApplicationService.class);

//		User markInterviewer = User.builder()
//				.firstName("Mark")
//				.lastName("Brown")
//				.email("mark@email.com")
//				.password("passwordMark")
//				.position("Middle Java Developer")
//				.role(Role.INTERVIEWER)
//				.build();
//
//		User bobInterviewer = User.builder()
//				.firstName("Bob")
//				.lastName("Marlin")
//				.email("bob@email.com")
//				.password("passwordBob")
//				.position("Middle Golang Developer")
//				.role(Role.INTERVIEWER)
//				.build();

//		User aryaHr = User.builder()
//				.firstName("Arya")
//				.lastName("Stark")
//				.email("arya@mail.com")
//				.password("passwordArya")
//				.position("Junior Hiring Manager")
//				.role(Role.HR)
//				.build();
//
//		User robHr = User.builder()
//				.firstName("Rob")
//				.lastName("Stark")
//				.email("rob@mail.com")
//				.password("passwordRob")
//				.position("Senior Hiring Manager")
//				.role(Role.HR)
//				.build();
//
//		User sansaHr = User.builder()
//				.firstName("Sansa")
//				.lastName("Stark")
//				.email("sansa@mail.com")
//				.password("passwordSansa")
//				.position("Middle Hiring Manager")
//				.role(Role.HR)
//				.build();

//		User ned = User.builder()
//				.firstName("Ned")
//				.lastName("Stark")
//				.email("ned@gmail.com")
//				.password("passwordNed")
//				.position("Head of Company")
//				.role(Role.ADMIN)
//				.build();
//		User catelyn = User.builder()
//				.firstName("Catelyn")
//				.lastName("Stark")
//				.email("catelyn@gmail.com")
//				.password("passwordCatelyn")
//				.position("Head of Company")
//				.role(Role.ADMIN)
//				.build();

//		User candidate1 = User.builder()
//				.firstName("John")
//				.lastName("Doe")
//				.email("john.doe@gmail.com")
//				.password("passwordJohn")
//				.position("Java Backend")
//				.role(Role.CANDIDATE)
//				.build();

//		User candidate2 = User.builder()
//				.firstName("Micheal")
//				.lastName("Brown")
//				.email("micheal@gmail.com")
//				.password("passwordMicheal")
//				.position("Java Middle")
//				.role(Role.CANDIDATE)
//				.build();

//		User candidate3 = User.builder()
//				.firstName("Brad")
//				.lastName("Walter")
//				.email("bradwalter@gmail.com")
//				.password("passwordBrad")
//				.position("Java Intern")
//				.role(Role.CANDIDATE)
//				.build();

//		User candidate4 = User.builder()
//				.firstName("Billy")
//				.lastName("Morgan")
//				.email("billmor@gmail.com")
//				.password("millmor12")
//				.position("Java Intern")
//				.role(Role.CANDIDATE)
//				.build();
	}

}
