package com.springdev.mepdev.controllers;

import com.springdev.mepdev.models.Role;
import com.springdev.mepdev.persistance.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;


@Controller
@RequestMapping("/db/roles")
public class RoleController {
    @Autowired
    @Qualifier("roleRepository")
    private RoleRepository roleRepository;

    @GetMapping
    public ModelAndView getAllRoles(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("roles", roleRepository.findAll());
        modelAndView.setViewName("roles");
        return modelAndView;
    }

    @GetMapping("/add")
    public ModelAndView addRole(){

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("role",new Role());
        modelAndView.setViewName("show-role");
        return modelAndView;

    }

    @PostMapping("/add")
    public ModelAndView saveRole(@Valid @ModelAttribute(value = "role") Role role, BindingResult bindingResult){
        ModelAndView modelAndView = new ModelAndView();

        if(bindingResult.hasErrors()){
            modelAndView.setViewName("show-role");
        }
        else {
            roleRepository.save(role);
            modelAndView.addObject("successMessage","Role Ajouté");
            modelAndView.addObject("typeCrud","Ajouter Role");
            modelAndView.addObject("role",new Role());
            modelAndView.setViewName("show-role");
        }


        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView showUpdateForm(@PathVariable("id") long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Id Role Invalid:" + id));

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("role",role);
        modelAndView.addObject("id",id);
        modelAndView.setViewName("update-role");
        return modelAndView;
    }

    @PostMapping("/update/{id}")
    public ModelAndView updateRole(@PathVariable("id") long id, @Valid @ModelAttribute(value = "role") Role role,
                                        BindingResult result) {
        ModelAndView modelAndView = new ModelAndView();
        if (result.hasErrors()) {
            role.setId(id);
            modelAndView.addObject("role",role);
            modelAndView.setViewName("update-role");
            return modelAndView;
        }

        roleRepository.save(role);
        modelAndView.addObject("successMessage","Role mis à jour");
        modelAndView.addObject("role",role);
        modelAndView.setViewName("update-role");
        return modelAndView;
    }


    @GetMapping("/delete/{id}")
    public String deleteRole(@PathVariable("id") long id) {
        Role categorie = roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Id Role Invalid:" + id));
        roleRepository.delete(categorie);
        return "redirect:/db/roles";
    }


}
