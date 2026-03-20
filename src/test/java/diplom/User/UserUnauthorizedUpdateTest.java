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

import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DisplayName("Изменение данных неавторизованного пользователя")
public class UserUnauthorizedUpdateTest {
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
    @DisplayName("Изменение имени неавторизованного пользователя")
    public void changeNameUnauthorizedTest(){
        newUser.setName(faker.name().fullName().replace(" ", ""));

        ValidatableResponse updateResp = userApi.updateUserWithoutAuth(Authorization.from(newUser));

        int statusCode = updateResp.extract().statusCode();
        boolean isUserCreated = updateResp.extract().path("success");
        String errorMessage = updateResp.extract().path("message");
        String expectedMessage = "You should be authorised";

        assertEquals(SC_UNAUTHORIZED, statusCode,"Ожидается статус 401");
        assertEquals(expectedMessage, errorMessage, "Ожидается что имя не измениться");
        assertFalse(isUserCreated, "Ожидается сообщение об ошибке");
    }

    @Test
    @DisplayName("Изменение имейла неавторизованного пользователя")
    public void changeEmailUnauthorizedTest() {
        newUser.setEmail(faker.internet().emailAddress());

        ValidatableResponse updateResp = userApi.updateUserWithoutAuth(Authorization.from(newUser));

        int statusCode = updateResp.extract().statusCode();
        boolean isUserCreated = updateResp.extract().path("success");
        String errorMessage = updateResp.extract().path("message");
        String message = "You should be authorised";

        assertEquals(SC_UNAUTHORIZED, statusCode,"Ожидается статус 401");
        assertEquals(message, errorMessage, "Ожидается что имейл не измениться");
        assertFalse(isUserCreated, "Ожидается сообщение об ошибке");
    }

    @Test
    @DisplayName("Изменение пароля неавторизованного пользователя")
    public void  changePasswordUnauthorizedTest() {
        String firstPassword = newUser.getPassword();
        newUser.setPassword(faker.internet().password(8, 16, true, true, true));

        ValidatableResponse updateResp = userApi.updateUserWithoutAuth(Authorization.from(newUser));

        int statusCode = updateResp.extract().statusCode();
        boolean isUserCreated = updateResp.extract().path("success");
        String errorMessage = updateResp.extract().path("message");
        String message = "You should be authorised";

        assertEquals(SC_UNAUTHORIZED, statusCode,"Ожидается статус 401");
        assertEquals(message, errorMessage, "Ожидается что пароль не измениться");
        assertFalse(isUserCreated, "Ожидается сообщение об ошибке");
    }
}

