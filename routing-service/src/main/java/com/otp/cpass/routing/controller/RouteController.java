package com.swathi.cpass.routing.controller;
import com.swathi.cpass.routing.service.RoutingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
@RestController @RequestMapping("/internal/routes") public class RouteController {
 private final RoutingService routes;private final String secret;
 public RouteController(RoutingService routes,@Value("${cpass.internal-secret}") String secret){this.routes=routes;this.secret=secret;}
 private void check(String supplied){if(!MessageDigest.isEqual(secret.getBytes(StandardCharsets.UTF_8),supplied.getBytes(StandardCharsets.UTF_8)))throw new ResponseStatusException(HttpStatus.FORBIDDEN);}

 @GetMapping public RoutingService.Route route(JwtAuthenticationToken auth,@RequestHeader("X-Internal-Secret") String internal,@RequestParam String name,@RequestParam String channel){check(internal);return routes.route(auth.getToken().getSubject(),name,channel);}
 @ExceptionHandler(IllegalArgumentException.class) ResponseEntity<String> missing(){return ResponseEntity.notFound().build();}
}