package com.springdev.mepdev.models;


import com.springdev.mepdev.util.MediaType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@Entity
@EqualsAndHashCode(of="id")
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty
    private String sourceMedia;

    @NotEmpty
    @Positive
    @Size(max = 100)
    private int percCompletion;

    @NotEmpty
    private MediaType mediaType;
}
