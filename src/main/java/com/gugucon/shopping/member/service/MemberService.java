package com.gugucon.shopping.member.service;

import com.gugucon.shopping.common.exception.ErrorCode;
import com.gugucon.shopping.common.exception.ShoppingException;
import com.gugucon.shopping.member.domain.entity.Member;
import com.gugucon.shopping.member.domain.vo.BirthYearRange;
import com.gugucon.shopping.member.domain.vo.Email;
import com.gugucon.shopping.member.domain.vo.Gender;
import com.gugucon.shopping.member.domain.vo.Nickname;
import com.gugucon.shopping.member.domain.vo.Password;
import com.gugucon.shopping.member.dto.request.SignupRequest;
import com.gugucon.shopping.member.repository.MemberRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signup(final SignupRequest signupRequest) {
        validatePasswordChecked(signupRequest.getPassword(), signupRequest.getPasswordCheck());
        validateBirthDate(signupRequest.getBirthDate());
        final Email email = Email.from(signupRequest.getEmail());
        validateEmailNotExist(email);
        final Password password = Password.of(signupRequest.getPassword(), passwordEncoder);
        final Nickname nickname = Nickname.from(signupRequest.getNickname());
        final Gender gender = Gender.from(signupRequest.getGender());
        final Member member = Member.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .gender(gender)
                .birthDate(signupRequest.getBirthDate())
                .build();
        memberRepository.save(member);
    }

    private void validateBirthDate(final LocalDate birthDate) {
        if (!BirthYearRange.isInRange(birthDate)) {
            throw new ShoppingException(ErrorCode.INVALID_BIRTH_DATE);
        }
    }

    private void validateEmailNotExist(final Email email) {
        if (isEmailExist(email)) {
            throw new ShoppingException(ErrorCode.EMAIL_ALREADY_EXIST);
        }
    }

    private boolean isEmailExist(final Email email) {
        return memberRepository.findByEmail(email)
                               .isPresent();
    }

    private void validatePasswordChecked(final String password, final String passwordCheck) {
        if (!password.equals(passwordCheck)) {
            throw new ShoppingException(ErrorCode.PASSWORD_CHECK_NOT_SAME);
        }
    }
}
