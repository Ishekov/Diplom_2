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

import static org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;
import static org.junit.jupiter.api.Assertions.assertEquals;
//+
@DisplayName("Создание заказа с неверным ингредиентом")
public class CreateOrderWithWrongIngredientTest {
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
    @DisplayName("Создание заказа: с неверными ингредиентами")
    public void authorizedUserCreateOrderTest() {
        List<String> ingredients = new ArrayList<>(Arrays.asList(
                "wrong_id_1",
                "wrong_id_2",
                "wrong_id_3"));
        orderCreate.setIngredients(ingredients);
        ValidatableResponse orderResp = orderApi.createOrderWithAuth(accessToken, orderCreate);

        int statusCode = orderResp.extract().statusCode();

        assertEquals(SC_INTERNAL_SERVER_ERROR, statusCode, "Ожидается статус 500");
    }
}