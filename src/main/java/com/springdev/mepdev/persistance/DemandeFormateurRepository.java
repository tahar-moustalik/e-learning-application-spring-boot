package com.springdev.mepdev.persistance;

import com.springdev.mepdev.models.DemandeFormateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("demandeFormateurRepository")
public interface DemandeFormateurRepository extends JpaRepository<DemandeFormateur,Long> {

    List<DemandeFormateur> findDemandeFormateursByEtatDemandeIsFalse();
}
