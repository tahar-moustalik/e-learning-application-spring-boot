package com.springdev.mepdev.controllers;

import com.springdev.mepdev.models.Utilisateur;
import com.springdev.mepdev.services.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.jws.WebParam;

@Controller
public class AdminController {

    @Autowired
    private UtilisateurService utilisateurService;


    @RequestMapping(value = "/admin",method = RequestMethod.GET)
    public String admin(){
        return "admin-login";
    }

    @RequestMapping(value = "/db",method = RequestMethod.GET)
    public ModelAndView adminHome()
    {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Utilisateur admin = utilisateurService.findUserByEmail(auth.getName());
        modelAndView.addObject("admiEmail",  admin.getEmail());
        modelAndView.addObject("fullName",admin.getPrenom() + " " + admin.getNom());

        modelAndView.setViewName("admin-home");
        return modelAndView;
    }


}
