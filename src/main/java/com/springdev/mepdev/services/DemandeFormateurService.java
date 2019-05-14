package com.springdev.mepdev.services;

import com.springdev.mepdev.models.DemandeFormateur;

public interface DemandeFormateurService {

    public Boolean accepterDemande(Long id);
    public Boolean refuserDemande(Long id);
}
