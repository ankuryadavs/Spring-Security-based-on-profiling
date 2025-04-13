package org.example.config.model;

import org.example.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class MyUserDetails implements UserDetails {

    private String userName;

    private String password;

    public MyUserDetails(User user)
    {
        this.userName=user.getUserName();
        this.password=user.getPassword();
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }
}
