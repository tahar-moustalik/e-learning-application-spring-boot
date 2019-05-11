package com.springdev.mepdev.services;


import com.springdev.mepdev.models.Profil;

public interface ProfilService {

    public Profil showUserProfil(Long userId);

    public Profil saveProfil(Profil profil);

    public Profil getProfil(Long profilId);


}
