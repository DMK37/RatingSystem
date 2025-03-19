package org.example.ratingsystem.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "users_rankings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserRanking {
    @Id
    @Column(name = "user_id", nullable = false, updatable = false, unique = true)
    private UUID id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ToString.Exclude
    private User user;

    @Column(name = "positive_comment_count", nullable = false)
    private int positiveCommentCount;

    @Column(name = "comment_count", nullable = false)
    private int commentCount;
}
