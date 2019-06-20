package com.springdev.mepdev.controllers;


import com.cloudinary.Cloudinary;
import com.cloudinary.Util;
import com.cloudinary.utils.ObjectUtils;
import com.springdev.mepdev.JModels.FVideo;
import com.springdev.mepdev.models.Categorie;
import com.springdev.mepdev.models.Cours;
import com.springdev.mepdev.models.Utilisateur;
import com.springdev.mepdev.persistance.CategorieRepository;
import com.springdev.mepdev.persistance.CoursRepository;
import com.springdev.mepdev.persistance.UtilisateurRepository;
import com.springdev.mepdev.services.CoursService;
import com.springdev.mepdev.services.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.jws.WebParam;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class CoursController {



    @Autowired
    @Qualifier("categorieRepository")
    private CategorieRepository categorieRepository;

    @Autowired
    @Qualifier("coursRepository")
    private CoursRepository coursRepository;

    @Autowired
    @Qualifier("userService")
    private UtilisateurService utilisateurService;

    @Autowired
    @Qualifier("userRepository")
    private UtilisateurRepository utilisateurRepository;


    private Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "dubphxozk",
            "api_key", "121227965943581",
            "api_secret", "tcvNTwPsmPtXzUcYF_8Np1zk7sE"));

    @GetMapping("/cours-ajoute")
    public ModelAndView showCours(){
        Utilisateur utilisateur = utilisateurService.getCurrentUser();
        List<Cours> coursList = utilisateur.getCoursCrees();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("coursList",coursList);
        modelAndView.setViewName("cours-ajoute");
        return modelAndView;

    }

    @GetMapping("/add-cours")
    public ModelAndView addCours(){
        List<Categorie> categories = categorieRepository.findAll();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("categories",categories);
        modelAndView.setViewName("add-cours");

        return modelAndView;

    }

    @PostMapping("/sendv")
    public ResponseEntity<?> multiUploadFileModel(@ModelAttribute FVideo model) {
        Map uploadResponse = null;
        try {



            uploadResponse=  cloudinary.uploader().upload(model.getVideo().getBytes(),
                    ObjectUtils.asMap("resource_type", "video"));
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(uploadResponse, HttpStatus.OK);
    }

    @GetMapping("/view-cours/{id}")
    public ModelAndView viewCours(@PathVariable("id") Long id){
        Cours cours = coursRepository.getOne(id);
        Boolean isPayed = false;
        String nomFormateur = "";
       List<Utilisateur> utilisateurList = utilisateurRepository.findAll();
       for(Utilisateur user : utilisateurList){
           for(Cours cr : user.getCoursCrees()){
               if(cr.getId().equals(id)){
                   nomFormateur = user.getPrenom() + " " + user.getNom();
               }
           }
       }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((auth instanceof AnonymousAuthenticationToken)) {
           isPayed = false;
        }
        else {
            Utilisateur utilisateur = utilisateurService.getCurrentUser();
            Boolean exists = true;
                    //utilisateurRepository.findByCoursAchetesContains(id);
            if(exists){
               isPayed = true;
            }
            else{
                isPayed = false;
            }



        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("nom",nomFormateur);
        modelAndView.addObject("test","Test Data");
        modelAndView.addObject("cours",cours);
        if(isPayed){
            modelAndView.addObject("isPayed",true);
        }
        else{
            modelAndView.addObject("isNotBought","Non Paye");
        }
        modelAndView.setViewName("view-cours");
        return modelAndView;

    }

}
