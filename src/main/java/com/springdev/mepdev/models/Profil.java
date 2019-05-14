package com.springdev.mepdev.models;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Data
@Entity
public class Profil {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Length( max = 1500)
    private String biographie;

    private String urlSiteWeb;

    private String urlFacebook;

    private String urlYoutube;

    @Email
    private String emailPaypal;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "profil_experiences",joinColumns = @JoinColumn(name = "profil_id"))
    @OneToMany(cascade = CascadeType.ALL)
    private List<Experience> experiences = new ArrayList<>();


    public void addExperience(Experience e)
    {
       experiences.add(e);
    }

    public void removeExperience(Long id){
        Experience e = new Experience();
        e.setId(id);
       experiences.remove(e);
    }


}
