package com.springdev.mepdev.services;

import com.springdev.mepdev.JModels.CoursJson;
import com.springdev.mepdev.models.Cours;

public interface CoursService {

    public Boolean save(CoursJson coursJson);

    public Cours get(Long id);


}
