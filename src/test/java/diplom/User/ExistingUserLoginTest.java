package diplom.User;

import diplom.user.Authorization;
import diplom.user.UserApi;
import diplom.user.UserCreate;
import diplom.user.UserGeneratorFaker;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Логин под существующим пользователем")
public class ExistingUserLoginTest {
    private UserApi userApi;
    private UserCreate newUser;
    private String accessToken;

    @BeforeEach
    public void init() {
        userApi = new UserApi();
        newUser = UserGeneratorFaker.userGeneratorFaker();
    }



    @AfterEach
    public void tearDown() {
        userApi.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Создание пользователя с последующей авторизацией")
    public void existingUserLogin() {
        ValidatableResponse resp = userApi.createUser(newUser);
        ValidatableResponse loginResp = userApi.loginUser(Authorization.from(newUser));

        int statusCode = loginResp.extract().statusCode();
        boolean isUserCreated = loginResp.extract().path("success");
        accessToken = loginResp.extract().path("accessToken");

        assertEquals(SC_OK, statusCode,"Ожидается статус 200");
        assertNotNull(accessToken, "При входе должен возвращаться accessToken");
        assertTrue(isUserCreated, "Ожидается успешный вход");
    }
}
