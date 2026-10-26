package ru.praktikum.api;

import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.junit.After;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.isEmptyOrNullString;

public abstract class ApiTest {
    protected final ApiClient api = new ApiClient();
    protected String token;

    protected Map<String, String> uniqueUser() {
        Map<String, String> user = new HashMap<>();
        user.put("email", "qa-" + UUID.randomUUID() + "@example.com");
        user.put("password", "TestPassword123");
        user.put("name", "Test User");
        return user;
    }

    protected Response registerAndRemember(Map<String, String> user) {
        Response response = api.register(user);
        token = response.jsonPath().getString("accessToken");
        return response;
    }

    protected void createUser(Map<String, String> user) {
        registerAndRemember(user).then().statusCode(200)
                .body("success", equalTo(true))
                .body("accessToken", not(isEmptyOrNullString()));
    }

    @After
    public void deleteOnlyCreatedTestUser() {
        if (token != null) {
            api.deleteUser(token).then().statusCode(202).body("success", equalTo(true));
        }
    }
}
