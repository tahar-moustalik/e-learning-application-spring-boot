package com.springdev.mepdev.persistance;


import com.springdev.mepdev.models.Chapitre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("chapitreRepository")
public interface ChapitreRepository extends JpaRepository<Chapitre,Long> {
}
