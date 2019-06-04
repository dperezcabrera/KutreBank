package com.github.dperezcabrera.bank.architecture.auth.services;

import com.github.dperezcabrera.bank.architecture.auth.dtos.MovementDto;
import com.github.dperezcabrera.bank.architecture.auth.dtos.SignUpDto;
import com.github.dperezcabrera.bank.architecture.auth.dtos.TransferDto;
import com.github.dperezcabrera.bank.architecture.auth.dtos.UserDto;
import com.github.dperezcabrera.bank.architecture.auth.dtos.UserPasswordDto;
import com.github.dperezcabrera.bank.architecture.auth.entities.Movement;
import com.github.dperezcabrera.bank.architecture.auth.entities.User;
import com.github.dperezcabrera.bank.architecture.auth.repositories.CodeRepository;
import com.github.dperezcabrera.bank.architecture.auth.repositories.MovementRepository;
import com.github.dperezcabrera.bank.architecture.auth.repositories.UserRepository;
import com.github.dperezcabrera.bank.architecture.common.MessageDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
	private final MovementMapper movementMapper;

	@Transactional(readOnly = true)
	public List<UserDto> getAll() {
		return userRepository.findAll().stream().map(userMapper::map).collect(Collectors.toList());
	}
	
	@Transactional(readOnly = true)
	public List<UserPasswordDto> getPasswords() {
		return userRepository.findAll().stream().map(userMapper::mapPassword).collect(Collectors.toList());
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
	public MessageDto transfer(String originUsername, TransferDto transferDto){
		User origin = userRepository.findByUsername(originUsername).get();
		if (origin.isLocked()) {
			return MessageDto.forbidden("El usuario '"+origin.getUsername()+"' esta bloqueado");
		}
		if (origin.getAmount() - transferDto.getAmount() < 0){
			return MessageDto.forbidden("El usuario '"+origin.getUsername()+"' no cuenta con suficientes fondos");
		}
		User target = userRepository.getOne(transferDto.getTargetId());
		if (target.isLocked()) {
			return MessageDto.forbidden("El usuario '"+target.getUsername()+"' esta bloqueado");
		}
		if (target.getAmount() + transferDto.getAmount() < 0){
			return MessageDto.forbidden("El usuario '"+target.getUsername()+"' esta en estado inconsistente");
		}
		origin.setAmount(origin.getAmount()-transferDto.getAmount());
		target.setAmount(target.getAmount()+transferDto.getAmount());
		Movement m = new Movement(null, transferDto.getAmount(), LocalDateTime.now(), origin, target, transferDto.getDescription());
		movementRepository.save(m);
		userRepository.save(origin);
		userRepository.save(target);
		return MessageDto.info("La transferencia ha sido realizada");
	}
	
	@Transactional
	public Optional<UserDto> setUserLock(long userId, boolean lock) {
		return userRepository.findById(userId).map(u -> {
			u.setLocked(lock);
			userRepository.save(u);
			return userMapper.map(u);
		});
	}

	@Transactional
	public MessageDto signUp(SignUpDto signUpDto) {
		// TODO: Validation
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
