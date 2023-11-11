package com.gugucon.shopping.integration;

import static com.gugucon.shopping.utils.ApiUtils.getRefreshTokenAfterLogin;
import static com.gugucon.shopping.utils.ApiUtils.loginAfterSignUp;
import static com.gugucon.shopping.utils.ApiUtils.signup;
import static org.assertj.core.api.Assertions.assertThat;

import com.gugucon.shopping.common.exception.ErrorCode;
import com.gugucon.shopping.common.exception.ErrorResponse;
import com.gugucon.shopping.integration.config.IntegrationTest;
import com.gugucon.shopping.item.dto.request.CartItemInsertRequest;
import com.gugucon.shopping.auth.dto.response.RefreshResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@IntegrationTest
@DisplayName("jwt 토큰 인증 기능 통합 테스트")
class AuthIntegrationTest {

    @Test
    @DisplayName("jwt 토큰 인증에 성공한다")
    void authenticate() {
        // given
        final String accessToken = loginAfterSignUp("test_email@woowafriends.com", "test_password!");

        // when
        final ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/api/v1/order-history")
            .then().log().all()
            .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("jwt 토큰 정보가 유효하지 않으면 인증에 실패한다")
    void authenticateFail_jwtTokenNotExist() {
        // given
        final String invalidToken = "";
        final CartItemInsertRequest cartItemInsertRequest = new CartItemInsertRequest(1L);

        // when
        final ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .auth().oauth2(invalidToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .body(cartItemInsertRequest)
            .when().post("/api/v1/cart/items")
            .then().log().all()
            .extract();

        /* then */
        final ErrorResponse errorResponse = response.as(ErrorResponse.class);
        assertThat(errorResponse.getErrorCode()).isEqualTo(ErrorCode.LOGIN_REQUESTED);
        assertThat(errorResponse.getMessage()).isEqualTo(ErrorCode.LOGIN_REQUESTED.getMessage());
    }

    @Test
    @DisplayName("유효한 리프레시 토큰으로 재발급을 요청하면 액세스 토큰을 재발급한다")
    void reissueSuccess_validRefreshToken() {
        //given
        final String email = "test_email@woowafriends.com";
        final String password = "test_password";
        signup(email, password);

        final String refreshToken = getRefreshTokenAfterLogin(email, password);

        // when
        final ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .cookie("refreshToken", refreshToken)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/api/v1/auth/refresh")
            .then().log().all()
            .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        final RefreshResponse refreshResponse = response.as(RefreshResponse.class);
        assertThat(refreshResponse.getAccessToken()).isNotEmpty();
    }

    @Test
    @DisplayName("유효하지 않은 리프레시 토큰으로 재발급을 요청하면 401 상태를 반환한다")
    void reissueFail_invalidRefreshToken_statusUnauthorized() {
        // given
        final String invalidRefreshToken = "invalid";

        // when
        final ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .cookie("refreshToken", invalidRefreshToken)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/api/v1/auth/refresh")
            .then().log().all()
            .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    @DisplayName("리프레시 토큰 쿠키가 존재하지 않은 상태에서 재발급을 요청하면 400 상태를 반환한다")
    void reissueFail_emptyRefreshToken_statusBadRequest() {
        // when
        final ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/api/v1/auth/refresh")
            .then().log().all()
            .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        final ErrorResponse errorResponse = response.as(ErrorResponse.class);
        assertThat(errorResponse.getErrorCode()).isEqualTo(ErrorCode.REQUIRED_COOKIE_MISSING);
        assertThat(errorResponse.getMessage()).isEqualTo("refreshToken is missing");
    }
}
