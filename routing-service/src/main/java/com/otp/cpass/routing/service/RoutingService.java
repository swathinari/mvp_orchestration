package com.swathi.cpass.routing.service;
import com.swathi.cpass.routing.repository.*;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
@Service public class RoutingService {
 private final PolicyRepository policies;public RoutingService(PolicyRepository policies){this.policies=policies;}
 public record Route(List<String> providers,int maxAttempts) implements java.io.Serializable {}
 @Cacheable(cacheNames="routes",key="#tenant + ':' + #name + ':' + #channel")
 public Route route(String tenant,String name,String channel){var p=policies.findByTenantIdAndPolicyNameAndChannel(tenant,name,channel).orElseThrow(()->new IllegalArgumentException("No routing policy"));return new Route(Arrays.asList(p.providerIds.split(",")),p.maxAttempts);}
 @Bean CommandLineRunner seedPolicies(PolicyRepository policies){return args->{if(policies.count()==0){policies.save(new RoutingPolicy("demo-tenant","otp_default","SMS","sms_a,sms_b",2));policies.save(new RoutingPolicy("demo-tenant","otp_default","WHATSAPP","whatsapp_mock",1));policies.save(new RoutingPolicy("demo-tenant","otp_default","RCS","rcs_mock",1));policies.save(new RoutingPolicy("demo-tenant","otp_default","VOICE","voice_mock",1));}};}
}