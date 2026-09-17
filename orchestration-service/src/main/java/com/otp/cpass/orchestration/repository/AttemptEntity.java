package com.swathi.cpass.orchestration.repository;
import jakarta.persistence.*;
import java.time.Instant;
@Entity @Table(name="attempts") public class AttemptEntity {
 @Id public String id;
 @ManyToOne(optional=false) @JoinColumn(name="message_id",nullable=false) public MessageEntity message;
 @Column(nullable=false) public String provider;
 @Column(nullable=false) public String status;
 @Column(name="external_id") public String externalId;
 @Column(name="failure_code") public String failureCode;
 @Column(name="created_at",nullable=false) public Instant createdAt;
}