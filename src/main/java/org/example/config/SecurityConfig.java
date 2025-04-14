package org.example.config;

import org.example.config.service.MyUserDetailService;
import org.example.util.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
 class SecurityConfig {

    @Autowired
    private MyUserDetailService myUserDetailService;

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    @Profile("default")
    public  UserDetailsService userDetailsService()
    {
        UserDetails userDetail= User.withUsername("ankur")
                .password(encodedPassword().encode("ankur"))
                .build();

        return new InMemoryUserDetailsManager(userDetail);
    }

    @Bean
    @Profile("default")
    public AuthenticationManager authenticationManagerDefault(HttpSecurity http, UserDetailsService userDetailsService) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder
                .userDetailsService(userDetailsService)
                .passwordEncoder(encodedPassword());
        return builder.build();
    }

    @Bean
    @Profile({"dev","test"})
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
                .userDetailsService(myUserDetailService)
                .passwordEncoder(encodedPassword());

        return authenticationManagerBuilder.build();
    }

    @Bean
    public PasswordEncoder encodedPassword()
    {
        return NoOpPasswordEncoder.getInstance();
    }


    @Bean
    @Profile({"dev","default"})
    public SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
       return  httpSecurity.authorizeHttpRequests((authorizeHttpRequest)->
                authorizeHttpRequest
                        .requestMatchers(HttpMethod.POST, "/save").permitAll()
                        .requestMatchers(HttpMethod.POST, "/authenticate").permitAll()
                        .anyRequest()
                        .authenticated())
               .csrf(csrf->csrf.disable())
               .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    @Profile("test")
    public SecurityFilterChain configureTest(HttpSecurity httpSecurity) throws Exception {
        return  httpSecurity.authorizeHttpRequests((authorizeHttpRequest)->
                        authorizeHttpRequest
                                .requestMatchers(HttpMethod.POST, "/save").permitAll()
                                .requestMatchers(HttpMethod.POST, "/authenticate").permitAll()
                                .anyRequest()
                                .authenticated())
                .csrf(csrf->csrf.disable())
                .sessionManagement(sessionManagement->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
