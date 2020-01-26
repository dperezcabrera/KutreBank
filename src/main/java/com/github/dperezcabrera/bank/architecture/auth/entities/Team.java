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
@Table(name = "teams")
@NoArgsConstructor
@AllArgsConstructor
public class Team implements Serializable {

    @Id
    @Column(length = 20)
    private String name;
}
