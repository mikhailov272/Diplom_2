package ru.praktikum.api;

import io.qameta.allure.Step;
import org.junit.Before;
import ru.praktikum.api.model.UserRequest;

public abstract class RegisteredUserApiTest extends ApiTest {
    protected UserRequest user;

    @Before
    @Step("Создать пользователя перед тестом")
    public void createTestUser() {
        user = uniqueUser();
        createUser(user);
    }
}
