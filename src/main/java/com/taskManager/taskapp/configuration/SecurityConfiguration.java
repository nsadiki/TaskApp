package com.taskManager.taskapp.configuration;

import com.taskManager.taskapp.services.CustomUserDetailsService;
import org.apache.catalina.realm.AuthenticatedUserRealm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {


   private final CustomUserDetailsService userDetailsService;
   private final JwtAuthenticationFilter jwtAuthenticationFilter;

   public SecurityConfiguration(CustomUserDetailsService userDetailsService, JwtAuthenticationFilter jwtAuthenticationFilter){
       this.userDetailsService = userDetailsService;
       this.jwtAuthenticationFilter =jwtAuthenticationFilter;
   }

   @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, NoOpPasswordEncoder noOpPasswordEncoder) throws Exception{

       AuthenticationManagerBuilder authenticationManagerBuilder= http.getSharedObject(AuthenticationManagerBuilder.class);
       authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(noOpPasswordEncoder);
       return authenticationManagerBuilder.build();
   }

   @Bean SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
       http
               .cors(cors -> cors
                       .configurationSource(corsConfigurationSource())
               )
               .authorizeHttpRequests(auth -> auth
                       .requestMatchers("/h2-console/**", "/auth/**").permitAll()
               )
               .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
               .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
               .headers(headers -> headers
                       .frameOptions(frameOption -> frameOption.sameOrigin())
               )
               .csrf(csrf -> csrf
                       .ignoringRequestMatchers("/h2-console/**").disable()
               )
               .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

       return http.build();
   }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }


    @SuppressWarnings("deprecation")
    @Bean
    public NoOpPasswordEncoder passwordEncoder(){
       return (NoOpPasswordEncoder)  NoOpPasswordEncoder.getInstance();
    }


}