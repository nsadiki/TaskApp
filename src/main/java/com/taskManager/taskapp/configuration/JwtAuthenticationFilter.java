package com.taskManager.taskapp.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskManager.taskapp.services.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private final JwtUtil jwtUtil;
    private final ObjectMapper mapper;


    public JwtAuthenticationFilter(JwtUtil jwtUtil, ObjectMapper mapper) {
        this.jwtUtil = jwtUtil;
        this.mapper = mapper;

    }


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain
    )throws ServletException, IOException {
        Map<String, Object> errorDetails = new HashMap<>();

        try{
            String accessToken = jwtUtil.resolveToken(request);
            if(accessToken==null){
                filterChain.doFilter(request,response);
                return;
            }
            System.out.println("token : " + accessToken);
            Claims claims = jwtUtil.resolveClaims(request);

            if(claims !=null && jwtUtil.validateClaims(claims)){
                String email = claims.getSubject();
                System.out.println("email : " + email);
                Authentication authentication = new UsernamePasswordAuthenticationToken(email,"", new ArrayList<>());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }catch (Exception e){
            errorDetails.put("message","Authentication Error");
            errorDetails.put("details", e.getMessage());
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            mapper.writeValue(response.getWriter(), errorDetails);
        }

        filterChain.doFilter(request,response);
    }

//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//
//        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
//        configuration.setAllowedMethods(List.of("GET","POST","PUT","DELETE"));
//        configuration.setAllowedHeaders(List.of("Authorization","Content-Type"));
//        configuration.getAllowCredentials();
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//
//        source.registerCorsConfiguration("/**",configuration);
//
//        return source;
//    }

}
