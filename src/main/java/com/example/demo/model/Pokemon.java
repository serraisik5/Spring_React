package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Pokemon extends BaseEntity {
    @Column(unique = true)
    private Long id;
    @Column(unique = true)
    private String name;
    private String type;
    private String imageUrl;
    private double height;
    private double weight;
    private String power;
    private String gender;
}
