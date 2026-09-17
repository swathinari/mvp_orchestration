package com.swathi.cpass.orchestration.repository;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.*;
@Entity @Table(name="messages",uniqueConstraints=@UniqueConstraint(columnNames={"tenant_id","idempotency_key"})) public class MessageEntity {
 @Id public String id;
 @Column(name="tenant_id",nullable=false) public String tenantId;
 @Column(name="idempotency_key",nullable=false) public String idempotencyKey;
 @Column(name="request_hash",nullable=false) public String requestHash;
 @Column(name="verification_id",nullable=false) public String verificationId;
 @Column(nullable=false) public String status;
 @Column(nullable=false) public String channel;
 @Column(name="selected_provider") public String selectedProvider;
 @Column(name="trace_id",nullable=false) public String traceId;
 @Column(name="created_at",nullable=false) public Instant createdAt;
 @OneToMany(mappedBy="message",cascade=CascadeType.ALL,fetch=FetchType.EAGER,orphanRemoval=true) @OrderBy("createdAt ASC") public List<AttemptEntity> attempts=new ArrayList<>();
 public void addAttempt(AttemptEntity a){a.message=this;attempts.add(a);}
}