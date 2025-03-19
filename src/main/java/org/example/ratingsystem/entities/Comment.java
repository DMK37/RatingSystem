package org.example.ratingsystem.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private UUID id;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "is_positive", nullable = false)
    private boolean isPositive;

    @ManyToOne
    @JoinColumn(name = "seller_id", referencedColumnName = "id", nullable = false)
    @ToString.Exclude
    private User seller;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    @ToString.Exclude
    private User author;

    @Column(name = "created_at", nullable = false, updatable = false)
    private long createdAt;

    @Column(name = "approved", nullable = false)
    private boolean approved;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now().getEpochSecond();
        approved = false;
    }
}
