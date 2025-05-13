package com.baro.challenges.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name="p_user")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, length = 10, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @ColumnDefault("USER")
    private Role role;

    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean isDeleted;

    @Getter
    @AllArgsConstructor
    public enum Role{
        USER("USER"),
        MASTER("MASTER")
        ;

        private final String value;
    }
}