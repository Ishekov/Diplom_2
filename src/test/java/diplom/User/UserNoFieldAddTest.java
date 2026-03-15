package diplom.User;

import diplom.user.UserApi;
import diplom.user.UserCreate;
import diplom.user.UserGeneratorFaker;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DisplayName("Создать пользователя и не заполнить одно из обязательных полей")
public class UserNoFieldAddTest {
    private final UserApi userApi = new UserApi();
    private String accessToken;

    static Stream<Arguments> userData() {
        return Stream.of(
                Arguments.of(UserGeneratorFaker.userGeneratorFakerNonEmail(), "без email"),
                Arguments.of(UserGeneratorFaker.userGeneratorFakerNonPassword(), "без пароля"),
                Arguments.of(UserGeneratorFaker.userGeneratorFakerNonName(), "без имени")
        );
    }

    @AfterEach
    public void tearDown() {
        if (accessToken != null && !accessToken.isEmpty()) {
            userApi.deleteUser(accessToken);
        }
    }

    @ParameterizedTest(name = "{index}: {1}")
    @MethodSource("userData")
    @DisplayName("Создать пользователя и не заполнить одно из обязательных полей")
    public void userNoFieldAddTest(UserCreate newUser, String testDescription) {
        ValidatableResponse resp = userApi.createUser(newUser);

        String message = resp.extract().path("message");
        int statusCode = resp.extract().statusCode();
        boolean isUserCreated = resp.extract().path("success");
        String expectedMessage = "Email, password and name are required fields";

        assertEquals(SC_FORBIDDEN, statusCode, "Для случая " + testDescription + " ожидается статус 403 Forbidden");
        assertEquals(expectedMessage, message, "Для случая " + testDescription + " ожидается сообщение о обязательных полях");
        assertFalse(isUserCreated, "Для случая " + testDescription + " ожидается сообщение об ошибке");
    }
}
