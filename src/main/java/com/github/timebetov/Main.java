package com.github.timebetov;

import com.github.timebetov.service.ApplicationService;
import com.github.timebetov.service.InterviewService;
import com.github.timebetov.service.JobService;
import com.github.timebetov.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.github.timebetov.repository")
@EntityScan("com.github.timebetov.model")
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class Main {

    public static void main(String[] args) {

        var context = SpringApplication.run(Main.class, args);

        UserService userService = context.getBean(UserService.class);
        JobService jobService = context.getBean(JobService.class);
        ApplicationService applicationService = context.getBean(ApplicationService.class);
        InterviewService interviewService = context.getBean(InterviewService.class);

//        User candidate = User.builder()
//                .firstName("John")
//                .lastName("Doe")
//                .email("Y5OyD@example.com")
//                .password("passwordJohn")
//                .role(RoleStatus.CANDIDATE)
//                .position("JOB SEEKING")
//                .build();
//        User candidate2 = User.builder()
//                .firstName("Jane")
//                .lastName("Doe")
//                .email("jane@gmai.com")
//                .password("passwordJane")
//                .role(RoleStatus.CANDIDATE)
//                .position("JOB SEEKING")
//                .build();
//
//        User interviewer = User.builder()
//                .firstName("Mark")
//                .lastName("Rob")
//                .email("marker@example.com")
//                .password("passwordMark")
//                .role(RoleStatus.INTERVIEWER)
//                .position("Java Developer")
//                .build();
//
//        User hr = User.builder()
//                .firstName("Maria")
//                .lastName("Roman")
//                .email("maria@example.com")
//                .password("passwordMaria")
//                .role(RoleStatus.HR)
//                .position("HR")
//                .build();
//
//        var candi = userService.registerUser(candidate);
//        var candi1 = userService.registerUser(candidate2);
//
//        userService.registerUser(interviewer);
//        userService.registerUser(hr);
//
//        List<User> candidates = userService.getUsersByRole(RoleStatus.CANDIDATE);
//        System.out.println("-".repeat(150));
//        System.out.println("Всего кандидатов: " + candidates.size());
//        candidates.forEach(System.out::println);
//        System.out.println("-".repeat(150));
//
//        List<User> interviewers = userService.getUsersByRole(RoleStatus.INTERVIEWER);
//        System.out.println("-".repeat(150));
//        System.out.println("Всего интервьюерев: " + interviewers.size());
//        interviewers.forEach(System.out::println);
//        System.out.println("-".repeat(150));
//
//        List<User> hrs = userService.getUsersByRole(RoleStatus.HR);
//        System.out.println("-".repeat(150));
//        System.out.println("Всего HR: " + hrs.size());
//        hrs.forEach(System.out::println);
//        System.out.println("-".repeat(150));
//
//        List<User> allUsers = userService.getAllUsers();
//        System.out.println("-".repeat(150));
//        System.out.println("Всего пользователей: " + allUsers.size());
//        allUsers.forEach(System.out::println);
//        System.out.println("-".repeat(150));
//
//        List<User> usersByPosition = userService.getUsersByPosition("Java Developer");
//        System.out.println("-".repeat(150));
//        System.out.println("Всего пользователей с должностью Java Developer: " + usersByPosition.size());
//        usersByPosition.forEach(System.out::println);
//        System.out.println("-".repeat(150));
//
//
//        Job javaDev = Job.builder()
//                .title("Java Developer")
//                .description("Java Spring Developer")
//                .status(JobStatus.ACTIVE)
//                .createdBy(hr.getFirstName())
//                .build();
//
//        Job pytDev = Job.builder()
//                .title("Python Developer 2")
//                .description("Python Django Developer")
//                .status(JobStatus.ACTIVE)
//                .createdBy(hr.getFirstName())
//                .build();
//
//        Job hrManager = Job.builder()
//                .title("HR Manager")
//                .description("Human Resources Manager")
//                .status(JobStatus.ACTIVE)
//                .createdBy(hr.getFirstName())
//                .build();
//
//
//
//        var javDev =jobService.saveJob(javaDev);
//        var pyDev = jobService.saveJob(pytDev);
//        Job toClose = jobService.saveJob(hrManager);
//
//        // CLOSING the job
//        jobService.closeJob(toClose.getId());
//
//
//        List<Job> jobs = jobService.getAllJobs();
//        System.out.println("-".repeat(150));
//        System.out.println("Всего вакансий: " + jobs.size());
//        jobs.forEach(System.out::println);
//        System.out.println("-".repeat(150));
//
//        List<Job> activeJobs = jobService.getJobsByStatus(JobStatus.ACTIVE);
//        System.out.println("-".repeat(150));
//        System.out.println("Всего активных вакансий: " + activeJobs.size());
//        activeJobs.forEach(System.out::println);
//        System.out.println("-".repeat(150));
//
//        List<Job> closedJobs = jobService.getJobsByStatus(JobStatus.CLOSED);
//        System.out.println("-".repeat(150));
//        System.out.println("Всего закрытых вакансий: " + closedJobs.size());
//        closedJobs.forEach(System.out::println);
//        System.out.println("-".repeat(150));
//
//        applicationService.applyForJob(candi.getId(), javDev.getId());
//        applicationService.applyForJob(candi1.getId(), javDev.getId());
//
//        List<Application> applicationsForJavaDev = applicationService.getApplicationByJob(javDev.getId());
//        System.out.println("-".repeat(150));
//        System.out.println("Всего кандидатов на вакансию: " + javDev.getTitle() + " - " + applicationsForJavaDev.size());
//        applicationsForJavaDev.forEach(System.out::println);
//        System.out.println("-".repeat(150));
//
//        interviewService.scheduleInterview(applicationsForJavaDev.getFirst().getId(), interviewer.getId(), LocalDateTime.now().plusDays(7), "Prepare question about Java");
//
//        List<Interview> interviews = interviewService.findInterviewsByApplication(applicationsForJavaDev.getFirst().getId());
//        System.out.println("-".repeat(150));
//        System.out.println("Всего интервью на вакансию: " + javDev.getTitle() + " - " + interviews.size());
//        interviews.forEach(System.out::println);
//        System.out.println("-".repeat(150));
//
//        // Update interview status
//        interviewService.updateInterviewStatus(interviews.getFirst().getApplication().getId(), InterviewStatus.COMPLETED, "Good candidate", ApplicationStatus.HIRED);
//
//        List<Interview> completedInterviews = interviewService.findInterviewsByApplication(applicationsForJavaDev.getFirst().getId());
//        System.out.println("-".repeat(150));
//        System.out.println("Всего закрытых интервью на вакансию: " + javDev.getTitle() + " - " + completedInterviews.size());
//        completedInterviews.forEach(System.out::println);
//        System.out.println("-".repeat(150));
//
//        List<Application> applications = applicationService.getAllApplications();
//        System.out.println("-".repeat(150));
//        System.out.println("Всего заявок: " + applications.size());
//        applications.forEach(System.out::println);
//        System.out.println("-".repeat(150));
    }
}
