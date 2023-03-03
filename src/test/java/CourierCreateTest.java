import java.io.File;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

import api.client.CourierClient;

public class CourierCreateTest {
    private File loginData;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Check correct creation of courier")
    public void correctCreateCourier() {
        File jsonCreateData = new File("src/test/resources/createCourierCorrectDataWhenCreate.json");
        loginData = new File("src/test/resources/createCourierCorrectDataWhenLogin.json");
        CourierClient client = new CourierClient();
        // create courier
        client.getCreateCourierResponseCorrect(jsonCreateData)
                .then()
                .statusCode(201)
                .and()
                .assertThat().body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Check error message for creating existing courier")
    public void whenCreateExistingCourierThenNotOk() {
        File jsonCreateData = new File("src/test/resources/createCourierCorrectDataWhenCreate.json");
        loginData = new File("src/test/resources/createCourierCorrectDataWhenLogin.json");
        CourierClient client = new CourierClient();
        // create courier
        client.getCreateCourierResponseCorrect(jsonCreateData)
                .then()
                .statusCode(201)
                .and()
                .assertThat().body("ok", equalTo(true));
        // try to create existing courier
        client.getCreateCourierResponseWhenTryToCreateExistingCourier(jsonCreateData)
                .then()
                .statusCode(409)
                .and()
                .assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Check error message for create courier without login")
    public void whenCreateWithNoLoginThenNotOk() {
        File json = new File("src/test/resources/createCourierDataWithoutLoginField.json");
        loginData = null;
        CourierClient client = new CourierClient();
        client.getCreateCourierResponseWhenTryToCreateCourierWithoutLogin(json)
                .then()
                .statusCode(400)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Check error message for create courier without password")
    public void whenCreateWithNoPasswordThenNotOk() {
        File json = new File("src/test/resources/createCourierDataWithoutPasswordField.json");
        loginData = null;
        CourierClient client = new CourierClient();
        client.getCreateCourierResponseWhenTryToCreateCourierWithoutPassword(json)
                .then()
                .statusCode(400)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @After
    public void tearDown() throws Exception {
        if (loginData != null) {
            // cleanup steps
            CourierClient client = new CourierClient();
            Response loginCourierResponse = client.getLoginCourierResponseWhenCorrectLogin(loginData);
            loginCourierResponse
                    .then()
                    .statusCode(200)
                    .and()
                    .assertThat().body("id", notNullValue());
            int courierId = client.parseCourierIdFromLoginCourierResponse(loginCourierResponse);
            client.getDeleteCourierResponseWhenCorrectDeletion(courierId)
                    .then()
                    .statusCode(200);
        }
    }
}
