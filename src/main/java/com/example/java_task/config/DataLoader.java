package com.example.java_task.config;

import com.example.java_task.entities.Role;
import com.example.java_task.entities.User;
import com.example.java_task.entities.enums.RoleName;
import com.example.java_task.repositories.RoleRepository;
import com.example.java_task.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            Role userRole = roleRepository.save(new Role(RoleName.USER));
            Role moderatorRole = roleRepository.save(new Role(RoleName.MODERATOR));
            userRepository.save(new User(
                    "Nuriddin", "Inoyatov",
                    "admin", passwordEncoder.encode("admin"), Set.of(userRole, moderatorRole)));
        }
    }
}
