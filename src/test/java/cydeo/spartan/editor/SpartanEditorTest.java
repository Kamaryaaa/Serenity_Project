package cydeo.spartan.editor;

import io.cucumber.java.af.En;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import utilities.SpartanUtil;

import io.restassured.RestAssured.*;
import io.restassured.http.ContentType;
import net.serenitybdd.junit5.SerenityTest;
import net.serenitybdd.rest.Ensure;
import net.serenitybdd.rest.SerenityRest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static net.serenitybdd.rest.SerenityRest.*;

import java.util.LinkedHashMap;
import java.util.Map;

import static net.serenitybdd.rest.SerenityRest.*;
import static org.hamcrest.Matchers.*;

@SerenityTest
public class SpartanEditorTest {


    @BeforeAll
    public static void init(){
        RestAssured.baseURI = "http://54.237.226.155:7000/api";
    }

    @DisplayName("POST/ spartan as editor")
    @Test
    public void postSpartan(){

        Map<String, Object> spartanMap = SpartanUtil.getRandomSpartanMap();
        System.out.println("spartanMap = " + spartanMap);

      given()
              .auth().basic("editor","editor")
              .accept(ContentType.JSON)
              .contentType(ContentType.JSON)
              .body(spartanMap)
              .when()
              .post("/spartans").prettyPeek();

        /**
         * SERIALIZATION --> JAVA to JSON
         * DESERIALIZATION-->JSON to JAVA
         *
         * Do we need to add dependency
         * -with regular rest assured we were adding jackson databind for serialization and deserialization
         * -for Serenity we are not going to add Jackson or GSON dependency
         *
         *  status code is 201
         *                 content type is Json
         *                 success message is A Spartan is Born!
         *                 id is not null
         *                 name is correct
         *                 gender is correct
         *                 phone is correct
         *                 check location header ends with newly generated id
         */


   Ensure.that("Status code is 201", vRes -> vRes.statusCode(201));
   Ensure.that("Content type is Json",vRes ->vRes.contentType(ContentType.JSON));
   Ensure.that("success message is A Spartan is Born!",vRes ->vRes.body("success",is("A Spartan is Born!")));
   Ensure.that("id is not null",vRes -> vRes.body("data.id",is(notNullValue())));
        System.out.println("lastResponse().path(\"id\") = " + lastResponse().path("data.id"));
   Ensure.that("name is correct",vRes ->vRes.body("data.name",is(equalTo(spartanMap.get("name")))));
        System.out.println("lastResponse().path(\"name\") = " + lastResponse().path("data.name"));
        System.out.println("spartanMap.get(\"name\") = " + spartanMap.get("name"));
   Ensure.that("gender is correct",vRes -> vRes.body("data.gender",is(equalTo(spartanMap.get("gender")))));
        System.out.println("lastResponse().path(\"gender\") = " + lastResponse().path("data.gender"));
        System.out.println("spartanMap.get(\"gender\") = " + spartanMap.get("gender"));
   Ensure.that("phone is correct", vRes -> vRes.body("data.phone",is(equalTo(spartanMap.get("phone")))));
        System.out.println("lastResponse().path(\"phone\") = " + lastResponse().path("data.phone"));
        System.out.println("spartanMap.get(\"phone\") = " + spartanMap.get("phone"));



        System.out.println("lastResponse().header(\"Location\") = " + lastResponse().header("Location"));
        Ensure.that("location header ends with newly generated id",
                vRes -> vRes.header("Location",endsWith(lastResponse().jsonPath().getString("data.id"))));
        System.out.println("lastResponse().path(\"id\") = " + lastResponse().path("data.id"));



    }

    @ParameterizedTest(name ="POST Spartan {index} name {0}")
    @CsvFileSource(resources = "/SpartanPost.csv",numLinesToSkip = 1)
    public void spartanPost(String name,String gender,long phone){

        Map<String,Object> spartanMap = new LinkedHashMap<>();
        spartanMap.put("name",name);
        spartanMap.put("gender",gender);
        spartanMap.put("phone",phone);

        System.out.println("spartanMap = " + spartanMap);

        given()
                .accept(ContentType.JSON)
                .auth().basic("admin","admin")
                .contentType(ContentType.JSON)
                .body(spartanMap)
                .when()
                .post("/spartans").prettyPeek();

        Ensure.that("Status code is 201", vRes -> vRes.statusCode(201));
        Ensure.that("Content type is Json",vRes ->vRes.contentType(ContentType.JSON));
        Ensure.that("success message is A Spartan is Born!",vRes ->vRes.body("success",is("A Spartan is Born!")));









    }














}
