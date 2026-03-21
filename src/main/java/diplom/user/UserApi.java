package diplom.user;

import diplom.TestBase;
import io.qameta.allure.Step;
import io.restassured.filter.Filter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserApi extends TestBase {
    private final Filter logRequestFilter = new RequestLoggingFilter();
    private final Filter logResponseFilter = new ResponseLoggingFilter();

    private static final String CREATE_USER = "/api/auth/register";
    private static final String LOGIN_PATH = "/api/auth/login";
    private static final String USER_PATH = "/api/auth/user";

    @Step("Создать пользователя")
    public ValidatableResponse createUser(UserCreate data) {
        return given().log().all()
                .filters(logRequestFilter, logResponseFilter)
                .spec(getReqSpec())
                .body(data)
                .when()
                .post(CREATE_USER)
                .then();
    }

    @Step("Удалить пользователя")
    public ValidatableResponse deleteUser(String accessToken) {
        return given()
                .filters(logRequestFilter, logResponseFilter)
                .spec(getReqSpec())
                .header("Authorization", accessToken)
                .when()
                .delete(USER_PATH)
                .then();
    }
    @Step("Авторизация пользователя")
    public ValidatableResponse loginUser(Authorization authorization){
        return given()
                .filters(logRequestFilter, logResponseFilter)
                .spec(getReqSpec())
                .body(authorization)
                .when()
                .post(LOGIN_PATH)
                .then();
    }
    @Step("Обновить данные пользователя с авторизацией")
    public ValidatableResponse updateUserWithAuth(String accessToken, Authorization authorization) {
        return given()
                .filters(logRequestFilter, logResponseFilter)
                .spec(getReqSpec())
                .header("Authorization", accessToken)
                .body(authorization)
                .when()
                .patch(USER_PATH)
                .then();
    }

    @Step("Обновить данные пользователя без авторизации")
    public ValidatableResponse updateUserWithoutAuth(Authorization authorization) {
        return given()
                .filters(logRequestFilter, logResponseFilter)
                .spec(getReqSpec())
                .body(authorization)
                .when()
                .patch(USER_PATH)
                .then();
    }
}

