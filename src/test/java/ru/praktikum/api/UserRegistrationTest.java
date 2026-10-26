package ru.praktikum.api;

import io.qameta.allure.Description;
import java.util.Map;
import org.junit.Test;
import static org.hamcrest.Matchers.*;

public class UserRegistrationTest extends ApiTest {
    @Test
    @Description("Уникальный пользователь регистрируется и получает токены и свои данные")
    public void createUniqueUser() {
        Map<String, String> user = uniqueUser();
        registerAndRemember(user).then().statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(user.get("email")))
                .body("user.name", equalTo(user.get("name")))
                .body("accessToken", startsWith("Bearer "))
                .body("refreshToken", not(isEmptyOrNullString()));
    }

    @Test
    @Description("Повторная регистрация уже созданного пользователя отклоняется")
    public void duplicateUserIsRejected() {
        Map<String, String> user = uniqueUser();
        createUser(user);
        api.register(user).then().statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }
}
