package com.gugucon.shopping.auth.security;

import com.gugucon.shopping.auth.domain.vo.JwtAuthenticationToken;
import com.gugucon.shopping.member.dto.MemberPrincipal;
import com.gugucon.shopping.common.exception.ErrorCode;
import com.gugucon.shopping.common.utils.JwtProvider;
import com.gugucon.shopping.member.domain.entity.Member;
import com.gugucon.shopping.member.repository.MemberRepository;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        final String jwtToken = ((JwtAuthenticationToken) authentication).getJwtToken();
        validateToken(jwtToken);
        final Long memberId = Long.valueOf(jwtProvider.parseToken(jwtToken));
        final MemberPrincipal principal = getPrincipal(memberId);
        return new JwtAuthenticationToken(principal, "", new ArrayList<>());
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private void validateToken(final String jwtToken) {
        if (!jwtProvider.validate(jwtToken)) {
            throw new BadCredentialsException(ErrorCode.INVALID_TOKEN.getMessage());
        }
    }

    private MemberPrincipal getPrincipal(final Long principal) {
        final Member member = memberRepository.findById(principal)
            .orElseThrow(() -> new UsernameNotFoundException(ErrorCode.EMAIL_NOT_REGISTERED.getMessage()));
        return MemberPrincipal.from(member);
    }
}
