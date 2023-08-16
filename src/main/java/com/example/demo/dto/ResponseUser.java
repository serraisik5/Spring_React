package com.example.demo.dto;

import lombok.Data;

@Data
public class ResponseUser {
    private Long id;
    private String username;
    private Boolean isAdmin;
}