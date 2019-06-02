package com.github.dperezcabrera.bank;

import com.github.dperezcabrera.bank.architecture.auth.entities.User;
import com.github.dperezcabrera.bank.architecture.auth.repositories.UserRepository;
import com.github.dperezcabrera.bank.architecture.auth.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import lombok.AllArgsConstructor;

@Slf4j
@Configuration
@AllArgsConstructor
public class Initializer implements CommandLineRunner {

    private UserService userService;

    private UserRepository userRepository;

    @Override
    @Transactional
    public void run(String... strings) throws Exception {
		userRepository.save(new User(null, "admin", "***********", 0));
        userRepository.save(new User(null, "alice", "1", 0));
        userRepository.save(new User(null, "bob", "2", 0));
        log.info("\"alice\" -> {}",userService.getUser("alice"));
    }
}
