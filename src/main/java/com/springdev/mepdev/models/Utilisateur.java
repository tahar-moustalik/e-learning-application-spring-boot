package com.springdev.mepdev.models;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "user")
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="user_id")
    private Long id;

    @Column(name = "prenom")
    @NotEmpty(message = "*Veuillez Saisir votre Prenom")
    private String prenom;

    @Column(name = "nom")
    @NotEmpty(message = "*Veuillez Saisir votre Nom")
    private String nom;


    @Column(name = "email")
    @Email(message = "* Veuillez fournir un email Valide")
    @NotEmpty(message = "*Veuillez Fournier un email")
    private String email;

    @Column(name = "password")

    @Length(min=8, message = "* Votre Mot de passe doit contenir minimum 8 caractères")
    @NotEmpty(message = "*Veuillez saisir un mot de passe")
    private String password;




    @Column(name = "active")
    private int active;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @OneToOne
    private Profil profil;

    @OneToMany
    private List<Cours> coursCrees;

    @ManyToMany
    private List<Cours> coursAchetes;

    public void addRole(Role role){
        roles.add(role);
    }

    public void addCoursCree(Cours cours){coursCrees.add(cours);}





}
