package ru.praktikum.api;

import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.praktikum.api.model.UserRequest;
import ru.praktikum.api.model.LoginRequest;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
public class UserInvalidCredentialsTest extends ApiTest {
    private final boolean correctEmail;
    private final boolean correctPassword;

    public UserInvalidCredentialsTest(boolean correctEmail, boolean correctPassword) {
        this.correctEmail = correctEmail;
        this.correctPassword = correctPassword;
    }

    @Parameterized.Parameters(name = "correct email={0}, correct password={1}")
    public static Collection<Object[]> credentials() {
        return Arrays.asList(new Object[][] {
            {false, true}, {true, false}, {false, false}
        });
    }

    @Test
    public void incorrectCredentialsAreRejected() {
        UserRequest user = uniqueUser();
        createUser(user);
        LoginRequest credentials = new LoginRequest(
                correctEmail ? user.getEmail() : "wrong-" + user.getEmail(),
                correctPassword ? user.getPassword() : "WrongPassword123");
        api.login(credentials).then().statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}
