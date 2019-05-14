package com.springdev.mepdev.services;


import com.springdev.mepdev.models.DemandeFormateur;
import com.springdev.mepdev.models.Role;
import com.springdev.mepdev.models.Utilisateur;
import com.springdev.mepdev.persistance.DemandeFormateurRepository;
import com.springdev.mepdev.persistance.RoleRepository;
import com.springdev.mepdev.persistance.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("demandeFormateurService")
public class DemandeFormateurServiceImpl implements DemandeFormateurService {

    @Autowired
    @Qualifier("demandeFormateurRepository")
    private DemandeFormateurRepository demandeFormateurRepository;

    @Autowired
    @Qualifier("roleRepository")
    private RoleRepository roleRepository;

    @Autowired
    @Qualifier("userRepository")
    private UtilisateurRepository utilisateurRepository;

    @Override
    public Boolean accepterDemande(Long id) {
        DemandeFormateur demandeFormateur = demandeFormateurRepository.findById(id).get();
        demandeFormateur.setEtatDemande(true);
        demandeFormateur.setDecisionDemande(true);
        Utilisateur utilisateur = demandeFormateur.getFormateur();
        Role role = roleRepository.findByRole("FORMATEUR");
        utilisateur.addRole(role);
        utilisateurRepository.save(utilisateur);
        demandeFormateurRepository.save(demandeFormateur);
        return true;
    }

    @Override
    public Boolean refuserDemande(Long id) {
        DemandeFormateur demandeFormateur = demandeFormateurRepository.findById(id).get();
        demandeFormateur.setEtatDemande(true);
        demandeFormateur.setDecisionDemande(false);
        demandeFormateurRepository.save(demandeFormateur);
        return true;
    }
}
