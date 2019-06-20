package com.springdev.mepdev.models;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Set;


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

    @OneToMany(mappedBy = "categorie", cascade = CascadeType.ALL)
    private List<Cours> cours;
}
