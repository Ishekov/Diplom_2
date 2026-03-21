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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DisplayName("Авторизация без обязательного поля password или email")
public class LoginWithInvalidDataTest {
    private static final Faker faker = new Faker();
    private UserApi userApi;
    private UserCreate newUser;
    private String accessToken;

    static Stream<Arguments> userInvalidData() {
        UserCreate baseUser = UserGeneratorFaker.userGeneratorFaker();
        return Stream.of(
                Arguments.of(
                        new UserCreate(
                        "wrong@email.com",
                        baseUser.getPassword(),
                        baseUser.getName()),
                        "Неверный email"),

                Arguments.of(
                        new UserCreate(
                        baseUser.getEmail(),
                        "wrongPassword",
                        baseUser.getName()),
                        "Неверный пароль"),

                Arguments.of(
                        new UserCreate(
                        null,
                        baseUser.getPassword(),
                        baseUser.getName()),
                        "null email"),

                Arguments.of(
                        new UserCreate(
                        baseUser.getEmail(),
                        null,
                        baseUser.getName()),
                        "null пароль")
        );
    }

    @BeforeEach
    public void init() {
        userApi = new UserApi();
        newUser = UserGeneratorFaker.userGeneratorFaker();
    }


    @ParameterizedTest(name = "{index}: {1}")
    @MethodSource("userInvalidData")
    @DisplayName("Авторизоваться и не заполнить одно из обязательных полей")
    public void loginWithInvalidDataTest(UserCreate userInvalidData, String testDescription){
        ValidatableResponse resp = userApi.createUser(newUser);
        ValidatableResponse loginResp = userApi.loginUser(Authorization.from(userInvalidData));

        accessToken = loginResp.extract().path("accessToken");
        int statusCode = loginResp.extract().statusCode();
        String message = loginResp.extract().path("message");
        String expected = "email or password are incorrect";
        boolean isUserCreated = loginResp.extract().path("success");


        assertEquals(SC_UNAUTHORIZED, statusCode, "Для случая " + testDescription + " ожидается статус 401 Unauthorized");
        assertEquals(expected, message, "Для случая " + testDescription + " ожидается сообщение о обязательных полях");
        assertFalse(isUserCreated, "Для случая " + testDescription + " ожидается сообщение об ошибке");




    }

    @AfterEach
    public void tearDown() {
        if (accessToken != null && !accessToken.isEmpty()) {
            userApi.deleteUser(accessToken);
        }
    }
}
