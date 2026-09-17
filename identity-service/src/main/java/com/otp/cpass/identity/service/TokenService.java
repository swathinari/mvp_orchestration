package com.swathi.cpass.identity.service;
import com.swathi.cpass.identity.repository.*;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
@Service public class TokenService {
 private final CredentialRepository credentials; private final PasswordEncoder encoder; private final JwtEncoder jwtEncoder;
 public TokenService(CredentialRepository credentials,PasswordEncoder encoder,JwtEncoder jwtEncoder){this.credentials=credentials;this.encoder=encoder;this.jwtEncoder=jwtEncoder;}
 public String issue(String apiKey){
  var credential=credentials.findByActiveTrue().stream().filter(c->encoder.matches(apiKey,c.keyHash)).findFirst().orElseThrow(()->new IllegalArgumentException("Invalid API key"));
  Instant now=Instant.now();var claims=JwtClaimsSet.builder().issuer("cpass-local").subject(credential.tenantId).issuedAt(now).expiresAt(now.plusSeconds(900)).claim("scope","verify.read verify.write").build();
  return jwtEncoder.encode(JwtEncoderParameters.from(JwsHeader.with(org.springframework.security.oauth2.jose.jws.MacAlgorithm.HS256).build(),claims)).getTokenValue();
 }
 @Bean static PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}
 @Bean CommandLineRunner localDemoSeed(TenantRepository tenants,CredentialRepository keys,PasswordEncoder passwords,@Value("${cpass.demo-api-key:}") String demoKey){
  return args->{if(!demoKey.isBlank() && keys.count()==0){tenants.save(new TenantAccount("demo-tenant","Local demo"));keys.save(new ApiCredential("demo-tenant",passwords.encode(demoKey)));}};
 }
}