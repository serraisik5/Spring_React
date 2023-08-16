package com.example.demo.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private  Long id;
    private String username;
    private Boolean isAdmin;
    private String password;
}