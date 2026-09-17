package com.swathi.cpass.identity.repository;
import jakarta.persistence.*;
@Entity @Table(name="tenant_accounts") public class TenantAccount {
 @Id public String id; @Column(nullable=false) public String name;
 protected TenantAccount(){} public TenantAccount(String id,String name){this.id=id;this.name=name;}
}