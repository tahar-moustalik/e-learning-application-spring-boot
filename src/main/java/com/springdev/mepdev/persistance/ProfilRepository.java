package com.springdev.mepdev.persistance;


import com.springdev.mepdev.models.Experience;
import com.springdev.mepdev.models.Profil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("profilRepository")
public interface ProfilRepository  extends JpaRepository<Profil,Long> {

   
}
