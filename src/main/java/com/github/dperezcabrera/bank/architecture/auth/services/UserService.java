package com.github.dperezcabrera.bank.architecture.auth.services;

import com.github.dperezcabrera.bank.architecture.auth.dtos.SignUpDto;
import com.github.dperezcabrera.bank.architecture.auth.dtos.UserDto;
import com.github.dperezcabrera.bank.architecture.auth.repositories.UserRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@AllArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	private final UserMapper userMapper;

	public Optional<UserDto> getUser(@NonNull String username) {
		return userRepository.findByUsername(username).map(userMapper::map);
	}

	public void signUp(SignUpDto petitionDto) {

	}
}
