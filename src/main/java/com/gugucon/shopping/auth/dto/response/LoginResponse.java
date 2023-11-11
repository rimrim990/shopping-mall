package com.gugucon.shopping.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public final class LoginResponse {

    private String accessToken;

    @JsonIgnore
    private String refreshToken;

    public static LoginResponse from(final String accessToken, final String refreshToken) {
        return new LoginResponse(accessToken, refreshToken);
    }
}
