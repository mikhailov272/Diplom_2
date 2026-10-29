package ru.praktikum.api;

import io.qameta.allure.Description;
import java.util.List;
import org.junit.Test;
import ru.praktikum.api.model.OrderRequest;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertTrue;

public class OrderWithoutAuthorizationTest extends ApiTest {
    @Test
    @Description("Уточнение эксперта курса от 26.10.2026: для заказа без авторизации допускается ответ 200")
    public void orderWithoutAuthorisationIsCreated() {
        List<String> ids = api.ingredientIds();
        assertTrue("Сервер должен вернуть хотя бы два ингредиента", ids.size() >= 2);
        api.createOrder(null, new OrderRequest(ids.subList(0, 2)))
                .then().statusCode(SC_OK).body("success", equalTo(true))
                .body("order.number", greaterThan(0));
    }
}
