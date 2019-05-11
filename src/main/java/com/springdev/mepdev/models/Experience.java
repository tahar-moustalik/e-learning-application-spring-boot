package com.springdev.mepdev.models;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(of = "id")
@Entity
public class Experience {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty
    @NotEmpty(message = "*Veuillez Saisir le titre de Poste")
    private String titrePoste;

    @NotEmpty
    @NotEmpty(message = "*Veuillez Saisir Le nom de l'ogranisation ")
    private String nomOrganisation;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateDebut;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFin;

}
