package com.github.dperezcabrera.bank.architecture.auth.controllers;

import com.github.dperezcabrera.bank.architecture.auth.dtos.Features;
import com.github.dperezcabrera.bank.architecture.auth.dtos.MovementDto;
import com.github.dperezcabrera.bank.architecture.auth.dtos.TransferDto;
import com.github.dperezcabrera.bank.architecture.auth.services.FeatureService;
import com.github.dperezcabrera.bank.architecture.auth.services.UserService;
import com.github.dperezcabrera.bank.architecture.common.MessageDto;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.data.domain.AuditorAware;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/movements")
@AllArgsConstructor
public class MovementController {

	private UserService userService;
	private FeatureService featureService;
	private AuditorAware<String> auditor;

	@PostMapping("/transfer")
	public ResponseEntity<MessageDto> postTransfer(@RequestBody TransferDto transferDto){
		if (!featureService.isActive(Features.TRANSFER_BUG)){
			transferDto.setAmount(Math.abs(transferDto.getAmount()));
		}
		return userService.transfer(auditor.getCurrentAuditor().get(), transferDto).toResponse();
	}
	
	@GetMapping("/transfer")
	public ResponseEntity<MessageDto> getTransfer(TransferDto transferDto){
		if (featureService.isActive(Features.WRONG_HTTP_METHOD)){
			return postTransfer(transferDto);
		}
		return MessageDto.forbidden("Metodo denegado").toResponse();
	}
	
	@GetMapping
	public ResponseEntity<List<MovementDto>> getMovements() {
		return userService.getMovements(auditor.getCurrentAuditor().get())
				.map(this::map)
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	private ResponseEntity<List<MovementDto>> map(List<MovementDto> movements) {
		Stream<MovementDto> s = movements.stream();
		if (!featureService.isActive(Features.XSS)) {
			s = s.map(this::map);
		}
		return ResponseEntity.ok(s.collect(Collectors.toList()));
	}

	private MovementDto map(MovementDto m) {
		m.setDescription(StringEscapeUtils.escapeHtml4(m.getDescription()));
		return m;
	}
}
