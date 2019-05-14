package com.springdev.mepdev.models;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;


@Entity
@Data
@EqualsAndHashCode(of = "id")
public class Categorie {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty
    @NotEmpty(message = "*Veuillez saisir un libelle pour la catégorie")
    private String libelleCategorie;
}
