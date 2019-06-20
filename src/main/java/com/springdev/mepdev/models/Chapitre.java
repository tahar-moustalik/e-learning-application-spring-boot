package com.springdev.mepdev.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.Entity;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;


@Entity
@Data
@EqualsAndHashCode(of = "id")
public class Chapitre {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @Positive
    private int numeroOrdre;

    @OneToOne
    private Paragraphe paragraphe;
}
