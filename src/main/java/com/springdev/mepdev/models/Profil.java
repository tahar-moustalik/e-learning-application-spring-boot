package com.springdev.mepdev.models;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
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

    @OneToMany
    private List<Experience> experiences;
}
