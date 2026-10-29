package ru.praktikum.api;

import static org.apache.http.HttpStatus.SC_OK;
import io.qameta.allure.Description;
import ru.praktikum.api.model.UserRequest;
import org.junit.Test;
import static org.hamcrest.Matchers.*;

public class UserRegistrationTest extends ApiTest {
    @Test
    @Description("Уникальный пользователь регистрируется и получает токены и свои данные")
    public void createUniqueUser() {
        UserRequest user = uniqueUser();
        registerAndRemember(user).then().statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("user.email", equalTo(user.getEmail()))
                .body("user.name", equalTo(user.getName()))
                .body("accessToken", startsWith("Bearer "))
                .body("refreshToken", not(isEmptyOrNullString()));
    }

}
