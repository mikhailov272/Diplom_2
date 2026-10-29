package ru.praktikum.api;

import org.junit.Test;
import ru.praktikum.api.model.UserRequest;
import ru.praktikum.api.model.LoginRequest;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.*;

public class UserLoginTest extends ApiTest {
    @Test
    public void existingUserCanLogIn() {
        UserRequest user = uniqueUser();
        createUser(user);
        LoginRequest credentials = new LoginRequest(user.getEmail(), user.getPassword());
        api.login(credentials).then().statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("user.email", equalTo(user.getEmail()))
                .body("user.name", equalTo(user.getName()))
                .body("accessToken", startsWith("Bearer "))
                .body("refreshToken", not(isEmptyOrNullString()));
    }
}
