package com.springdev.mepdev.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(of = "id")
public class Paragraphe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String contenuTexte;

    @NotEmpty
    @Positive
    private int numeroOrdre;

    @OneToMany
    private List<Media> medias;
}
