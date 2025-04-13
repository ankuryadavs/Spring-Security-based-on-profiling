package org.example.controller;

import org.example.model.User;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicationController {

    @Autowired
    private UserService userService;


    @GetMapping("/myController")
    public String getData()
    {
        return "Ankur";
    }


    @PostMapping("/save")
    public User persistData(@RequestBody User user)
    {
        return userService.savedata(user);
    }
}
