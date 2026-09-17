package com.swathi.cpass.identity.controller;
import com.swathi.cpass.identity.service.TokenService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/auth") public class AuthController {
 private final TokenService service;public AuthController(TokenService service){this.service=service;}
 public record TokenResponse(String accessToken,String tokenType,long expiresIn){}
 @PostMapping("/token") public TokenResponse token(@RequestHeader("X-API-Key") String key){return new TokenResponse(service.issue(key),"Bearer",900);}
 @ExceptionHandler(IllegalArgumentException.class) ResponseEntity<String> invalid(){return ResponseEntity.status(401).body("Invalid credentials");}
}