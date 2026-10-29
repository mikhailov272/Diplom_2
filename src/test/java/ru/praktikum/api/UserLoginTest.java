package ru.praktikum.api;

import java.util.Arrays;
import java.util.Collection;
import ru.praktikum.api.model.UserRequest;
import ru.praktikum.api.model.LoginRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.hamcrest.Matchers.*;

@RunWith(Parameterized.class)
public class UserLoginTest extends ApiTest {
    private final boolean correctEmail;
    private final boolean correctPassword;

    public UserLoginTest(boolean correctEmail, boolean correctPassword) {
        this.correctEmail = correctEmail;
        this.correctPassword = correctPassword;
    }

    @Parameterized.Parameters(name = "correct email={0}, correct password={1}")
    public static Collection<Object[]> credentials() {
        return Arrays.asList(new Object[][] {
            {true, true}, {false, true}, {true, false}, {false, false}
        });
    }

    @Test
    public void loginChecksEmailAndPassword() {
        UserRequest user = uniqueUser();
        createUser(user);
        LoginRequest credentials = new LoginRequest(
                correctEmail ? user.getEmail() : "wrong-" + user.getEmail(),
                correctPassword ? user.getPassword() : "WrongPassword123");
        if (correctEmail && correctPassword) {
            api.login(credentials).then().statusCode(200)
                    .body("success", equalTo(true))
                    .body("user.email", equalTo(user.getEmail()))
                    .body("user.name", equalTo(user.getName()))
                    .body("accessToken", startsWith("Bearer "))
                    .body("refreshToken", not(isEmptyOrNullString()));
        } else {
            api.login(credentials).then().statusCode(401)
                    .body("success", equalTo(false))
                    .body("message", equalTo("email or password are incorrect"));
        }
    }
}
