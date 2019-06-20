package com.springdev.mepdev.controllers;

import com.springdev.mepdev.JModels.CoursJson;
import com.springdev.mepdev.models.Cours;
import com.springdev.mepdev.services.CoursService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CoursRestController {
    @Autowired
    @Qualifier("coursService")
    private CoursService coursService;

    @PostMapping("/save-cours")
    public String saveCours(@RequestBody CoursJson coursJson){
        Boolean res =  coursService.save(coursJson);

        return res.toString();


    }
}
