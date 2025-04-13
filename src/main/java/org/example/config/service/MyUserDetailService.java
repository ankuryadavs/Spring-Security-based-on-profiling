package org.example.config.service;

import org.example.config.model.MyUserDetails;
import org.example.model.User;
import org.example.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user=userRepo.findByUserName(username);

        user.orElseThrow(()->new UsernameNotFoundException("User does not exist"));

        return user.map(MyUserDetails::new).get();
    }
}
