package com.github.dperezcabrera.bank.architecture.auth.repositories;

import com.github.dperezcabrera.bank.architecture.auth.entities.Movement;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface MovementRepository extends JpaRepository<Movement, Long>  {
    
	@Query("select m from Movement m where m.origin.id = :userId OR m.target.id = :userId")
    List<Movement> findByUserId(@Param("userId") long userId);
}
