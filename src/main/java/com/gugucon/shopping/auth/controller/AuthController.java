package com.gugucon.shopping.auth.controller;

import com.gugucon.shopping.auth.dto.request.LoginRequest;
import com.gugucon.shopping.auth.dto.response.LoginResponse;
import com.gugucon.shopping.auth.dto.response.RefreshResponse;
import com.gugucon.shopping.auth.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private static final String COOKIE_HEADER = "Set-Cookie";
    private static final String REFRESH_COOKIE_SAME_SITE = "Strict";
    private static final String REFRESH_COOKIE_KEY = "refreshToken";

    private final AuthService authService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponse login(@RequestBody @Valid final LoginRequest loginRequest,
        final HttpServletResponse response) {
        final LoginResponse loginResponse = authService.login(loginRequest);
        final ResponseCookie cookie = ResponseCookie.from(REFRESH_COOKIE_KEY)
            .httpOnly(true)
            .value(loginResponse.getRefreshToken())
            .sameSite(REFRESH_COOKIE_SAME_SITE)
            .build();

        response.addHeader(COOKIE_HEADER, cookie.toString());
        return loginResponse;
    }

    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.CREATED)
    public RefreshResponse refresh(@CookieValue("refreshToken") final String refreshToken) {
        return authService.refresh(refreshToken);
    }
}
