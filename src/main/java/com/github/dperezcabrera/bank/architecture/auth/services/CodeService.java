package com.github.dperezcabrera.bank.architecture.auth.services;

import com.github.dperezcabrera.bank.architecture.auth.repositories.CodeRepository;
import com.github.dperezcabrera.bank.architecture.auth.dtos.CodeDto;
import com.github.dperezcabrera.bank.architecture.auth.entities.Code;
import com.github.dperezcabrera.bank.architecture.common.FunctionalException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class CodeService {

    private CodeRepository codeRepository;
    private CodeMapper codeMapper;

    @Transactional(readOnly = true)
    public List<CodeDto> getAll() {
        return codeRepository.findAll().stream().map(codeMapper::map).collect(Collectors.toList());
    }

    @Transactional
    public void createCode(String code, long amount) {
        if (!codeRepository.findById(code).isPresent()) {
            codeRepository.save(new Code(code, null, amount));
        } else {
            throw new FunctionalException("El codigo '" + code + "' ya existe");
        }
    }
}
