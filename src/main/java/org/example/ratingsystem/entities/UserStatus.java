package org.example.ratingsystem.entities;

import jakarta.persistence.*;
import lombok.*;
import org.example.ratingsystem.enums.ApprovalStatus;

import java.util.UUID;

@Entity
@Table(name = "users_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserStatus {
    @Id
    @Column(name = "user_id", nullable = false, updatable = false, unique = true)
    private UUID id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ToString.Exclude
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ApprovalStatus status;
}
