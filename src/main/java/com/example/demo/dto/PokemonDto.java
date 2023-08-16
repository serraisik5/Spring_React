package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PokemonDto {
    private  Long id;
    private String name;
    private String type;
    private String imageUrl;
    private double height;
    private double weight;
    private String power;
    private String gender;

}