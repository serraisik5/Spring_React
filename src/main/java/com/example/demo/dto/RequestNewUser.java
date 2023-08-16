package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RequestNewUser {
    private Long id;

    @NotBlank(message = "no blank username")
    @Size(min= 3,max = 20)
    private String username;

    @NotBlank(message = "no blank password")
    private String password;
    private Boolean isAdmin;
}
