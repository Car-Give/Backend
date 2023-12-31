package com.example.cargive.domain.member.entity;

import com.example.cargive.global.base.BaseException;
import com.example.cargive.global.base.BaseResponseStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Email {
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\." +
            "[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
            "A-Z]{2,7}$";
    private static final Pattern EMAIL_MATCHER = Pattern.compile(EMAIL_PATTERN);

    @Column(name = "email", nullable = false, updatable = false)
    private String value;

    private Email(String value) {
        this.value = value;
    }

    public static Email from(String value) throws BaseException {
        validateEmailPattern(value);
        return new Email(value);
    }

    private static void validateEmailPattern(String value) {
        if (isNotValidPattern(value)) {
            throw new BaseException(BaseResponseStatus.INVALID_EMAIL_FORMAT);
        }
    }

    private static boolean isNotValidPattern(String email) {
        return !EMAIL_MATCHER.matcher(email).matches();
    }
}
