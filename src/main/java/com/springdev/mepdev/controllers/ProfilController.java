package com.springdev.mepdev.controllers;



import com.springdev.mepdev.models.DemandeFormateur;
import com.springdev.mepdev.models.Experience;
import com.springdev.mepdev.models.Profil;
import com.springdev.mepdev.models.Utilisateur;
import com.springdev.mepdev.persistance.DemandeFormateurRepository;
import com.springdev.mepdev.persistance.ExperienceRepository;
import com.springdev.mepdev.services.ProfilService;
import com.springdev.mepdev.services.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;


@Controller
public class ProfilController {

    @Autowired
    private ProfilService profilService;

    @Autowired
    @Qualifier("experienceRepository")
    private ExperienceRepository experienceRepository;

    @Autowired
    @Qualifier("demandeFormateurRepository")
    private DemandeFormateurRepository demandeFormateurRepository;

    @Autowired
    private UtilisateurService utilisateurService;


    @GetMapping("/findUser")
    @ResponseBody
    public Utilisateur findOne(Long id){
     return  utilisateurService.getUser(id);
    }

    @RequestMapping(value="/profil", method = RequestMethod.GET)
    public ModelAndView profil(){
        Utilisateur utilisateur = utilisateurService.getCurrentUser();
        ModelAndView modelAndView = new ModelAndView();
        Profil profil = profilService.showUserProfil(utilisateur.getId());
        modelAndView.addObject("profil",profil);
        modelAndView.setViewName("profil");
        return modelAndView;
    }

    @RequestMapping(value="/devenir/formateur", method = RequestMethod.GET)
    public ModelAndView devenirFormateur(){
        Utilisateur utilisateur = utilisateurService.getCurrentUser();
        ModelAndView modelAndView = new ModelAndView();
        Profil profil = profilService.showUserProfil(utilisateur.getId());
        modelAndView.addObject("profil",profil);
        modelAndView.setViewName("profil_formateur");
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

    @RequestMapping(value = "/saveprofilformateur", method = RequestMethod.POST)
    public ModelAndView saveProfilFormateur(@Valid @ModelAttribute(value = "profil") Profil profil, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        Utilisateur utilisateur = utilisateurService.getCurrentUser();
        Profil currentUserProfil = utilisateur.getProfil();
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("profil_formateur");


        }
        else if(currentUserProfil.getExperiences() == null){
            modelAndView.addObject("minExperience","Ajouter au moin une expérience professionnel");
            modelAndView.setViewName("profil_formateur");
        }
        else {
            profilService.saveProfil(profil);
            modelAndView.addObject("successMessage", "Votre Demande pour Devenir Formateur est envoyé");
            modelAndView.addObject("profil", profil);
            modelAndView.setViewName("profil_formateur");
            DemandeFormateur demandeFormateur = new DemandeFormateur();
            demandeFormateur.setFormateur(utilisateur);
            demandeFormateurRepository.save(demandeFormateur);

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

    @RequestMapping(value="/experience/formateur", method = RequestMethod.GET)
    public ModelAndView addExperienceFormateur(){
        ModelAndView modelAndView = new ModelAndView();
        Experience e = new Experience();
        modelAndView.addObject("experience",e);
        modelAndView.setViewName("experience_formateur");
        return modelAndView;
    }

    @RequestMapping(value="/save/experience", method = RequestMethod.POST)
    public ModelAndView saveExperience(@Valid @ModelAttribute(value = "experience") Experience experience, BindingResult bindingResult){
        ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("experience");
        } else {
            experienceRepository.save(experience);
            Utilisateur utilisateur = utilisateurService.getCurrentUser();
            Profil profil = profilService.showUserProfil(utilisateur.getId());
            experienceRepository.save(experience);
            profil.addExperience(experience);
            profil.setExperiences(profil.getExperiences());
            profilService.saveProfil(profil);
            modelAndView.addObject("profil", profil);
            modelAndView.setViewName("profil");

        }
        return modelAndView;
    }

    @RequestMapping(value="/save/experience/formateur", method = RequestMethod.POST)
    public ModelAndView saveExperienceFormateur(@Valid @ModelAttribute(value = "experience") Experience experienceFormateur, BindingResult bindingResult){
        ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("experience");
        } else {
            experienceRepository.save(experienceFormateur);
            Utilisateur utilisateur = utilisateurService.getCurrentUser();
            Profil profilFormateur = profilService.showUserProfil(utilisateur.getId());
            Experience e=  experienceRepository.save(experienceFormateur);
            profilFormateur.addExperience(e);
            profilService.saveProfil(profilFormateur);
            modelAndView.addObject("profil", profilFormateur);
            modelAndView.setViewName("profil_formateur");

        }
        return modelAndView;
    }

    @RequestMapping(value="/experience/{profilId}/delete/{expId}", method = RequestMethod.GET)
    public String deleteExperience(@PathVariable Long profilId,@PathVariable Long expId){
        Profil profil = profilService.getProfil(profilId);
        profil.removeExperience(expId);
        profilService.saveProfil(profil);
        experienceRepository.delete(experienceRepository.findById(expId).get());
        return "redirect:/profil";
    }


    @InitBinder
    public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, null,  new CustomDateEditor(dateFormat, true));
    }









}
