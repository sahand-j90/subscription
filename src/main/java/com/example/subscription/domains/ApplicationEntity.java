package com.example.subscription.domains;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

/**
 * @author Sahand Jalilvand 18.01.24
 */
@Entity
@Table(name = "TLB_APPLICATION")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "idempotent_key")
    private UUID id;

    @Column(name = "channel")
    private String channel;

}
