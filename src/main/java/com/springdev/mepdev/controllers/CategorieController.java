package com.springdev.mepdev.controllers;


import com.springdev.mepdev.models.Categorie;
import com.springdev.mepdev.persistance.CategorieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("/db/categories")
public class CategorieController {

    @Autowired
    @Qualifier("categorieRepository")
    private CategorieRepository categorieRepository;

    @GetMapping
    public ModelAndView getAllCateogries(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("categories",categorieRepository.findAll());
        modelAndView.setViewName("categories");
        return modelAndView;
    }


    @GetMapping("/add")
    public ModelAndView addCategorie(){

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("categorie",new Categorie());
        modelAndView.setViewName("show-categorie");
        return modelAndView;

    }

    @PostMapping("/add")
    public ModelAndView saveCategorie(@Valid @ModelAttribute(value = "categorie") Categorie categorie, BindingResult bindingResult){
        ModelAndView modelAndView = new ModelAndView();

        if(bindingResult.hasErrors()){
            modelAndView.setViewName("categorie");
        }
        else {
            categorieRepository.save(categorie);
            modelAndView.addObject("successMessage","Categorie Ajouté");
            modelAndView.addObject("typeCrud","Ajouter Categorie");
            modelAndView.addObject("categorie",new Categorie());
            modelAndView.setViewName("show-categorie");
        }


        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView showUpdateForm(@PathVariable("id") long id) {
        Categorie categorie = categorieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Id Categorie Invalid:" + id));

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("categorie",categorie);
        modelAndView.addObject("id",id);
        modelAndView.setViewName("update-categorie");
        return modelAndView;
    }

    @PostMapping("/update/{id}")
    public ModelAndView updateCategorie(@PathVariable("id") long id, @Valid @ModelAttribute(value = "categorie") Categorie categorie,
                             BindingResult result) {
        ModelAndView modelAndView = new ModelAndView();
        if (result.hasErrors()) {
            categorie.setId(id);
            modelAndView.addObject("categorie",categorie);
            modelAndView.setViewName("update-categorie");
            return modelAndView;
        }

        categorieRepository.save(categorie);
        modelAndView.addObject("successMessage","Catégorie mise à jour");
        modelAndView.addObject("categorie",categorie);
        modelAndView.setViewName("update-categorie");
        return modelAndView;
    }


    @GetMapping("/delete/{id}")
    public String deleteCategorie(@PathVariable("id") long id) {
        Categorie categorie = categorieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Id Categorie Invalid:" + id));
        categorieRepository.delete(categorie);
        return "redirect:/db/categories";
    }




}
