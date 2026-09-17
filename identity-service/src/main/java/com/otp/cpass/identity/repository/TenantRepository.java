package com.swathi.cpass.identity.repository;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TenantRepository extends JpaRepository<TenantAccount,String>{}