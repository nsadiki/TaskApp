package com.taskManager.taskapp.controllers;


import com.taskManager.taskapp.configuration.JwtUtil;
import com.taskManager.taskapp.dto.LoginReq;
import com.taskManager.taskapp.dto.LoginRes;
import com.taskManager.taskapp.dto.UserDto;
import com.taskManager.taskapp.entities.User;
import com.taskManager.taskapp.mapper.MapDtoToEntity;
import com.taskManager.taskapp.services.JwtService;
import com.taskManager.taskapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;


    @Autowired
    private UserService userService;

    @Autowired
    private MapDtoToEntity mapDtoToEntity; // pas propre je pense

    private JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil){
        this.authenticationManager= authenticationManager;
        this.jwtUtil=jwtUtil;
    }

    @ResponseBody
    @PostMapping(value="/login")
    public ResponseEntity login(@RequestBody LoginReq loginReq){

        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginReq.getEmail(),
                    loginReq.getPassword()
            ));
            UserDto user = userService.getUserByEmail(loginReq.getEmail());
            String email = authentication.getName();
            System.out.println("Email : " + email);
            String token = jwtUtil.createToken(user);
            System.out.println("Token : " + token + ", "+email);
            LoginRes loginRes = new LoginRes(email, token);
            System.out.println("test du loginRes : " + loginRes.getEmail() + ", "+loginRes.getToken());
            return ResponseEntity.ok(loginRes);
        }catch (BadCredentialsException e){

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid username or password");
        }catch (Exception e){

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
