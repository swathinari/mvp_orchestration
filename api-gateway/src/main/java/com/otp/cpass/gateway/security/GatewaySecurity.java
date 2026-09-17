package com.otp.cpass.gateway.security;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.JwtIssuerValidator;
import org.springframework.security.oauth2.jwt.Jwt;
@Configuration public class GatewaySecurity {
 @Bean SecurityWebFilterChain chain(ServerHttpSecurity http){return http.csrf(ServerHttpSecurity.CsrfSpec::disable).authorizeExchange(x->x.pathMatchers("/auth/token","/actuator/health").permitAll().pathMatchers(HttpMethod.POST,"/v1/verify").hasAuthority("SCOPE_verify.write").pathMatchers(HttpMethod.GET,"/v1/messages/**").hasAuthority("SCOPE_verify.read").anyExchange().denyAll()).oauth2ResourceServer(x->x.jwt(j->{})).build();}
 @Bean ReactiveJwtDecoder decoder(@Value("${cpass.jwt.secret}") String secret){
  var d=NimbusReactiveJwtDecoder.withSecretKey(new SecretKeySpec(secret.getBytes(java.nio.charset.StandardCharsets.UTF_8),"HmacSHA256")).macAlgorithm(org.springframework.security.oauth2.jose.jws.MacAlgorithm.HS256).build();
  d.setJwtValidator(new DelegatingOAuth2TokenValidator<Jwt>(JwtValidators.createDefault(),new JwtIssuerValidator("cpass-local")));return d;}
}