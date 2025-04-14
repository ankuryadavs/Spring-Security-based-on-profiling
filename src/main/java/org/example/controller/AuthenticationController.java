package org.example.controller;

import org.example.config.service.MyUserDetailService;
import org.example.model.AuthenticateRequest;
import org.example.model.AuthenticationResponse;
import org.example.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailService myUserDetailService;

    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping("/authenticate")
    private ResponseEntity<AuthenticationResponse> generateJwtToken(@RequestBody AuthenticateRequest authenticateRequest)
    {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticateRequest.getUserName(), authenticateRequest.getPassword()));
        }
        catch (BadCredentialsException exception)
        {

        }

        UserDetails userDetails= myUserDetailService.loadUserByUsername(authenticateRequest.getUserName());

        final String token=jwtUtil.generateToken(authenticateRequest.getUserName());

        return ResponseEntity.ok(new AuthenticationResponse(token));
    }


}
