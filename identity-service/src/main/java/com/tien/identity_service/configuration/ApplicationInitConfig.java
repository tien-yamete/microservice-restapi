package com.tien.identity_service.configuration;

import java.util.HashSet;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.ControllerAdvice;

import com.tien.identity_service.entity.User;
import com.tien.identity_service.enums.Role;
import com.tien.identity_service.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    // Chạy sau khi ứng dụng Spring Boot khởi động xong
    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            // Kiểm tra xem user "admin" đã tồn tại chưa
            if (userRepository.findByUsername("admin").isEmpty()) {
                var roles = new HashSet<String>();
                roles.add(Role.ADMIN.name());
                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        // .roles(roles)
                        .build();

                userRepository.save(user);
                log.warn("admin user has been created with default password:admin, please change it");
            }
        };
    }
}
