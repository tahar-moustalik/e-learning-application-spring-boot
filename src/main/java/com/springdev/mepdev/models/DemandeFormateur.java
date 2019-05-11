package com.springdev.mepdev.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class DemandeFormateur {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private Utilisateur formateur;

    private Boolean etatDemande = true;

}
