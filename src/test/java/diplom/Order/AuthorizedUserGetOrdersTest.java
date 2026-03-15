package diplom.Order;

import diplom.user.UserApi;
import diplom.user.UserCreate;
import diplom.user.UserGeneratorFaker;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Получение заказов авторизованным пользователем")
public class AuthorizedUserGetOrdersTest {
    private OrderCreate orderCreate;
    private String accessToken;
    private UserApi userApi;
    private UserCreate newUser;
    private OrderApi orderApi;

    @BeforeEach
    public void init() {
        orderApi = new OrderApi();
        userApi = new UserApi();
        orderCreate = new OrderCreate();
        newUser = UserGeneratorFaker.userGeneratorFaker();
        ValidatableResponse resp = userApi.createUser(newUser);
        accessToken = resp.extract().path("accessToken");
    }

    @AfterEach
    public void tearDown() {
        if (accessToken != null && !accessToken.isEmpty()) {
            userApi.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Получение заказов конкретного пользователя: авторизованный пользователь")
    public void authorizedUserCreateOrderTest() {
        List<String> ingredients = new ArrayList<>(Arrays.asList(
                "61c0c5a71d1f82001bdaaa6d",
                "61c0c5a71d1f82001bdaaa6f",
                "61c0c5a71d1f82001bdaaa70"));
        orderCreate.setIngredients(ingredients);
        ValidatableResponse orderResp = orderApi.getOrdersWithAuth(accessToken);

        int statusCode = orderResp.extract().statusCode();
        boolean isOrderCreated = orderResp.extract().path("success");

        assertEquals(SC_OK, statusCode, "Ожидается статус 200");
        assertTrue(isOrderCreated, "Ожидается успешное создание заказа");
    }
}
