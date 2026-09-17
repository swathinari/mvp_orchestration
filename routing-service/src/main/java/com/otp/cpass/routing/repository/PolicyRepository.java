package com.swathi.cpass.routing.repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PolicyRepository extends JpaRepository<RoutingPolicy,Long>{Optional<RoutingPolicy> findByTenantIdAndPolicyNameAndChannel(String tenant,String name,String channel);}