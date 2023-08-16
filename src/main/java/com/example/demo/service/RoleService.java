package com.example.demo.service;

import com.example.demo.model.Role;

import java.util.List;

public interface RoleService {
    void checkAndCreateRoles(List<String> roleAdmin);

    Role findByName(String admin);
}
