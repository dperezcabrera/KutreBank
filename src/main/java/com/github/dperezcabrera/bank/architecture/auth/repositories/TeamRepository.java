package com.github.dperezcabrera.bank.architecture.auth.repositories;

import com.github.dperezcabrera.bank.architecture.auth.entities.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, String> {
    
}
