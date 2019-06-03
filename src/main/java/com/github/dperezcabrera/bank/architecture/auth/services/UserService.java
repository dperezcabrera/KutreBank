package com.github.dperezcabrera.bank.architecture.auth.services;

import com.github.dperezcabrera.bank.architecture.auth.dtos.SignUpDto;
import com.github.dperezcabrera.bank.architecture.auth.dtos.UserDto;
import com.github.dperezcabrera.bank.architecture.auth.entities.Movement;
import com.github.dperezcabrera.bank.architecture.auth.entities.User;
import com.github.dperezcabrera.bank.architecture.auth.repositories.CodeRepository;
import com.github.dperezcabrera.bank.architecture.auth.repositories.MovementRepository;
import com.github.dperezcabrera.bank.architecture.auth.repositories.UserRepository;
import com.github.dperezcabrera.bank.architecture.common.MessageDto;
import com.github.dperezcabrera.bank.architecture.common.MessageType;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CodeRepository codeRepository;
    private final MovementRepository movementRepository;

    private final UserMapper userMapper;

    public Optional<UserDto> getUser(@NonNull String username) {
        return userRepository.findByUsername(username).map(userMapper::map);
    }

    @Transactional
    public MessageDto signUp(SignUpDto signUpDto) {
        String username = signUpDto.getUsername().toLowerCase();
        if (userRepository.findByUsername(username).isPresent()) {
            return MessageDto.error("El ususario '" + signUpDto.getUsername() + "' ya estaba dado de alta.");
        } else {
            return codeRepository.findById(signUpDto.getCode()).map(code -> {
                if (code.getUsername() != null) {
                    return MessageDto.error("El codigo ya ha sido utilizado");
                } else {
                    User user = new User(null, signUpDto.getUsername().toLowerCase(), signUpDto.getPassword(), code.getAmount(), false);
                    user = userRepository.save(user);
                    code.setUsername(user.getUsername());
                    codeRepository.save(code);
                    User admin = userRepository.findByUsername("admin").get();
                    Movement movement = new Movement(null, code.getAmount(), LocalDateTime.now(), admin, user, "Deposito inicial");
                    movementRepository.save(movement);
                    return MessageDto.info("El ususario '" + signUpDto.getUsername() + "' ha sido dado de alta correctamente con un deposito de " + code.getAmount());
                }
            }).orElse(MessageDto.error("El codigo '" + signUpDto.getCode() + "' no existe"));
        }
    }
}
