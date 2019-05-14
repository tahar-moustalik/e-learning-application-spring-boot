package com.springdev.mepdev.services;


import com.springdev.mepdev.models.Experience;
import com.springdev.mepdev.models.Profil;
import com.springdev.mepdev.models.Utilisateur;
import com.springdev.mepdev.persistance.ExperienceRepository;
import com.springdev.mepdev.persistance.ProfilRepository;
import com.springdev.mepdev.persistance.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("profilService")
public class ProfilServiceImpl implements ProfilService {




    @Autowired
    @Qualifier("profilRepository")
    private ProfilRepository profilRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    @Qualifier("experienceRepository")
    private ExperienceRepository experienceRepository;

    @Autowired
    private UtilisateurService utilisateurService;

    @Override
    public Profil getProfil(Long profilId) {
        return profilRepository.findById(profilId).get();
    }
    @Override
    public Profil showUserProfil(Long userId) {

       // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Utilisateur user = utilisateurRepository.findById(userId).get();

        if(user.getProfil() == null){
            return new Profil();
        }

        else {
            Profil profil = user.getProfil();

            return profil;
        }


    }

    @Override
    public Profil saveProfil(Profil profil) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Utilisateur user = utilisateurService.findUserByEmail(auth.getName());

        if(user.getProfil() == null) {
            user.setProfil(profil);
        }
        else {
            profil.setId(user.getProfil().getId());

        }
        List<Experience> experienceList = new ArrayList<>();
        for (Experience e:
                profil.getExperiences()) {
            experienceRepository.save(e);
            experienceList.add(e);
        }
        profil.setExperiences(experienceList);
        profilRepository.save(profil);
         utilisateurRepository.save(user);

        return profil;
    }
}
