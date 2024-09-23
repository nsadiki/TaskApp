package com.taskManager.taskapp.configuration;


import com.taskManager.taskapp.dto.UserDto;
import com.taskManager.taskapp.entities.User;
import com.taskManager.taskapp.mapper.MapEntityToDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger((JwtUtil.class));


    private final Key SECRET_KEY;
    private  long accessTokenValidity= 60*60*1000;

    private final JwtParser jwtParser;

    private final String TOKEN_HEADER= "Authorization";
    private final String TOKEN_PREFIX= "Bearer ";

    public JwtUtil(){

        this.SECRET_KEY= Keys.secretKeyFor(SignatureAlgorithm.HS256);
        this.jwtParser= Jwts.parserBuilder().setSigningKey(SECRET_KEY).build();
    }

    public String createToken(UserDto user){
        Claims claims = Jwts.claims().setSubject(user.getEmail());
        claims.put("username", user.getUsername());
        logger.info("Le user correspond à ça : " + user.toString());
        claims.put("userId", user.getId());
        Date tokenCreateTime= new Date();
        Date tokenValidity = new Date(tokenCreateTime.getTime() + TimeUnit.MINUTES.toMillis(accessTokenValidity));
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(tokenValidity)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    private Claims parseJwtClaims(String token){
        return jwtParser.parseClaimsJws(token).getBody();
    }

    public String resolveToken(HttpServletRequest req){
        String bearerToken = req.getHeader(TOKEN_HEADER);
        if(bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)){
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    public Claims resolveClaims(HttpServletRequest req){
        try{
            String token =resolveToken(req);
            if(token != null){
                return parseJwtClaims(token);
            }
            return null;
        }catch (ExpiredJwtException ex){
            req.setAttribute("expired", ex.getMessage());
            throw ex;
        }catch (Exception ex){
            req.setAttribute("invalid", ex.getMessage());
            throw ex;
        }
    }

    public boolean validateClaims(Claims claims) throws AuthenticationException{
        try{
            return claims.getExpiration().after(new Date());
        }catch(Exception ex){
            throw ex;
        }


    }

    public String getEmail(Claims claims){
        return claims.getSubject();
    }

    private List<String> getRoles(Claims claims){
        return (List<String>)claims.get("roles");
    }


}
