package com.gugucon.shopping.auth.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@Getter
public final class RefreshResponse {

    private String accessToken;

    public static RefreshResponse from(final String accessToken) {
        return new RefreshResponse(accessToken);
    }
}
