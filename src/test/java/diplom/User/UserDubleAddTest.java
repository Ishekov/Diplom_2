package diplom.User;

import diplom.user.UserApi;
import diplom.user.UserCreate;
import diplom.user.UserGeneratorFaker;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DisplayName("Создать пользователя, который уже зарегистрирован")
public class UserDubleAddTest {
    private final UserApi userApi = new UserApi();
    private UserCreate newUser;
    private String accessToken;

    @BeforeEach
    public void init() {
        newUser = UserGeneratorFaker.userGeneratorFaker();
    }

    @AfterEach
    public void tearDown() {
        if (accessToken != null && !accessToken.isEmpty()) {
            userApi.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Создать пользователя, который уже зарегистрирован")
    public void userDubleAddTest() {
        userApi.createUser(newUser);
        ValidatableResponse resp = userApi.createUser(newUser);

        String message = resp.extract().path("message");
        int statusCode = resp.extract().statusCode();
        boolean isUserCreated = resp.extract().path("success");
        String expected = "User already exists";

        assertEquals(SC_FORBIDDEN, statusCode,"Ожидается статус 403 Forbidden");
        assertEquals(expected, message, "Ожидается сообщение 'Пользователь уже существует'");
        assertFalse(isUserCreated, "Ожидается сообщение об ошибке");
    }
}
