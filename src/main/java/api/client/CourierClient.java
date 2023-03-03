package api.client;

import java.io.File;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.RestAssured;
import api.model.CourierId;
import static io.restassured.RestAssured.given;

public class CourierClient {

    private static final String BASE_URI = "http://qa-scooter.praktikum-services.ru";
    private static final String COURIER_URI_SUBPATH = "/api/v1/courier";
    private static final String COURIER_LOGIN_URI_SUBPATH = "/api/v1/courier/login";

    private Response getCreateCourierResponse(File body) {
        RestAssured.baseURI = BASE_URI;
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(body)
                .when()
                .post(COURIER_URI_SUBPATH);
    }

    @Step("Get response for correct courier create")
    public Response getCreateCourierResponseCorrect(File body) {
        return getCreateCourierResponse(body);
    }

    @Step("Get response for incorrect courier create, when try to create existing courier")
    public Response getCreateCourierResponseWhenTryToCreateExistingCourier(File body) {
        return getCreateCourierResponse(body);
    }

    @Step("Get response for incorrect courier create, when try to create courier without login")
    public Response getCreateCourierResponseWhenTryToCreateCourierWithoutLogin(File body) {
        return getCreateCourierResponse(body);
    }

    @Step("Get response for incorrect courier create, when try to create courier without password")
    public Response getCreateCourierResponseWhenTryToCreateCourierWithoutPassword(File body) {
        return getCreateCourierResponse(body);
    }

    private Response getLoginCourierResponse(File body) {
        RestAssured.baseURI = BASE_URI;
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(body)
                .when()
                .post(COURIER_LOGIN_URI_SUBPATH);
    }

    @Step("Get response for correct courier login")
    public Response getLoginCourierResponseWhenCorrectLogin(File body) {
        return getLoginCourierResponse(body);
    }

    @Step("Get response for incorrect courier login, try to login courier without login field")
    public Response getLoginCourierResponseWhenTryToLoginWithoutLoginField(File body) {
        return getLoginCourierResponse(body);
    }

    @Step("Get response for incorrect courier login, try to login courier with not existing credentials")
    public Response getLoginCourierResponseWhenTryToLoginWithNotExistingCredentials(File body) {
        return getLoginCourierResponse(body);
    }

    @Step("Parse courier id from response of courier's login")
    public int parseCourierIdFromLoginCourierResponse(Response response) {
        CourierId courierId = response.body().as(CourierId.class);
        return courierId.getId();
    }

    private Response getDeleteCourierResponse(int id) {
        RestAssured.baseURI = BASE_URI;
        return given()
                .when()
                .delete(COURIER_URI_SUBPATH + "/" + Integer.toString(id));
    }

    @Step("Get response for delete courier request, when correct courier deletion")
    public Response getDeleteCourierResponseWhenCorrectDeletion(int id) {
        return getDeleteCourierResponse(id);
    }

}
