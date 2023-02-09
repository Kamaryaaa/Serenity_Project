package cydeo.spartan.editor;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.serenitybdd.junit5.SerenityTest;
import net.serenitybdd.rest.Ensure;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import utilities.SpartanUtil;

import java.util.LinkedHashMap;
import java.util.Map;

import static net.serenitybdd.rest.SerenityRest.*;
import static org.hamcrest.Matchers.*;

@SerenityTest
public class SpartanEditorTest {

    @BeforeAll
    public static void init(){
        RestAssured.baseURI = "http://54.86.2.212:7000/api";
    }

    @DisplayName("POST /spartan as editor")
    @Test
    public void postSpartan(){
        Map<String, Object> spartanMap = SpartanUtil.getRandomSpartanMap();
        System.out.println("spartanMap = " + spartanMap);

        given().auth().basic("editor","editor")
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(spartanMap)//SERIALIZATION
        .when()
                .post("/spartans").prettyPeek();

        /*
            SERIALIZATION --> JAVA to JSON
            DESERIALIZATION --> JSON TO JAVA

            DO we need to add dependency ?
            -With regular Rest Assured we were adding JACKSON Databind for Serialization and Deserialization
            -For Serenity we are not gonna add Jackson or GSON dependency.

                status code is 201
                content type is Json
                success message is A Spartan is Born!
                id is not null
                name is correct
                gender is correct
                phone is correct

                check location header ends with newly generated id

         */

        //status code is 201
        Ensure.that("Status code is 201",vRes->vRes.statusCode(201));

        //content type is Json
        Ensure.that("Content Type is JSON",vRes->vRes.contentType(ContentType.JSON));

        //success message is A Spartan is Born!
        Ensure.that("Success Message is A Spartan is Born!",vRes->vRes.body("success",is("A Spartan is Born!")));

        //id is not null
        Ensure.that("ID is Not Null",vRes->vRes.body("data.id",notNullValue()));
        //name is correct
        Ensure.that("Name is Correct",vRes->vRes.body("data.name",is(spartanMap.get("name"))));

        //gender is correct
        Ensure.that("Gender is Correct",vRes->vRes.body("data.gender",is(spartanMap.get("gender"))));

        //phone is correct
        Ensure.that("Phone is Correct",vRes->vRes.body("data.phone",is(spartanMap.get("phone"))));

        //check location header ends with newly generated id
        String id = lastResponse().jsonPath().getString("data.id");

        Ensure.that("check location header ends with newly generated id",vRes->vRes.header("Location",endsWith(id)));
    }
    //{index} --> will shown in screen based on number of iteration
    //{0}--> it refers name
    //{1} --> it refers gender
    //{2} --> it refers phone

    @ParameterizedTest(name = "POST Spartan {index} name {0}")
    @CsvFileSource(resources = "/SpartanPOST.csv",numLinesToSkip = 1)
    public void POSTSpartan(String name, String gender, long phone){
        System.out.println("name = " + name);
        System.out.println("gender = " + gender);
        System.out.println("phone = " + phone);

        Map<String,Object> spartanMap = new LinkedHashMap<>();
        spartanMap.put("name",name);
        spartanMap.put("gender",gender);
        spartanMap.put("phone",phone);

        given().auth().basic("editor","editor")
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(spartanMap)//SERIALIZATION
                .when()
                .post("/spartans").prettyPeek();

        //status code is 201
        Ensure.that("Status code is 201",vRes->vRes.statusCode(201));

        //content type is Json
        Ensure.that("Content Type is JSON",vRes->vRes.contentType(ContentType.JSON));

        //success message is A Spartan is Born!
        Ensure.that("Success Message is A Spartan is Born!",vRes->vRes.body("success",is("A Spartan is Born!")));

    }


}
