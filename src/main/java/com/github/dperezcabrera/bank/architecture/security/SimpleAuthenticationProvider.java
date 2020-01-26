package com.github.dperezcabrera.bank.architecture.security;

import com.github.dperezcabrera.bank.architecture.auth.dtos.Features;
import com.github.dperezcabrera.bank.architecture.auth.repositories.UserRepository;
import com.github.dperezcabrera.bank.architecture.auth.services.FeatureService;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.stream.IntStream;
import javax.sql.DataSource;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Component
@AllArgsConstructor
public class SimpleAuthenticationProvider implements AuthenticationProvider {

    private static final String INSECURE_CHARS = "()\"'-";

    private DataSource dataSource;
    private UserRepository userRepository;
    private Environment env;
    private FeatureService featureService;

    @Override
    @Transactional(readOnly = true)
    public Authentication authenticate(Authentication authentication) {
        String username = authentication.getName().toLowerCase();
        String password = authentication.getCredentials().toString();
        if (authenticate(username, password)) {
            return new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());
        } else {
            throw new BadCredentialsException("Usuario o contraseña incorrecta");
        }
    }

    private String getAdminPass() {
        String pwd = System.getenv("ADMIN_PASS");
        if (StringUtils.isEmpty(pwd)) {
            pwd = "1";
        }
        return pwd;
    }

    private boolean authenticate(@NonNull String username, @NonNull String password) {
        if ("admin".equalsIgnoreCase(username)) {
            return password.equals(getAdminPass());
        } else if (featureService.isActive(Features.SQL_INJECTION)) {
            return insecureAuth(username, password);
        } else {
            return secureAuth(username, password);
        }
    }

    private boolean insecureAuth(String username, String password) {
        try {
            checkInsecureCredentials(username, password);
            Connection c = dataSource.getConnection();
            Statement st = c.createStatement();
            return st.executeQuery("SELECT id FROM users WHERE username = '" + username + "' and password = '" + password + "';").next();
        } catch (SQLException e) {
            throw new BadCredentialsException("Usuario o contraseña incorrecta", e);
        }
    }

    void checkInsecureCredentials(@NonNull String username, @NonNull String password) {
        String s = username + password;
        boolean insecure = IntStream.range(0, s.length())
                .mapToObj(i -> s.substring(i, i + 1))
                .anyMatch(INSECURE_CHARS::contains);
        if (insecure) {
            featureService.useFeature(Features.SQL_INJECTION);
        }
    }

    private boolean secureAuth(String username, String password) {
        return userRepository.auth(username, password);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
