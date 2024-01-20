package com.example.subscription.domains;

import com.example.subscription.enums.UserAuthorityEnum;
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

/**
 * @author Sahand Jalilvand 18.01.24
 */
@Entity
@Table(name = "TLB_USER")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @Column(name = "USERNAME")
    private String username;

    @Column(name = "OPTIMISTIC_LOCK_VERSION", nullable = false)
    @Version
    private int version;

    @CreatedDate
    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private Date createdAt;

    @LastModifiedDate
    @Column(name = "UPDATED_AT")
    private Date updatedAt;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "IS_ENABLED", nullable = false)
    Boolean enabled;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "TBL_USER_AUTHORITY",
            joinColumns = {@JoinColumn(name = "USERNAME")},
            foreignKey = @ForeignKey(name = "FK_AUTHORITIES_OF_USER"))
    @Column(name = "AUTHORITY", nullable = false)
    @Enumerated(EnumType.STRING)
    private List<UserAuthorityEnum> authorities;

}
