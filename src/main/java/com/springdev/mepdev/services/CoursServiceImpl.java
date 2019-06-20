package com.springdev.mepdev.services;


import com.springdev.mepdev.JModels.ChapJson;
import com.springdev.mepdev.JModels.CoursJson;
import com.springdev.mepdev.models.*;
import com.springdev.mepdev.persistance.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("coursService")
public class CoursServiceImpl implements CoursService{

    @Autowired
    @Qualifier("userService")
    private UtilisateurService utilisateurService;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    @Qualifier("coursRepository")
    private CoursRepository coursRepository;

    @Autowired
    @Qualifier("chapitreRepository")
    private ChapitreRepository chapitreRepository;

    @Autowired
    @Qualifier("paraRepository")
    private ParagrapheRepository paragrapheRepository;

    @Autowired
    @Qualifier("categorieRepository")
    private CategorieRepository categorieRepository;


    @Override
    public Boolean save(CoursJson coursJson) {

        int numero_ordre = 0;

        Cours cours = new Cours();
        cours.setTitre(coursJson.getTitre());
        cours.setDescription(coursJson.getDescription());
        cours.setMessageBienvenue(coursJson.getMessageBienvenue());
        cours.setMessageFelicitation(coursJson.getMessageFelicitation());
        cours.setCategorie(categorieRepository.findById(Long.parseLong(coursJson.getCategorie())).get());
        cours.setRequis(coursJson.getRequis());
        cours.setButs(coursJson.getObjectifs());
        cours.setUtilisateursCibles(coursJson.getCibles());
        cours.setPrix(coursJson.getPrix());
        cours.setLibelleDevise(coursJson.getDevise());
        List<Chapitre> chapitreList = new ArrayList<>();
        for(ChapJson chap : coursJson.getChapitres()){
            Chapitre chapitre = new Chapitre();
            ++numero_ordre;
            chapitre.setNumeroOrdre(numero_ordre);
            Paragraphe paragraphe = new Paragraphe();
            paragraphe.setContenuTexte(chap.getParagraphe().getContenu());
            paragraphe.setType(chap.getParagraphe().getType());
            paragrapheRepository.save(paragraphe);
            chapitre.setParagraphe(paragraphe);
            chapitreRepository.save(chapitre);
            chapitreList.add(chapitre);

        }
        cours.setChapitres(chapitreList);
        coursRepository.save(cours);
        Utilisateur utilisateur = utilisateurService.getCurrentUser();
        utilisateur.addCoursCree(cours);
        utilisateurRepository.save(utilisateur);

        return true;
    }

    @Override
    public Cours get(Long id) {
        return null;
    }
}
