package com.github.dperezcabrera.bank.architecture.auth.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "codes")
@NoArgsConstructor
@AllArgsConstructor
public class Code implements Serializable {

    @Id
    @Column(length = 20, nullable = false, unique = true)
    private String code;

    @Column(length = 20)
    private String username;
    
    @Column(nullable = false)
    private long amount;
}
