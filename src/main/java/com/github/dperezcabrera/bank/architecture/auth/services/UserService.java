package com.github.dperezcabrera.bank.architecture.auth.services;

import com.github.dperezcabrera.bank.architecture.auth.dtos.ChangePasswordDto;
import com.github.dperezcabrera.bank.architecture.auth.dtos.MovementDto;
import com.github.dperezcabrera.bank.architecture.auth.dtos.SignUpDto;
import com.github.dperezcabrera.bank.architecture.auth.dtos.TransferCodeDto;
import com.github.dperezcabrera.bank.architecture.auth.dtos.TransferDto;
import com.github.dperezcabrera.bank.architecture.auth.dtos.UserDto;
import com.github.dperezcabrera.bank.architecture.auth.dtos.UserPasswordDto;
import com.github.dperezcabrera.bank.architecture.auth.entities.Code;
import com.github.dperezcabrera.bank.architecture.auth.entities.Movement;
import com.github.dperezcabrera.bank.architecture.auth.entities.Team;
import com.github.dperezcabrera.bank.architecture.auth.entities.User;
import com.github.dperezcabrera.bank.architecture.auth.repositories.CodeRepository;
import com.github.dperezcabrera.bank.architecture.auth.repositories.MovementRepository;
import com.github.dperezcabrera.bank.architecture.auth.repositories.TeamRepository;
import com.github.dperezcabrera.bank.architecture.auth.repositories.UserRepository;
import com.github.dperezcabrera.bank.architecture.common.ForbiddenException;
import com.github.dperezcabrera.bank.architecture.common.FunctionalException;
import com.github.dperezcabrera.bank.architecture.common.MessageDto;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final CodeRepository codeRepository;
    private final MovementRepository movementRepository;
    private final UserMapper userMapper;
    private final MovementMapper movementMapper;

    @Transactional(readOnly = true)
    public List<String> getTeams() {
        return teamRepository.findAll().stream().map(Team::getName).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserDto> getAll() {
        return userRepository.findAll().stream().map(userMapper::map).sorted(Comparator.comparing(UserDto::getAmount).reversed()).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserPasswordDto> getPasswords() {
        return userRepository.findAll().stream().map(userMapper::mapPassword).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<UserDto> getById(long userId) {
        return userRepository.findById(userId).map(userMapper::map);
    }

    @Transactional(readOnly = true)
    public Optional<UserDto> getUser(@NonNull String username) {
        return userRepository.findByUsername(username).map(userMapper::map);
    }

    @Transactional(readOnly = true)
    public Optional<List<MovementDto>> getMovements(@NonNull String username) {
        return userRepository.findByUsername(username)
                .map(u -> movementRepository.findByUserId(u.getId()).stream()
                .map(movementMapper::map)
                .collect(Collectors.toList()));
    }

    @Transactional
    public MessageDto changePassword(String username, ChangePasswordDto changePasswordDto) {
        User user = userRepository.findByUsername(username).get();
        user.setPassword(changePasswordDto.getNewPassword());
        userRepository.save(user);
        return new MessageDto("El password ha sido cambiado correctamente");
    }

    User checkUser(Optional<User> opt, String msg) {
        return opt.orElseThrow(() -> new ForbiddenException(msg));
    }

    void checkNonNegativeAmount(User user) {
        if (user.getAmount() < 0) {
            throw new ForbiddenException("El usuario '" + user.getUsername() + "' no cuenta con suficientes fondos");
        }
    }
    
    @Transactional
    public MessageDto transfer(String username, TransferCodeDto transferCodeDto) {
        User user = checkUser(userRepository.findByUsername(username.toLowerCase()), "El usuario '" + username + "' no exsite");
        Code code = codeRepository.getOne(transferCodeDto.getCode());
        if (code.getUsername() != null){
            throw new ForbiddenException("El codigo ya ha sido utilizado");
        }
        user.setAmount(user.getAmount() + code.getAmount());
        code.setUsername(username);
        Movement m = new Movement(null, code.getAmount(), LocalDateTime.now(), null, user, "Transferencia desde codigo: '"+transferCodeDto.getCode());
        userRepository.save(user);
        codeRepository.save(code);
        movementRepository.save(m);
        return new MessageDto("La transferencia ha sido realizada");
    }

    @Transactional
    public MessageDto transfer(String originUsername, TransferDto transferDto) {
        User origin = checkUser(userRepository.findByUsername(originUsername.toLowerCase()), "El usuario '" + originUsername + "' no exsite");
        User target = checkUser(userRepository.findById(transferDto.getTargetId()), "La cc de destino: " + transferDto.getTargetId() + "' no exsite");
        if (origin.getId().equals(target.getId())) {
            throw new ForbiddenException("No se puede realizar una transferencia con el mismo origen y destino");
        }
        origin.setAmount(origin.getAmount() - transferDto.getAmount());
        target.setAmount(target.getAmount() + transferDto.getAmount());
        checkNonNegativeAmount(origin);
        checkNonNegativeAmount(target);
        Movement m = new Movement(null, transferDto.getAmount(), LocalDateTime.now(), origin, target, transferDto.getDescription());
        movementRepository.save(m);
        userRepository.save(origin);
        userRepository.save(target);
        return new MessageDto("La transferencia ha sido realizada");
    }

    private void checkEmptyString(String str, String msg) {
        if (StringUtils.isEmpty(str)) {
            throw new FunctionalException(msg);
        }
    }

    @Transactional
    public MessageDto signUp(SignUpDto signUpDto) {
        checkEmptyString(signUpDto.getUsername(), "El nombre de ususario no puede ser nulo.");
        checkEmptyString(signUpDto.getPassword(), "La contraseña no puede ser nula.");
        checkEmptyString(signUpDto.getTeam(), "El equipo no puede ser nulo.");
        String username = signUpDto.getUsername().toLowerCase();
        if (userRepository.findByUsername(username).isPresent()) {
            throw new FunctionalException("Ya existia un usuario con nombre: '" + signUpDto.getUsername() + "' dado de alta.");
        }
        Team team = teamRepository.getOne(signUpDto.getTeam().toLowerCase());
        User user = new User(null, signUpDto.getUsername().toLowerCase(), signUpDto.getPassword(), team, 0L);
        userRepository.save(user);
        return new MessageDto("El ususario '" + signUpDto.getUsername() + "' ha sido dado de alta correctamente");
    }
}
