package com.swathi.cpass.orchestration.controller;
import com.swathi.cpass.orchestration.service.OrchestrationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.NoSuchElementException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/v1") public class VerifyController {
 private final OrchestrationService service;public VerifyController(OrchestrationService service){this.service=service;}
 public record VerifyInput(@NotBlank @Pattern(regexp="^\\+[1-9][0-9]{7,14}$") String recipient,@NotBlank String templateId,@NotBlank String channel,@NotBlank String channelPolicy){}
 @PostMapping("/verify") public OrchestrationService.MessageView verify(JwtAuthenticationToken auth,@RequestHeader("Authorization") String token,@RequestHeader("Idempotency-Key") String key,@Valid @RequestBody VerifyInput input){
  return service.verify(auth.getToken().getSubject(),token,key,new OrchestrationService.VerifyRequest(input.recipient(),input.templateId(),input.channel(),input.channelPolicy()));}
 @GetMapping("/messages/{id}") public OrchestrationService.MessageView get(JwtAuthenticationToken auth,@PathVariable String id){return service.get(auth.getToken().getSubject(),id);}
 @ExceptionHandler(IllegalArgumentException.class) ResponseEntity<String> bad(IllegalArgumentException e){return ResponseEntity.badRequest().body(e.getMessage());}
 @ExceptionHandler(NoSuchElementException.class) ResponseEntity<String> missing(){return ResponseEntity.notFound().build();}
}