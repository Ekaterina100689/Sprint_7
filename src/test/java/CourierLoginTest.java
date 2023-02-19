import java.io.File;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

import api.client.CourierClient;

public class CourierLoginTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    // курьер может авторизоваться
    // для авторизации нужно передать все обязательные поля
    // успешный запрос возвращает id
    @Test
    @DisplayName("Check correct log in of courier")
    public void correctLoginCourier() {
        File json = new File("src/test/resources/loginCourierDataCorrect.json");
        CourierClient client = new CourierClient();
        // create courier
        client.getCreateCourierResponseCorrect(json)
                .then()
                .statusCode(201)
                .and()
                .assertThat().body("ok", equalTo(true));
        // login courier
        Response loginCourierResponse = client.getLoginCourierResponseWhenCorrectLogin(json);
        loginCourierResponse
                .then()
                .statusCode(200)
                .and()
                .assertThat().body("id", notNullValue());
        // delete courier
        int courierId = client.parseCourierIdFromLoginCourierResponse(loginCourierResponse);
        client.getDeleteCourierResponseWhenCorrectDeletion(courierId)
                .then()
                .statusCode(200);
    }


    // система вернёт ошибку, если неправильно указать логин или пароль
    // если какого-то поля нет, запрос возвращает ошибку
    // если авторизоваться под несуществующим пользователем, запрос возвращает ошибку;
    @Test
    @DisplayName("Check error message for courier log in without login field")
    public void whenTryToLoginWithoutLoginFieldThenNotOk() {
        File json = new File("src/test/resources/loginCourierDataWithoutLoginField.json");
        CourierClient client = new CourierClient();
        // try to log in without "login" field
        client.getLoginCourierResponseWhenTryToLoginWithoutLoginField(json)
                .then()
                .statusCode(400)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Check error message for courier log in with not existing login field")
    public void whenTryToLoginWithNotExistingLoginThenNotOk() {
        File json = new File("src/test/resources/loginCourierDataNotExistingLogin.json");
        CourierClient client = new CourierClient();
        client.getLoginCourierResponseWhenTryToLoginWithNotExistingCredentials(json)
                .then()
                .statusCode(404)
                .and()
                .assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Check error message for courier log in with not valid password")
    public void whenTryToLoginWithNotCorrectPasswordThenNotOk() {
        File jsonNotCorrectData = new File("src/test/resources/loginCourierDataNotCorrect.json");
        File jsonCorrectData = new File("src/test/resources/loginCourierDataCorrect.json");
        CourierClient client = new CourierClient();
        // create courier
        client.getCreateCourierResponseCorrect(jsonCorrectData)
                .then()
                .statusCode(201)
                .and()
                .assertThat().body("ok", equalTo(true));
        // try to log in with not correct password
        client.getLoginCourierResponseWhenTryToLoginWithNotExistingCredentials(jsonNotCorrectData)
                .then()
                .statusCode(404)
                .and()
                .assertThat().body("message", equalTo("Учетная запись не найдена"));
        // log in courier with correct data
        Response loginCourierResponse = client.getLoginCourierResponseWhenCorrectLogin(jsonCorrectData);
        loginCourierResponse
                .then()
                .statusCode(200)
                .and()
                .assertThat().body("id", notNullValue());
        // delete courier
        int courierId = client.parseCourierIdFromLoginCourierResponse(loginCourierResponse);
        client.getDeleteCourierResponseWhenCorrectDeletion(courierId)
                .then()
                .statusCode(200);
    }

}
