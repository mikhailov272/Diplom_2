package ru.praktikum.api;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
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
        Map<String, String> user = uniqueUser();
        createUser(user);
        Map<String, String> credentials = new HashMap<>();
        credentials.put("email", correctEmail ? user.get("email") : "wrong-" + user.get("email"));
        credentials.put("password", correctPassword ? user.get("password") : "WrongPassword123");
        if (correctEmail && correctPassword) {
            api.login(credentials).then().statusCode(200)
                    .body("success", equalTo(true))
                    .body("user.email", equalTo(user.get("email")))
                    .body("user.name", equalTo(user.get("name")))
                    .body("accessToken", startsWith("Bearer "))
                    .body("refreshToken", not(isEmptyOrNullString()));
        } else {
            api.login(credentials).then().statusCode(401)
                    .body("success", equalTo(false))
                    .body("message", equalTo("email or password are incorrect"));
        }
    }
}
