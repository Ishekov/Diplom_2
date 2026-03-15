package diplom.Order;

import diplom.user.UserApi;
import diplom.user.UserCreate;
import diplom.user.UserGeneratorFaker;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

//+
@DisplayName("Создание заказа без ингредиентов")
public class CreateOrderWithoutIngredientsTest {
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
    @DisplayName("Создание заказа: без ингредиентов")
    public void authorizedUserCreateOrderTest() {

        ValidatableResponse orderResp = orderApi.createOrderWithoutIngredients(accessToken);

        int statusCode = orderResp.extract().statusCode();
        boolean isOrderCreated = orderResp.extract().path("success");
        String expectedMessage = "Ingredient ids must be provided";
        String message = orderResp.extract().path("message");

        assertEquals(SC_BAD_REQUEST, statusCode, "Ожидается статус 400");
        assertEquals(expectedMessage, message, "Ожидается сообщение об ошибке");
        assertFalse(isOrderCreated, "Ожидается что заказ не будет создан");
    }
}
