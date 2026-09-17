package com.swathi.cpass.orchestration.security;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.JwtIssuerValidator;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
@Configuration public class SecurityConfig {
 @Bean SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
  http.csrf(csrf->csrf.disable()).authorizeHttpRequests(a->a.requestMatchers("/actuator/health").permitAll().requestMatchers(HttpMethod.POST,"/v1/verify").hasAuthority("SCOPE_verify.write").requestMatchers(HttpMethod.GET,"/v1/messages/**").hasAuthority("SCOPE_verify.read").anyRequest().denyAll()).oauth2ResourceServer(o->o.jwt(Customizer.withDefaults()));return http.build();
 }
 @Bean JwtDecoder jwtDecoder(@Value("${cpass.jwt.secret}") String secret) {
  var decoder=NimbusJwtDecoder.withSecretKey(new SecretKeySpec(secret.getBytes(java.nio.charset.StandardCharsets.UTF_8),"HmacSHA256")).macAlgorithm(org.springframework.security.oauth2.jose.jws.MacAlgorithm.HS256).build();
  decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<Jwt>(JwtValidators.createDefault(),new JwtIssuerValidator("cpass-local")));
  return decoder;
 }
}