package com.example.subscription.domains;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author Sahand Jalilvand 18.01.24
 */
@Entity
@Table(name = "TLB_SUBSCRIBER")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriberEntity {

    @Id
    @Column(name = "ID")
    private UUID id;

    @Column(name = "OPTIMISTIC_LOCK_VERSION", nullable = false)
    @Version
    private int version;

    @CreatedDate
    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private Date createdAt;

    @LastModifiedDate
    @Column(name = "UPDATED_AT")
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "APPLICATION_ID",
            referencedColumnName = "ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_SUBSCRIBER_OF_APPLICATION"))
    private ApplicationEntity application;

    @OneToMany(mappedBy = "subscriber")
    private List<SubscriptionEntity> subscriptions;
}
