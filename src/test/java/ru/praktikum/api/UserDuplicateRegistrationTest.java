package ru.praktikum.api;

import io.qameta.allure.Description;
import org.junit.Test;
import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.hamcrest.Matchers.equalTo;

public class UserDuplicateRegistrationTest extends RegisteredUserApiTest {
    @Test
    @Description("Повторная регистрация уже созданного пользователя отклоняется")
    public void duplicateUserIsRejected() {
        api.register(user).then().statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }
}
