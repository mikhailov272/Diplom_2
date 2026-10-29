package ru.praktikum.api;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import ru.praktikum.api.model.OrderRequest;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import static org.hamcrest.Matchers.*;

public class OrderCreationTest extends RegisteredUserApiTest {
    @Step("Подготовить два существующих ингредиента")
    private List<String> validIngredients() {
        List<String> ids = api.ingredientIds();
        org.junit.Assert.assertTrue("Сервер должен вернуть хотя бы два ингредиента", ids.size() >= 2);
        return ids.subList(0, 2);
    }

    @Test
    public void authorisedOrderWithIngredientsIsCreated() {
        api.createOrder(token, new OrderRequest(validIngredients()))
                .then().statusCode(SC_OK).body("success", equalTo(true))
                .body("order.number", greaterThan(0));
    }

    @Test
    public void emptyIngredientListIsRejected() {
        api.createOrder(token, new OrderRequest(Collections.emptyList()))
                .then().statusCode(SC_BAD_REQUEST).body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    public void missingIngredientFieldIsRejected() {
        api.createOrder(token, new OrderRequest(null)).then().statusCode(SC_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @Description("Уточнение эксперта курса от 12.10.2026: для неверного хеша ожидается 400 вместо 500 из PDF")
    public void invalidIngredientHashIsRejected() {
        api.createOrder(token, new OrderRequest(List.of("invalid-hash")))
                .then().statusCode(SC_BAD_REQUEST).body("success", equalTo(false));
    }
}
