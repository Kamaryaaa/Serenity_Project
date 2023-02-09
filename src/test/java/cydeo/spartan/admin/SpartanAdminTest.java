package cydeo.spartan.admin;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.serenitybdd.junit5.SerenityTest;
import net.serenitybdd.rest.Ensure;
import net.serenitybdd.rest.SerenityRest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static net.serenitybdd.rest.SerenityRest.*;
import static org.hamcrest.Matchers.is;


@SerenityTest
public class SpartanAdminTest {

    @BeforeAll
    public static void init(){
        RestAssured.baseURI = "http://54.86.2.212:7000/api";
    }

    @DisplayName("GET /spartans with PURE REST ASSURED")
    @Test
    public void test1(){
        RestAssured.given().accept(ContentType.JSON)
                .auth().basic("admin","admin")
            .when().get("/spartans")
                .then().statusCode(200).contentType(ContentType.JSON);

    }

    @DisplayName("GET /spartans with SERENITY REST")
    @Test
    public void test2(){
      SerenityRest.given().accept(ContentType.JSON)
                .auth().basic("admin","admin")
                .when().get("/spartans")
                .then().statusCode(200).contentType(ContentType.JSON);

      //lastResponse() --> response --> Serenity Rest will generate after sending request
        //and store response information without saving in a variable
        System.out.println("lastResponse().statusCode() = " + lastResponse().statusCode());

        System.out.println("lastResponse().path(\"id[0]\") = " + lastResponse().path("id[0]"));

        System.out.println("lastResponse().jsonPath().getString(\"name[0]\") = " + lastResponse().jsonPath().getString("name[0]"));

    }

    @DisplayName("GET /spartans/{id} with SERENITY REST")
    @Test
    public void test3(){
        SerenityRest.given().accept(ContentType.JSON)
                .auth().basic("admin","admin")
                .pathParam("id",10)
                .when().get("/spartans/{id}");

        //lastResponse() --> response --> Serenity Rest will generate after sending request
        //and store response information without saving in a variable

        //ASSERTIONS IN SERENITY
        /*
            Ensure.that -->it is method that comes from Serenity to put assertions into Serenity
         */

        Ensure.that("Status code is 200",vRes -> vRes.statusCode(200));
        Ensure.that("Status code is 200",then -> then.statusCode(200));
        Ensure.that("Status code is 200",x -> x.statusCode(200));

        //Ensure that content type is Json
        Ensure.that("Content-type is JSON",vRes->vRes.contentType(ContentType.JSON));
        //Ensure that id is 10
        Ensure.that("Spartan ID is 10",vRes -> vRes.body("id",is(10)));
        //Ensure that Transfer-Encoding: chunked
        Ensure.that("Header:Transfer-Encoding is chunked",then -> then.header("Transfer-Encoding",is("chunked")));

    }

}
