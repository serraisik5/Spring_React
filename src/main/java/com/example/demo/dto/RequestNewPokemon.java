package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RequestNewPokemon {
    private Long id;

    @NotBlank(message = "no blank name")
    @Size(min= 3,max = 20)
    private String name;
    private String type;
    private String imageUrl;
    private double height;
    private double weight;
    private String power;
    private String gender;

}
