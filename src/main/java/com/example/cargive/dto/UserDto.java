package com.example.cargive.dto;

import com.example.cargive.entity.User;
import com.example.cargive.security.oauth.SNSType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long userId;

    private String email;

    private String userName;

    private String profilePicture;

    private boolean isDeleted;

    private SNSType snsType;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static UserDto toDto(User user) {
        return new UserDto(user.getUserId(), user.getEmail(), user.getUserName(), user.getProfilePicture(), user.isDeleted(), user.getSnsType(),
                user.getCreatedAt(), user.getUpdatedAt());
    }
}
