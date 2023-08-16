package com.example.demo.security;

import com.example.demo.model.User;
import com.example.demo.utils.CollectionUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class UserAuthDetails implements UserDetails {
    private User user;

    public UserAuthDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(Objects.nonNull(user)&& CollectionUtils.isNotEmpty(user.getRoles())){
            return user.getRoles().stream().map(t -> new SimpleGrantedAuthority(t.getName())).toList();
        }
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.user.getEnabled();
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.user.getEnabled();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.user.getEnabled();
    }

    @Override
    public boolean isEnabled() {
        return this.user.getEnabled();
    }
}

