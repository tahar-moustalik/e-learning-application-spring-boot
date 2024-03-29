package com.springdev.mepdev.services;



import com.springdev.mepdev.models.Role;
import com.springdev.mepdev.models.Utilisateur;
import com.springdev.mepdev.persistance.RoleRepository;
import com.springdev.mepdev.persistance.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.HashSet;

@Service("userService")
public class UtilisateurService {

    private UtilisateurRepository utilisateurRepository;

    private RoleRepository roleRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    public UtilisateurService(UtilisateurRepository utilisateurRepository,
                              @Qualifier("roleRepository") RoleRepository roleRepository,
                            BCryptPasswordEncoder bCryptPasswordEncoder){
        this.utilisateurRepository = utilisateurRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    public Utilisateur findUserByEmail(String email){

        return utilisateurRepository.findByEmail(email);
    }


    public void saveUser(Utilisateur utilisateur){
        utilisateur.setPassword(bCryptPasswordEncoder.encode(utilisateur.getPassword()));
        utilisateur.setActive(1);
        Role userRole = roleRepository.findByRoleName("APPRENANT");
        utilisateur.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        utilisateurRepository.save(utilisateur);
    }

    public Utilisateur getCurrentUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Utilisateur utilisateur = this.findUserByEmail(auth.getName());
        return utilisateur;
    }

    public Utilisateur getUser(Long id){
        return utilisateurRepository.findById(id).get();
    }


    public ModelAndView infoUser(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Utilisateur user = this.findUserByEmail(auth.getName());
        if(user.getRoles().size() > 1){
            modelAndView.addObject("isFormateur","FORMATEUR");
        }
        else {
            modelAndView.addObject("notFormateur","NOT FORMATEUR");
        }
        modelAndView.addObject("userId",user.getId());
        modelAndView.addObject("userEmail",  user.getEmail());
        modelAndView.addObject("fullName", user.getPrenom() + " " + user.getNom());
        return modelAndView;
    }

}
