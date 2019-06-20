package com.springdev.mepdev.persistance;


import com.springdev.mepdev.models.Paragraphe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("paraRepository")
public interface ParagrapheRepository extends JpaRepository<Paragraphe,Long> {
}
