package ru.praktikum.api;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.List;
import ru.praktikum.api.model.UserRequest;
import ru.praktikum.api.model.LoginRequest;
import ru.praktikum.api.model.OrderRequest;
import static io.restassured.RestAssured.given;

public class ApiClient {
    private final RequestSpecification spec;

    public ApiClient() {
        int timeout = Integer.getInteger("api.timeout.ms", 10000);
        spec = new RequestSpecBuilder()
                .setBaseUri(System.getProperty("base.url", "https://qa-stellarburgers.education-services.ru"))
                .setContentType(ContentType.JSON)
                .setConfig(RestAssuredConfig.config().httpClient(HttpClientConfig.httpClientConfig()
                        .setParam("http.connection.timeout", timeout)
                        .setParam("http.socket.timeout", timeout)))
                .addFilter(new AllureRestAssured())
                .build();
    }

    @Step("Зарегистрировать пользователя")
    public Response register(UserRequest user) {
        return given().spec(spec).body(user).post("/api/auth/register");
    }

    @Step("Войти с учётными данными пользователя")
    public Response login(LoginRequest credentials) {
        return given().spec(spec).body(credentials).post("/api/auth/login");
    }

    @Step("Удалить созданного тестового пользователя")
    public Response deleteUser(String token) {
        return given().spec(spec).header("Authorization", token).delete("/api/auth/user");
    }

    @Step("Получить идентификаторы ингредиентов")
    public List<String> ingredientIds() {
        return given().spec(spec).get("/api/ingredients")
                .then().statusCode(200).extract().jsonPath().getList("data._id");
    }

    @Step("Создать заказ")
    public Response createOrder(String token, OrderRequest body) {
        RequestSpecification request = given().spec(spec).body(body);
        if (token != null) {
            request.header("Authorization", token);
        }
        return request.post("/api/orders");
    }
}
