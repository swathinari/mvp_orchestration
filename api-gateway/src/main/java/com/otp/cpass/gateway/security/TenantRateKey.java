package com.otp.cpass.gateway.security;
import reactor.core.publisher.Mono;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
@Configuration public class TenantRateKey {
 @Bean KeyResolver tenantKeyResolver(){return exchange->ReactiveSecurityContextHolder.getContext().map(ctx->ctx.getAuthentication()).ofType(JwtAuthenticationToken.class).map(auth->auth.getToken().getSubject()).defaultIfEmpty("anonymous");}
}