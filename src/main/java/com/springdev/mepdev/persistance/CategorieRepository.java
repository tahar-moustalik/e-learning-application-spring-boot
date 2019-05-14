package com.springdev.mepdev.persistance;

import com.springdev.mepdev.models.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("categorieRepository")
public interface CategorieRepository extends JpaRepository<Categorie,Long> {
}
