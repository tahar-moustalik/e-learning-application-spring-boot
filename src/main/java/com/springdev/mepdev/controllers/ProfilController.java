package com.springdev.mepdev.controllers;



import com.springdev.mepdev.models.Experience;
import com.springdev.mepdev.models.Profil;
import com.springdev.mepdev.models.Utilisateur;
import com.springdev.mepdev.persistance.ExperienceRepository;
import com.springdev.mepdev.services.ProfilService;
import com.springdev.mepdev.services.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class ProfilController {

    @Autowired
    private ProfilService profilService;

    @Autowired
    @Qualifier("experienceRepository")
    private ExperienceRepository experienceRepository;

    @Autowired
    private UtilisateurService utilisateurService;

    @RequestMapping(value="/profil/{userId}", method = RequestMethod.GET)
    public ModelAndView profil( @PathVariable Long userId){
        ModelAndView modelAndView = new ModelAndView();
        Profil profil = profilService.showUserProfil(userId);
        modelAndView.addObject("profil",profil);
        modelAndView.setViewName("profil");
        return modelAndView;
    }



    @RequestMapping(value = "/saveprofil", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid @ModelAttribute(value = "profil") Profil profil, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("profil");
        } else {
            profilService.saveProfil(profil);
            modelAndView.addObject("successMessage", "Votre Profil a ete mis a jour avec Sucess");
            modelAndView.addObject("profil", profil);
            modelAndView.setViewName("profil");

        }
        return modelAndView;
    }

    @RequestMapping(value="/experience", method = RequestMethod.GET)
    public ModelAndView addExperience(){
        ModelAndView modelAndView = new ModelAndView();
        Experience e = new Experience();
        modelAndView.addObject("experience",e);
        modelAndView.setViewName("experience");
        return modelAndView;
    }

    @RequestMapping(value="/saveexperience", method = RequestMethod.POST)
    public ModelAndView saveExperience(@Valid @ModelAttribute(value = "experience") Experience experience, BindingResult bindingResult){
        ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("experience");
        } else {
            experienceRepository.save(experience);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Utilisateur utilisateur = utilisateurService.findUserByEmail(auth.getName());
            Profil profil = profilService.showUserProfil(utilisateur.getId());
            experienceRepository.save(experience);
            profil.addExperience(experience);
            profilService.saveProfil(profil);
            modelAndView.addObject("profil", profil);
            modelAndView.setViewName("profil");

        }
        return modelAndView;
    }

    @RequestMapping(value="/experience/{profilId}/delete/{expId}", method = RequestMethod.GET)
    public String deleteExperience(@PathVariable Long profilId,@PathVariable Long expId){
        Profil profil = profilService.getProfil(profilId);
        profil.removeExperience(expId);
        profilService.saveProfil(profil);
        experienceRepository.delete(experienceRepository.findById(expId).get());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Utilisateur utilisateur = utilisateurService.findUserByEmail(auth.getName());
        return "redirect:/profil/"+utilisateur.getId();
    }


    @InitBinder
    public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, null,  new CustomDateEditor(dateFormat, true));
    }









}
