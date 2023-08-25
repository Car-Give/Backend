package com.example.cargive.entity;

import com.example.cargive.security.oauth.SNSType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, length = 30)
    private String email;

    @Column(length = 20)
    private String userName;

    @Column(nullable = false)
    private boolean isDeleted;

    @Column
    private String profilePicture;

    @Enumerated(value = EnumType.STRING)
    private SNSType snsType;
}
