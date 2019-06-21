package com.springdev.mepdev.persistance;

import com.springdev.mepdev.models.Cours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository("coursRepository")
public interface CoursRepository extends JpaRepository<Cours,Long>, JpaSpecificationExecutor<Cours> {

    List<Cours> findTop5ByOrderByNbreReviewsDesc();



}
