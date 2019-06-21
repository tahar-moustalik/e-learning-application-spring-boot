package com.springdev.mepdev.controllers;


import com.springdev.mepdev.JModels.SearchObj;
import com.springdev.mepdev.models.Categorie;
import com.springdev.mepdev.models.Cours;
import com.springdev.mepdev.models.Utilisateur;
import com.springdev.mepdev.persistance.CategorieRepository;
import com.springdev.mepdev.persistance.CoursRepository;
import com.springdev.mepdev.services.UtilisateurService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
public class LoginController {

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    @Qualifier("coursRepository")
    private CoursRepository coursRepository;

    @Autowired
    @Qualifier("categorieRepository")
    private CategorieRepository categorieRepository;



    @RequestMapping(value = "/",method = RequestMethod.GET)
    public ModelAndView index(){

        List<Cours> coursPopulaires = coursRepository.findTop5ByOrderByNbreReviewsDesc();
        ModelAndView modelAndView = new ModelAndView();
        try{
            SearchObj searchObj = new SearchObj();
            modelAndView.addObject("searchObj",searchObj);
        }
        catch (Exception e){
            e.getStackTrace();
        }

        List<Categorie> categories = categorieRepository.findAll();
        modelAndView.addObject("categories",categories);
        modelAndView.addObject("coursPopulaires",coursPopulaires);
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @RequestMapping(value="/login", method = RequestMethod.GET)
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("v2_login");
        return modelAndView;
    }


    @RequestMapping(value="/registration", method = RequestMethod.GET)
    public ModelAndView registration(Model model){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user",new Utilisateur());
        modelAndView.setViewName("v2_registration");
       return modelAndView;
    }


    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid @ModelAttribute(value = "user") Utilisateur user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        Utilisateur userExists = utilisateurService.findUserByEmail(user.getEmail());
        if (userExists != null) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "L'email saisis déja  existe ");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("v2_registration");
        } else {
            utilisateurService.saveUser(user);
            modelAndView.addObject("successMessage", "Inscription est effectué avec Success!.Connectez-Vous !");
            modelAndView.addObject("user", new Utilisateur());
            modelAndView.setViewName("v2_registration");

        }
        return modelAndView;
    }



    @RequestMapping(value="/user", method = RequestMethod.GET)
    public ModelAndView home(){
        List<Cours> coursPopulaires = coursRepository.findTop5ByOrderByNbreReviewsDesc();
        ModelAndView modelAndView = utilisateurService.infoUser();
        modelAndView.addObject("coursPopulaires",coursPopulaires);
        modelAndView.setViewName("user_home");
        return modelAndView;
    }


}
