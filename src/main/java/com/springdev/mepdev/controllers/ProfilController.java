package com.springdev.mepdev.controllers;



import com.springdev.mepdev.models.Experience;
import com.springdev.mepdev.models.Profil;
import com.springdev.mepdev.persistance.ExperienceRepository;
import com.springdev.mepdev.services.ProfilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProfilController {
    private static final String AJAX_HEADER_NAME = "X-Requested-With";
    private static final String AJAX_HEADER_VALUE = "XMLHttpRequest";


    @Autowired
    private ProfilService profilService;

    @Autowired
    @Qualifier("experienceRepository")
    private ExperienceRepository experienceRepository;



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

    @PostMapping(path = {"/profil", "/profil/{id}"})
    public ModelAndView addExperience(Profil profil, HttpServletRequest request) {
        List<Experience> experienceList = new ArrayList<>(profil.getExperiences());
        System.out.println("BEFORE");
        for (Experience experience : experienceList) {
            System.out.println(experience);
        }
        Experience e = new Experience();
        experienceRepository.save(e);
        experienceList.add(e);
        profil.setExperiences(experienceList);
        profilService.saveProfil(profil);
        if (AJAX_HEADER_VALUE.equals(request.getHeader(AJAX_HEADER_NAME))) {
            // It is an Ajax request, render only #items fragment of the page.
            System.out.println("AFTER");
            for (Experience experience : experienceList) {
                System.out.println(experience);
            }
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("profil",profil);
            modelAndView.setViewName("profil");
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("profil",profil);
            modelAndView.setViewName("profil");
            return modelAndView;
        }
    }







}
