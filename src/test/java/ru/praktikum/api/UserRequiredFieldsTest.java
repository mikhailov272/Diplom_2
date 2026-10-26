package ru.praktikum.api;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
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
        Map<String, String> user = uniqueUser();
        if (omit) {
            user.remove(field);
        } else {
            user.put(field, "");
        }
        registerAndRemember(user).then().statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
}
