package com.swathi.cpass.provider.controller;
import com.swathi.cpass.provider.service.MockProviderService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
@RestController @RequestMapping("/internal/providers") public class ProviderController {
 private final MockProviderService service;private final String secret;
 public ProviderController(MockProviderService service,@Value("${cpass.internal-secret}") String secret){this.service=service;this.secret=secret;}
 private void check(String supplied){if(!MessageDigest.isEqual(secret.getBytes(StandardCharsets.UTF_8),supplied.getBytes(StandardCharsets.UTF_8)))throw new ResponseStatusException(HttpStatus.FORBIDDEN);}

 @PostMapping("/{id}/send") public MockProviderService.SendResult send(@PathVariable String id,@RequestHeader("X-Internal-Secret") String internal,@RequestBody MockProviderService.SendRequest request){check(internal);return service.send(id,request);}
}