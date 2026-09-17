package com.swathi.cpass.routing.repository;
import jakarta.persistence.*;
@Entity @Table(name="routing_policies",uniqueConstraints=@UniqueConstraint(columnNames={"tenant_id","policy_name","channel"})) public class RoutingPolicy {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @Column(name="tenant_id",nullable=false) public String tenantId;
 @Column(name="policy_name",nullable=false) public String policyName;
 @Column(nullable=false) public String channel;
 @Column(name="provider_ids",nullable=false) public String providerIds;
 @Column(name="max_attempts",nullable=false) public int maxAttempts;
 protected RoutingPolicy(){}public RoutingPolicy(String tenantId,String name,String channel,String providers,int maxAttempts){this.tenantId=tenantId;this.policyName=name;this.channel=channel;this.providerIds=providers;this.maxAttempts=maxAttempts;}
}