package com.springdev.mepdev.controllers;


import com.springdev.mepdev.models.Utilisateur;
import com.springdev.mepdev.services.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class LoginController {

    @Autowired
    private UtilisateurService utilisateurService;


    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String index(){
        return "index";
    }

    @RequestMapping(value="/login", method = RequestMethod.GET)
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }


    @RequestMapping(value="/registration", method = RequestMethod.GET)
    public String registration(Model model){
        model.addAttribute("user",new Utilisateur());
        return "registration";
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
            modelAndView.setViewName("registration");
        } else {
            utilisateurService.saveUser(user);
            modelAndView.addObject("successMessage", "Inscription est effectué avec Success!.Connectez-Vous !");
            modelAndView.addObject("user", new Utilisateur());
            modelAndView.setViewName("registration");

        }
        return modelAndView;
    }



    @RequestMapping(value="/user", method = RequestMethod.GET)
    public ModelAndView home(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Utilisateur user = utilisateurService.findUserByEmail(auth.getName());
        modelAndView.addObject("userId",user.getId());
        modelAndView.addObject("userEmail",  user.getEmail());
        modelAndView.addObject("fullName",user.getPrenom() + " " + user.getNom());

        modelAndView.setViewName("user_home");
        return modelAndView;
    }


}
