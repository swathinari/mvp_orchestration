package com.swathi.cpass.identity.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CredentialRepository extends JpaRepository<ApiCredential,Long>{List<ApiCredential> findByActiveTrue();}