package com.github.dperezcabrera.bank;

import com.github.dperezcabrera.bank.architecture.auth.dtos.SignUpDto;
import com.github.dperezcabrera.bank.architecture.auth.entities.Code;
import com.github.dperezcabrera.bank.architecture.auth.entities.User;
import com.github.dperezcabrera.bank.architecture.auth.repositories.CodeRepository;
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
    private CodeRepository codeRepository;

    @Override
    @Transactional
    public void run(String... strings) throws Exception {
        userRepository.save(new User(null, "admin", "***********", 0, false));
//        userRepository.save(new User(null, "alice", "1", 0));
//        userRepository.save(new User(null, "bob", "2", 0));
        codeRepository.save(new Code("1", null, 3000));
        codeRepository.save(new Code("2", null, 3000));
        codeRepository.save(new Code("3", null, 3000));
        codeRepository.save(new Code("4", null, 3000));
        codeRepository.save(new Code("5", null, 3000));
        
        
        userService.signUp(new SignUpDto("alice", "1", "1"));
        log.info("\"alice\" -> {}", userService.getUser("alice"));
    }
}
