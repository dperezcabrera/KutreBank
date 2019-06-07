package com.github.dperezcabrera.bank;

import com.github.dperezcabrera.bank.architecture.auth.entities.Code;
import com.github.dperezcabrera.bank.architecture.auth.entities.User;
import com.github.dperezcabrera.bank.architecture.auth.repositories.CodeRepository;
import com.github.dperezcabrera.bank.architecture.auth.repositories.UserRepository;
import com.github.dperezcabrera.bank.architecture.auth.services.FeatureService;
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
    private FeatureService featureService;
    private UserRepository userRepository;
    private CodeRepository codeRepository;

    @Override
    @Transactional
    public void run(String... strings) throws Exception {
        userRepository.save(new User(0L, "admin", "******", 0, false));
        for (int i = 1; i <= 27; i++) {
            codeRepository.save(new Code(Integer.toString(i), null, 10000));
        }
        for (int i = 28; i <= 50; i++) {
            codeRepository.save(new Code(Integer.toString(i), null, 0));
        }
        featureService.setActiveFeatures(featureService.getAllFeatures());
    }
}
