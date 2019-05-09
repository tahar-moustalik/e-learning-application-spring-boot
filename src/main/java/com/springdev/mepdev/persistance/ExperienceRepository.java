package com.springdev.mepdev.persistance;

import com.springdev.mepdev.models.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("experienceRepository")
public interface ExperienceRepository  extends JpaRepository<Experience,Long> {


}
