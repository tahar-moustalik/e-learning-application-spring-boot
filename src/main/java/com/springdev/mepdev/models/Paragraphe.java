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
    @Column(length = 2000)
    private String contenuTexte;


    @NotEmpty
    private String type;
}
