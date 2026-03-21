package diplom.User;

import diplom.user.Authorization;
import diplom.user.UserApi;
import diplom.user.UserCreate;
import diplom.user.UserGeneratorFaker;
import io.restassured.response.ValidatableResponse;
import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Изменение данных авторизованного пользователя")
public class UserProfileUpdateServiceTest {
    private static final Faker faker = new Faker();
    private UserApi userApi;
    private UserCreate newUser;
    private String accessToken;

    @BeforeEach
    public void init() {
        userApi = new UserApi();
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
    @DisplayName("Изменение имени авторизованного пользователя")
    public void changeNameForAuthorizedUserTest(){
        String firstName = newUser.getName();
        newUser.setName(faker.name().fullName().replace(" ", ""));

        ValidatableResponse updateResp = userApi.updateUserWithAuth(accessToken, Authorization.from(newUser));

        int statusCode = updateResp.extract().statusCode();
        boolean isUserCreated = updateResp.extract().path("success");

        assertEquals(SC_OK, statusCode,"Ожидается статус 200");
        assertTrue(isUserCreated, "Ожидается успешное изменение имени пользователя");
        assertNotEquals(firstName, newUser.getName(), "Ожидается изменение имени");
    }

    @Test
    @DisplayName("Изменение имейла авторизованного пользователя")
    public void changeEmailForAuthorizedUserTest() {
        String firstEmail = newUser.getEmail();
        newUser.setEmail(faker.internet().emailAddress());

        ValidatableResponse updateResp = userApi.updateUserWithAuth(accessToken, Authorization.from(newUser));

        int statusCode = updateResp.extract().statusCode();
        boolean isUserCreated = updateResp.extract().path("success");

        assertEquals(SC_OK, statusCode,"Ожидается статус 200");
        assertTrue(isUserCreated, "Ожидается успешное изменение имени пользователя");
        assertNotEquals(firstEmail, newUser.getEmail(), "Ожидается изменение имени");
    }

    @Test
    @DisplayName("Изменение пароля авторизованного пользователя")
    public void  changePasswordForAuthorizedUserTest() {
        String firstPassword = newUser.getPassword();
        newUser.setPassword(faker.internet().password(8, 16, true, true, true));

        ValidatableResponse updateResp = userApi.updateUserWithAuth(accessToken, Authorization.from(newUser));

        int statusCode = updateResp.extract().statusCode();
        boolean isUserCreated = updateResp.extract().path("success");

        assertEquals(SC_OK, statusCode,"Ожидается статус 200");
        assertTrue(isUserCreated, "Ожидается успешное изменение имени пользователя");
        assertNotEquals(firstPassword, newUser.getPassword(), "Ожидается изменение имени");
    }
}
