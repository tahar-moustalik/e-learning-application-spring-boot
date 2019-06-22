package com.springdev.mepdev.controllers;


import com.cloudinary.Cloudinary;
import com.cloudinary.Util;
import com.cloudinary.utils.ObjectUtils;
import com.springdev.mepdev.JModels.CoursSpecification;
import com.springdev.mepdev.JModels.FVideo;
import com.springdev.mepdev.JModels.SearchCriteria;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

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
    public ModelAndView showCours() {
        Utilisateur utilisateur = utilisateurService.getCurrentUser();
        List<Cours> coursList = utilisateur.getCoursCrees();
        ModelAndView modelAndView = utilisateurService.infoUser();
        modelAndView.addObject("coursList", coursList);
        modelAndView.setViewName("cours-ajoute");
        return modelAndView;

    }
        @GetMapping("/cours-achetes")
        public ModelAndView showCoursAchetes(){
            Utilisateur utilisateur = utilisateurService.getCurrentUser();
            List<Cours> coursList = utilisateur.getCoursAchetes();
            ModelAndView modelAndView = utilisateurService.infoUser();
            modelAndView.addObject("coursList",coursList);
            modelAndView.setViewName("cours-achete");
            return modelAndView;

        }


    @GetMapping("/add-cours")
    public ModelAndView addCours(){
        List<Categorie> categories = categorieRepository.findAll();

        ModelAndView modelAndView = utilisateurService.infoUser();
        modelAndView.addObject("categories",categories);
        modelAndView.setViewName("add-cours");

        return modelAndView;

    }


    @GetMapping("/search-cours")
    public ModelAndView searchCours(@RequestParam("motCle") String motCle, @RequestParam("categorieId") Long categorieId){
        CoursSpecification motCleSpec = new CoursSpecification(
                new SearchCriteria("titre",":",motCle)
        );
        List<Cours> searchedCours;
        if(categorieId != -1) {
            CoursSpecification categSpec = new CoursSpecification(
                    new SearchCriteria("categorie", "=", categorieId)
            );

           searchedCours = coursRepository.findAll(
                    Specification.where(motCleSpec).and(categSpec)
            );
        }
        else {
            searchedCours = coursRepository.findAll(
                    Specification.where(motCleSpec)
            );
        }
        ModelAndView modelAndView;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((auth instanceof AnonymousAuthenticationToken)) {
            modelAndView = new ModelAndView();
            modelAndView.setViewName("search-cours");
        }
        else{
             modelAndView = utilisateurService.infoUser();
            modelAndView.setViewName("search-cours-auth");
        }

        modelAndView.addObject("searchedCpurs",searchedCours);

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
        String payeeEmail = "";
       List<Utilisateur> utilisateurList = utilisateurRepository.findAll();
       for(Utilisateur user : utilisateurList){
           for(Cours cr : user.getCoursCrees()){
               if(cr.getId().equals(id)){
                   nomFormateur = user.getPrenom() + " " + user.getNom();
                   payeeEmail = user.getProfil().getEmailPaypal();
               }
           }
       }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       ModelAndView modelAndView;
        if ((auth instanceof AnonymousAuthenticationToken)) {
            modelAndView =new ModelAndView();
            modelAndView.setViewName("view-cours");
           isPayed = false;
        }
        else {
            modelAndView = utilisateurService.infoUser();
            modelAndView.setViewName("view-cours-auth");
            Utilisateur utilisateur = utilisateurService.getCurrentUser();
            Boolean exists = false;

            for(Cours cr : utilisateur.getCoursAchetes()){
                if(cr.getId().equals(id)){
                    exists = true;
                    break;
                }
            }
            if(exists){
               isPayed = true;
            }
            else{
                isPayed = false;
            }



        }


        modelAndView.addObject("nom",nomFormateur);
        modelAndView.addObject("payeeEmail",payeeEmail);
        modelAndView.addObject("test","Test Data");
        modelAndView.addObject("cours",cours);
        if(isPayed){
            modelAndView.addObject("isPayed",true);
        }
        else{
            modelAndView.addObject("isPayed",false);
        }

        return modelAndView;

    }

    @PostMapping("/rate-cours/{id}")
    public String rateCours(@RequestParam("id") Long id,@RequestParam("nbreStars") int nbreStars){
       try {
           Cours cours = coursRepository.getOne(id);
           int newStarsNumber = (cours.getNbreStars() + nbreStars) / 2;
           cours.setNbreStars(newStarsNumber);
           cours.setNbreReviews(cours.getNbreReviews() + 1);
           coursRepository.save(cours);
           return "success";
       }
       catch(Exception e){
           return "error";
       }



    }

}
