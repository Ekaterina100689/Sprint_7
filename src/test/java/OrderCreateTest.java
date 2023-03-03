// импортируем RestAssured

import api.client.OrdersClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import api.model.Order;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderCreateTest {

    private final List<String> color;

    @Parameterized.Parameters(name="Цвет самоката. Тестовые данные: {0}.")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { List.of("BLACK", "GREY")},
                { List.of("BLACK")},
                { List.of("GREY")},
                { List.of()},
        });
    }

    public OrderCreateTest(List<String> color) {
        this.color = color;
    }

    // аннотация Before показывает, что метод будет выполняться перед каждым тестовым методом
    @Before
    public void setUp() {
        // повторяющуюся для разных ручек часть URL лучше записать в переменную в методе Before
        // если в классе будет несколько тестов, указывать её придётся только один раз
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Check correct order creation")
    public void correctCreateOrder() {
        Order order = new Order("Naruto", "Uchiha", "Konoha, 142 apt.", "4", "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", color);
        OrdersClient client = new OrdersClient();
        client.getCorrectCreateOrderResponse(order)
                .then()
                .statusCode(201)
                .and()
                .assertThat().body("track", notNullValue());
    }
}
