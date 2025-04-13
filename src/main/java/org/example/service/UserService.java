package org.example.service;

import org.example.model.User;
import org.example.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public User savedata(User user)
    {
        return userRepo.save(user);
    }
}
