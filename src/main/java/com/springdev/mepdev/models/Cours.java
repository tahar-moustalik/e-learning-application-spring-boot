package com.springdev.mepdev.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(of = "id")
public class Cours {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty
    private String titre;

    @Positive
    private double prix;

    //@Size(min = 150)
    @NotEmpty
    @Column(length = 1000)
    private String description;

    //@NotEmpty
    private String imageCover;

    private String videoPromo;

    @NotEmpty
    @Column(length = 1000)
    private String messageBienvenue;

    @NotEmpty
    @Column(length = 1000)
    private String messageFelicitation;

    //@NotEmpty
    private String libelleDevise;

    @ElementCollection
    @CollectionTable(name = "cours_utilisateurs_cibles")
    private List<String> utilisateursCibles;

    @ElementCollection
    @CollectionTable(name = "requis_cours")
    private List<String> requis;

    @ElementCollection
    @CollectionTable(name = "buts_cours")
    private List<String> buts;

    @ManyToOne
    @JoinColumn
    private Categorie categorie;

    @OneToMany
    private List<Chapitre> chapitres;

    @Column(nullable = true)
    private int nbreStars = 0;

    @Column(nullable = true)
    private int nbreReviews = 0;
}
