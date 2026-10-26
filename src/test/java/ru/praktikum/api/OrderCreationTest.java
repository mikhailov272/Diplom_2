package ru.praktikum.api;

import io.qameta.allure.Description;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import static org.hamcrest.Matchers.*;

public class OrderCreationTest extends ApiTest {
    private List<String> validIngredients() {
        List<String> ids = api.ingredientIds();
        org.junit.Assert.assertTrue("Сервер должен вернуть хотя бы два ингредиента", ids.size() >= 2);
        return ids.subList(0, 2);
    }

    @Test
    public void authorisedOrderWithIngredientsIsCreated() {
        createUser(uniqueUser());
        api.createOrder(token, Collections.singletonMap("ingredients", validIngredients()))
                .then().statusCode(200).body("success", equalTo(true))
                .body("order.number", greaterThan(0));
    }

    @Test
    @Description("Уточнение эксперта курса от 26.10.2026: для заказа без авторизации допускается ответ 200")
    public void orderWithoutAuthorisationIsCreated() {
        api.createOrder(null, Collections.singletonMap("ingredients", validIngredients()))
                .then().statusCode(200).body("success", equalTo(true))
                .body("order.number", greaterThan(0));
    }

    @Test
    public void emptyIngredientListIsRejected() {
        createUser(uniqueUser());
        api.createOrder(token, Collections.singletonMap("ingredients", Collections.emptyList()))
                .then().statusCode(400).body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    public void missingIngredientFieldIsRejected() {
        createUser(uniqueUser());
        api.createOrder(token, Collections.emptyMap()).then().statusCode(400)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @Description("Уточнение эксперта курса от 12.10.2026: для неверного хеша ожидается 400 вместо 500 из PDF")
    public void invalidIngredientHashIsRejected() {
        createUser(uniqueUser());
        api.createOrder(token, Collections.singletonMap("ingredients", List.of("invalid-hash")))
                .then().statusCode(400).body("success", equalTo(false));
    }
}
