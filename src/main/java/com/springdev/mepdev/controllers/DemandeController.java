package com.springdev.mepdev.controllers;


import com.springdev.mepdev.persistance.DemandeFormateurRepository;
import com.springdev.mepdev.services.DemandeFormateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/db/demandes")
public class DemandeController {

    @Autowired
    @Qualifier("demandeFormateurService")
    private DemandeFormateurService demandeService;

    @Autowired
    @Qualifier("demandeFormateurRepository")
    private DemandeFormateurRepository demandeFormateurRepository;

    @GetMapping
    public ModelAndView getAllDemandes(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("demandes",demandeFormateurRepository.findDemandeFormateursByEtatDemandeIsFalse());
        modelAndView.setViewName("demandes");
        return modelAndView;
    }

  @GetMapping("/accepter-demande/{id}")
    public String accepterDemande(@PathVariable Long id){
        demandeService.accepterDemande(id);
        return "redirect:/db/demandes";
  }

    @GetMapping("/refuser-demande/{id}")
    public String refuseDemande(@PathVariable Long id){
        demandeService.refuserDemande(id);
        return "redirect:/db/demandes";
    }




}
