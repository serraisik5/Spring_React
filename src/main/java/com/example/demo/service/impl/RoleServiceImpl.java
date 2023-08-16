package com.example.demo.service.impl;

import com.example.demo.model.Role;
import com.example.demo.repository.RoleRepository;
import com.example.demo.service.RoleService;
import com.example.demo.utils.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    @Override
    public void checkAndCreateRoles(List<String> roleNames){
        if(CollectionUtils.isEmpty(roleNames)){
            return;
        }
        roleNames.stream().forEach(roleName -> {
            roleRepository.findByName(roleName).orElseGet(()->roleRepository.save(Role.builder().name(roleName).build()));
        });

    }

    @Override
    public Role findByName(String admin) {
        Objects.requireNonNull(admin,"need role name");
        return roleRepository.findByName(admin).orElseThrow(()->new RuntimeException("no role name"));
    }

}