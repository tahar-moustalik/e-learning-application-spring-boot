package com.springdev.mepdev.JModels;

import lombok.Data;

import java.util.List;

@Data
public class CoursJson {

    private String titre;
    private String description;
    private String messageBienvenue;
    private String messageFelicitation;
    private String categorie;
    private List<String> requis;
    private List<String> cibles;
    private List<String> objectifs;
    private double prix;
    private String devise;
    private List<ChapJson> chapitres;



}

