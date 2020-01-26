package com.github.dperezcabrera.bank;

import com.github.dperezcabrera.bank.architecture.auth.entities.Code;
import com.github.dperezcabrera.bank.architecture.auth.entities.Team;
import com.github.dperezcabrera.bank.architecture.auth.entities.User;
import com.github.dperezcabrera.bank.architecture.auth.repositories.CodeRepository;
import com.github.dperezcabrera.bank.architecture.auth.repositories.TeamRepository;
import com.github.dperezcabrera.bank.architecture.auth.repositories.UserRepository;
import com.github.dperezcabrera.bank.architecture.auth.services.FeatureService;
import com.github.dperezcabrera.bank.architecture.auth.services.UserService;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import lombok.AllArgsConstructor;

@Slf4j
@Configuration
@AllArgsConstructor
public class Initializer implements CommandLineRunner {

    private static final List<String> TEAMS_NAME = Arrays.asList("team-1", "team-2", "team-3", "team-4", "team-5");

    private UserService userService;
    private FeatureService featureService;
    private UserRepository userRepository;
    private TeamRepository teamRepository;
    private CodeRepository codeRepository;

    @Override
    @Transactional
    public void run(String... strings) throws Exception {
        List<Team> teams = TEAMS_NAME.stream().map(Team::new).collect(Collectors.toList());
        teamRepository.saveAll(teams);
        userRepository.save(new User(0L, "admin", "******", null, 0));
        List<Code> codes = Arrays.asList(new Code("1001", null, 1000));
        codeRepository.saveAll(codes);
    }
}
