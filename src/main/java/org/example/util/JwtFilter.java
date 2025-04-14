package org.example.util;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.config.service.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MyUserDetailService userDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
          final String authorizationHeader=request.getHeader("Authorization");


          String userName=null;
          String jwt=null;

          if(authorizationHeader!=null && authorizationHeader.startsWith("Bearer "))
          {
              jwt=authorizationHeader.substring(7);
              userName=jwtUtil.extractUsername(jwt);
          }

          if(userName!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
              UserDetails userDetails = userDetailService.loadUserByUsername(userName);

              if (jwtUtil.validateToken(jwt, userDetails)) {
                  UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                  authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                  SecurityContextHolder.getContext().setAuthentication(authToken);
              }
          }
          filterChain.doFilter(request,response);

    }
}
