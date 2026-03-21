package diplom.User;

import diplom.user.UserApi;
import diplom.user.UserCreate;
import diplom.user.UserGeneratorFaker;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Создание пользователя")
public class UserAddTest {
    private final UserApi userApi = new UserApi();
    private UserCreate newUser;
    private String accessToken;

    @BeforeEach
    public void init() {
        newUser = UserGeneratorFaker.userGeneratorFaker();
    }
    @AfterEach
    public void tearDown() {
        userApi.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Создать уникального пользователя")
    public void userAddTest() {

        ValidatableResponse resp = userApi.createUser(newUser);

        accessToken = resp.extract().path("accessToken");
        int statusCode = resp.extract().statusCode();
        boolean isUserCreated = resp.extract().path("success");

        assertEquals(SC_OK, statusCode,"Ожидается статус 200");
        assertTrue(isUserCreated, "Ожидается успешное создание пользователя");
    }
}
