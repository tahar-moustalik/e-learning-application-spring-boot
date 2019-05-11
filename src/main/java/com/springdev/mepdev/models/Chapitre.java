package com.springdev.mepdev.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.Entity;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(of = "id")
public class Chapitre {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty
    @Size(min= 200)
    private String introduction;

    @NotEmpty
    @Positive
    private int numeroOrdre;

    @OneToMany
    private List<Paragraphe> paragraphes ;
}
