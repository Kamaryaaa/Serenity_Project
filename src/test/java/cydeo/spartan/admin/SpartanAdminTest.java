package cydeo.spartan.admin;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.serenitybdd.junit5.SerenityTest;
import net.serenitybdd.rest.SerenityRest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


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

    }

}
