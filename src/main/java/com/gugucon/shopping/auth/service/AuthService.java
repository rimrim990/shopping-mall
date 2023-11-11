package com.gugucon.shopping.auth.service;

import com.gugucon.shopping.auth.domain.entity.RefreshToken;
import com.gugucon.shopping.auth.dto.request.LoginRequest;
import com.gugucon.shopping.auth.dto.response.LoginResponse;
import com.gugucon.shopping.auth.dto.response.RefreshResponse;
import com.gugucon.shopping.auth.repository.RefreshTokenRepository;
import com.gugucon.shopping.common.exception.ErrorCode;
import com.gugucon.shopping.common.exception.ShoppingException;
import com.gugucon.shopping.common.utils.JwtProvider;
import com.gugucon.shopping.member.domain.entity.Member;
import com.gugucon.shopping.member.domain.vo.Email;
import com.gugucon.shopping.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Transactional
    public LoginResponse login(final LoginRequest loginRequest) {
        final Member member = memberRepository.findByEmail(Email.from(loginRequest.getEmail()))
            .orElseThrow(() -> new ShoppingException(ErrorCode.EMAIL_NOT_REGISTERED));
        validatePassword(loginRequest, member);

        final String accessToken = jwtProvider.generateToken(String.valueOf(member.getId()));
        final String refreshToken = createRefreshToken(member.getId());

        return LoginResponse.from(accessToken, refreshToken);
    }

    public RefreshResponse refresh(final String refreshToken) {
        final RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
            .orElseThrow(() -> new ShoppingException(ErrorCode.INVALID_REFRESH_TOKEN));

        final String accessToken = jwtProvider.generateToken(String.valueOf(token.getMemberId()));
        return RefreshResponse.from(accessToken);
    }

    private String createRefreshToken(final Long memberId) {
        final String token = jwtProvider.generateRefreshToken();
        final RefreshToken refreshToken = RefreshToken.builder()
            .memberId(memberId)
            .token(token)
            .build();

        refreshTokenRepository.save(refreshToken);
        return token;
    }

    private void validatePassword(final LoginRequest loginRequest, final Member member) {
        if (!member.matchPassword(loginRequest.getPassword(), passwordEncoder)) {
            throw new ShoppingException(ErrorCode.PASSWORD_NOT_CORRECT);
        }
    }
}
