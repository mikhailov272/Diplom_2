package ru.praktikum.api;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import java.util.Arrays;
import java.util.Collection;
import ru.praktikum.api.model.UserRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
public class UserRequiredFieldsTest extends ApiTest {
    private final String field;
    private final boolean omit;

    public UserRequiredFieldsTest(String field, boolean omit) {
        this.field = field;
        this.omit = omit;
    }

    @Parameterized.Parameters(name = "field={0}, omitted={1}")
    public static Collection<Object[]> fields() {
        return Arrays.asList(new Object[][] {
            {"email", true}, {"password", true}, {"name", true},
            {"email", false}, {"password", false}, {"name", false}
        });
    }

    @Test
    public void registrationRequiresEveryMandatoryField() {
        UserRequest user = uniqueUser();
        String value = omit ? null : "";
        switch (field) {
            case "email": user.setEmail(value); break;
            case "password": user.setPassword(value); break;
            case "name": user.setName(value); break;
            default: throw new IllegalArgumentException("Неизвестное поле: " + field);
        }
        registerAndRemember(user).then().statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
}
