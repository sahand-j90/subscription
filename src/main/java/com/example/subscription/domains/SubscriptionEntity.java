package com.example.subscription.domains;

import com.example.subscription.enums.SubscriptionStateEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

/**
 * @author Sahand Jalilvand 18.01.24
 */
@Entity
@Table(name = "TLB_SUBSCRIPTION")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SubscriptionEntity implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
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

    @Column(name = "SUBSCRIPTION_STATE", nullable = false)
    @Enumerated(EnumType.STRING)
    private SubscriptionStateEnum state;

    @Column(name = "FROM_DATE", nullable = false)
    private LocalDate from;

    @Column(name = "TO_DATE", nullable = false)
    private LocalDate to;

    @ManyToOne
    @JoinColumn(name = "SUBSCRIBER_ID",
            referencedColumnName = "ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_SUBSCRIPTION_OF_SUBSCRIBER"))
    private SubscriberEntity subscriber;
}
