package com.swathi.cpass.orchestration.repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
public interface MessageRepository extends JpaRepository<MessageEntity,String>{
 Optional<MessageEntity> findByTenantIdAndIdempotencyKey(String tenantId,String idempotencyKey);
 Optional<MessageEntity> findByTenantIdAndId(String tenantId,String id);
}