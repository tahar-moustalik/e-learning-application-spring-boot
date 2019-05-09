package com.springdev.mepdev.persistance;

import com.springdev.mepdev.models.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("userRepository")
public interface UtilisateurRepository  extends JpaRepository<Utilisateur,Long> {

    Utilisateur findByEmail(String email);

}
