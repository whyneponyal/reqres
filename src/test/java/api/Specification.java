package api;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class Specification {
    public static RequestSpecification requestSpec(String url){
        return new RequestSpecBuilder().setBaseUri(url)
                .setContentType(ContentType.JSON).build().header("x-api-key", "reqres-free-v1");
    }

    public static ResponseSpecification responseSpec(Integer status){
        return new ResponseSpecBuilder().expectStatusCode(status).build();
    }

    public static Integer getStatus(Integer status){
        return status;
    }


    public static void installation(RequestSpecification request,
                                    ResponseSpecification response){
        RestAssured.requestSpecification = request;
        RestAssured.responseSpecification = response;
    }
}
