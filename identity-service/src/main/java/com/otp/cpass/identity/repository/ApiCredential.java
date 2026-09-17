package com.swathi.cpass.identity.repository;
import jakarta.persistence.*;
@Entity @Table(name="api_credentials") public class ApiCredential {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @Column(name="tenant_id",nullable=false) public String tenantId;
 @Column(name="key_hash",nullable=false) public String keyHash;
 @Column(nullable=false) public boolean active;
 protected ApiCredential(){} public ApiCredential(String tenantId,String keyHash){this.tenantId=tenantId;this.keyHash=keyHash;this.active=true;}
}