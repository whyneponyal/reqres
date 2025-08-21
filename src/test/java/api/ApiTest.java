package api;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Description;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.time.Clock;
import java.util.List;

import static io.restassured.RestAssured.given;

public class ApiTest extends BaseApiTest {
    private final String URL = "https://reqres.in/";

    @Test
    @Description("Проверяет заканчиваются ли эмейлы пользователей на @reqres.in и содержатся ли в эмейлах id пользователя")
    public void checkUserListTest() {
        Specification.installation(Specification.requestSpec(URL), Specification.responseSpec(200));
        List<UserData> users = given()
                .when()
                .get("api/users?page=2")
                .then()
                .log().all().extract().body().jsonPath().getList("data", UserData.class);

        Assertions.assertTrue(users.stream().allMatch(x -> x.getEmail().endsWith("@reqres.in")));
        List<String> emails = users.stream().map(UserData::getEmail).toList();
        List<String> ids = users.stream().map(x -> x.getId().toString()).toList();
        for (String id : emails) {
            Assertions.assertTrue(emails.contains(id));
        }
    }


    @Test
    public void getOneUserTest() {
        Specification.installation(Specification.requestSpec(URL), Specification.responseSpec(200));
        Response user = given()
                .when()
                .get("api/users/2")
                .then()
                .log().body().extract().response();
        JsonPath jsonPath = user.jsonPath();
        Integer id = 2;
        Assertions.assertEquals(id, jsonPath.get("data.id"));
    }

    @Test
    public void getUncorrectedUserTest() {
        Specification.installation(Specification.requestSpec(URL), Specification.responseSpec(404));
        Response user = given()
                .when()
                .get("api/users/23")
                .then().log().body().extract().response();
        Integer statusCode = user.statusCode();
        Assertions.assertEquals(Specification.getStatus(404), statusCode);
    }

    @Test
    public void checkSortedYearsTest() {
        Specification.installation(Specification.requestSpec(URL), Specification.responseSpec(200));
        List<Colors> colors = given()
                .when()
                .get("api/unknown")
                .then()
                .log().body().extract().jsonPath().getList("data", Colors.class);

        List<Integer> sortedYears = colors.stream().map(Colors::getYear).sorted().toList();
        List<Integer> years = colors.stream().map(Colors::getYear).toList();
        Assertions.assertEquals(years, sortedYears);
    }

    @Test
    public void getOneColorTest() {
        Specification.installation(Specification.requestSpec(URL), Specification.responseSpec(200));
        Response color = given()
                .when()
                .get("api/unknown/2")
                .then()
                .log().body().extract().response();
        JsonPath jsonPath = color.jsonPath();
        Integer year = 2001;
        Assertions.assertEquals(year, jsonPath.get("data.year"));
    }

    @Test
    public void getUncorrectedColorTest() {
        Specification.installation(Specification.requestSpec(URL), Specification.responseSpec(404));
        Response color = given()
                .when()
                .get("api/unknown/23")
                .then()
                .log().body().extract().response();
        Integer statusCode = color.statusCode();
        Assertions.assertEquals(Specification.getStatus(404), statusCode);
    }

    @Test
    public void checkTimeTest() {
        Specification.installation(Specification.requestSpec(URL), Specification.responseSpec(201));
        EmployeeUser user = new EmployeeUser("morpheus", "leader");
        Response response = given()
                .body(user)
                .when()
                .post("api/users")
                .then().extract().response();
        String time = Clock.systemUTC().instant().toString().replaceAll("(.{11})$", "");
        ;
        String timeFromResponse = response.jsonPath().get("createdAt").toString().replaceAll("(.{5})$", "");
        Assertions.assertEquals(time, timeFromResponse);
    }

    @Test
    public void updateJobTest() {
        Specification.installation(Specification.requestSpec(URL), Specification.responseSpec(200));
        EmployeeUser user = new EmployeeUser("morpheus", "zion resident");
        Response response = given()
                .body(user)
                .when()
                .put("api/users/2")
                .then().extract().response();
        Assertions.assertEquals(user.getJob(), response.jsonPath().get("job"));
    }

    @Test
    public void patchUserInfoTest() {
        Specification.installation(Specification.requestSpec(URL), Specification.responseSpec(200));
        EmployeeUser user = new EmployeeUser("morpheus", "zion resident");
        Response response = given()
                .body(user)
                .when()
                .patch("api/users/2")
                .then().extract().response();
        Assertions.assertEquals(user.getJob(), response.jsonPath().get("job"));
    }

    @Test
    public void removeUserTest() {
        Specification.installation(Specification.requestSpec(URL), Specification.responseSpec(204));
        Integer statusCode = given()
                .when()
                .delete("api/users/2")
                .then().extract().statusCode();
        Assertions.assertEquals(Specification.getStatus(204), statusCode);
    }

    @Test
    public void successesRegTest() {
        Specification.installation(Specification.requestSpec(URL), Specification.responseSpec(200));
        Registration user = new Registration("eve.holt@reqres.in", "pistol");
        SuccessesReg successesReg = given()
                .body(user)
                .when()
                .post("api/register")
                .then().log().body().extract().as(SuccessesReg.class);
        Integer id = 4;
        String token = "QpwL5tke4Pnpja7X4";
        Assertions.assertEquals(id, successesReg.getId());
        Assertions.assertEquals(token, successesReg.getToken());
    }

    @Test
    public void unSuccessesRegTest() {
        Specification.installation(Specification.requestSpec(URL), Specification.responseSpec(400));
        Registration user = new Registration("sydney@fife", null);
        UnSuccessesReg unSuccessesReg = given()
                .body(user)
                .when()
                .post("api/register")
                .then().log().body().extract().as(UnSuccessesReg.class);
        String error = "Missing password";
        Assertions.assertEquals(error, unSuccessesReg.getError());
    }

    @Test
    public void loginTest() {
        Specification.installation(Specification.requestSpec(URL), Specification.responseSpec(200));
        Registration user = new Registration("eve.holt@reqres.in", "cityslicka");
        Response response = given()
                .body(user)
                .when()
                .post("api/login")
                .then().log().body().extract().response();
        String tokenFromResponse = response.jsonPath().get("token");
        String token = "QpwL5tke4Pnpja7X4";
        Assertions.assertEquals(token, tokenFromResponse);
    }

    @Test
    public void failedLoginTest(){
        Specification.installation(Specification.requestSpec(URL), Specification.responseSpec(400));
        Registration user = new Registration("peter@klaven", null);
        Response response = given()
                .body(user)
                .when()
                .post("api/login")
                .then().log().body().extract().response();

        String errorFromResponse = response.jsonPath().get("error");
        String error = "Missing password";

        Integer statusCode = response.statusCode();

        Assertions.assertEquals(error, errorFromResponse);
        Assertions.assertEquals(Specification.getStatus(400), statusCode);
    }

    @Test
    public void checkUsersWithDelayed(){
        Specification.installation(Specification.requestSpec(URL), Specification.responseSpec(200));
        List<UserData> users = given()
                .when()
                .get("api/users?delay=3")
                .then()
                .log().all().extract().body().jsonPath().getList("data", UserData.class);
        Assertions.assertTrue(users.stream().allMatch(x -> x.getEmail().endsWith("@reqres.in")));
        List<String> emails = users.stream().map(UserData::getEmail).toList();
        List<String> ids = users.stream().map(x -> x.getId().toString()).toList();
        for (String id : emails) {
            Assertions.assertTrue(emails.contains(id));
        }
    }
}
