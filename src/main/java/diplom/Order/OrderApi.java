package diplom.Order;

import diplom.TestBase;
import io.qameta.allure.Step;
import io.restassured.filter.Filter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderApi extends TestBase {
    private final Filter logRequestFilter = new RequestLoggingFilter();
    private final Filter logResponseFilter = new ResponseLoggingFilter();
    private static final String PATH = "/api/orders/";

    @Step("Создать заказ с авторизованным пользователем")
    public ValidatableResponse createOrderWithAuth(String accessToken, OrderCreate orderCreate) {
        return given()
                .filters(logRequestFilter, logResponseFilter)
                .spec(getReqSpec())
                .header("Authorization", accessToken)
                .body(orderCreate)
                .when()
                .post(PATH)
                .then();
    }

    @Step("Создать заказ с неавторизованным пользователем")
    public ValidatableResponse createOrderWithoutAuth(OrderCreate orderCreate) {
        return given()
                .filters(logRequestFilter, logResponseFilter)
                .spec(getReqSpec())
                .body(orderCreate)
                .when()
                .post(PATH)
                .then();
    }

    @Step("Создать заказ без ингредиентов")
    public ValidatableResponse createOrderWithoutIngredients(String accessToken) {
        return given()
                .filters(logRequestFilter, logResponseFilter)
                .spec(getReqSpec())
                .header("Authorization", accessToken)
                .when()
                .post(PATH)
                .then();
    }

    @Step("Получить заказы для авторизованного пользователя")
    public ValidatableResponse getOrdersWithAuth(String accessToken) {
        return given()
                .filters(logRequestFilter, logResponseFilter)
                .spec(getReqSpec())
                .header("Authorization", accessToken)
                .when()
                .get(PATH)
                .then();
    }

    @Step("Получить заказы для неавторизованного пользователя")
    public ValidatableResponse getOrdersWithoutAuth() {
        return given()
                .filters(logRequestFilter, logResponseFilter)
                .spec(getReqSpec())
                .when()
                .get(PATH)
                .then();
    }
}

