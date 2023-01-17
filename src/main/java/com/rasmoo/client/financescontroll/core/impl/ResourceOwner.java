package com.rasmoo.client.financescontroll.core.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.rasmoo.client.financescontroll.entity.User;

public class ResourceOwner implements UserDetails{

    private User usuario;

    public ResourceOwner(User usuario){
        this.usuario = usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(this.usuario.getRole()));

        return roles;
    }

    @Override
    public String getPassword() {
        
        return usuario.getCredencial().getSenha();
    }

    @Override
    public String getUsername() {
        
        return usuario.getCredencial().getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        
        return true;
    }

    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return true;
    }
    
}
