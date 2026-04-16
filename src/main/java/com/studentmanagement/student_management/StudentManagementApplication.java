package com.studentmanagement.student_management;

import com.studentmanagement.student_management.entity.Role;
import com.studentmanagement.student_management.entity.User;
import com.studentmanagement.student_management.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class StudentManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentManagementApplication.class, args);
	}

	@Bean
	public CommandLineRunner dataSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			if (!userRepository.existsByUsername("admin")) {
				User admin = new User();
				admin.setUsername("admin");
				admin.setPassword(passwordEncoder.encode("12345678"));
				admin.setFullName("Administrator");
				admin.setRole(Role.ROLE_ADMIN);
				userRepository.save(admin);
				System.out.println("====== Admin user created automatically with username 'admin' and password '12345678' ======");
			}
		};
	}
}
