package ru.praktikum.api;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_ACCEPTED;
import io.restassured.response.Response;
import io.qameta.allure.Step;
import ru.praktikum.api.model.UserRequest;
import java.util.UUID;
import org.junit.After;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.isEmptyOrNullString;

public abstract class ApiTest {
    protected final ApiClient api = new ApiClient();
    protected String token;

    @Step("Подготовить уникальные данные пользователя")
    protected UserRequest uniqueUser() {
        return new UserRequest("qa-" + UUID.randomUUID() + "@example.com",
                "TestPassword123", "Test User");
    }

    @Step("Зарегистрировать пользователя и сохранить токен для уборки")
    protected Response registerAndRemember(UserRequest user) {
        Response response = api.register(user);
        token = response.jsonPath().getString("accessToken");
        return response;
    }

    @Step("Создать пользователя для проверки")
    protected void createUser(UserRequest user) {
        registerAndRemember(user).then().statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("accessToken", not(isEmptyOrNullString()));
    }

    @After
    @Step("Удалить пользователя, созданного в текущем тесте")
    public void deleteOnlyCreatedTestUser() {
        if (token != null) {
            api.deleteUser(token).then().statusCode(SC_ACCEPTED).body("success", equalTo(true));
        }
    }
}
