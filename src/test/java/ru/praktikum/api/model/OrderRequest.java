package ru.praktikum.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderRequest {
    private final List<String> ingredients;

    public OrderRequest(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getIngredients() { return ingredients; }
}
