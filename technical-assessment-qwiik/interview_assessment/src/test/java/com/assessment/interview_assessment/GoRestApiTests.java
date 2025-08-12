package com.assessment.interview_assessment;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.http.ContentType;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class GoRestApiTests {

    private String accessToken = "21cd5cd190cafbd4f089ab8c457c5719f750d2d2717211b89a4435e5971e942b";
    private int createdUserId;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://gorest.co.in/public/v2";
    }

    @Test(priority = 1)
    public void createUser() {
        
        String userJson = "{"
                + "\"name\": \"Test User\","
                + "\"gender\": \"male\","
                + "\"email\": \"testuser" + System.currentTimeMillis() + "@example.com\","
                + "\"status\": \"active\""
                + "}";

        // Send POST request to create user
        Response response = given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(userJson)
            .when()
                .post("/users")
            .then()
                .statusCode(201)
                .body("name", equalTo("Test User"))
                .extract()
                .response();

        // Get the user ID from response for later use
        createdUserId = response.path("id");

        System.out.println("User created successfully! ID: " + createdUserId);
        System.out.println("Response Body: " + response.asPrettyString());
    }

    @Test(priority = 2, dependsOnMethods = "createUser")
    public void getUserDetails() {
        // Fetch user details by ID
        Response response = given()
                .header("Authorization", "Bearer " + accessToken)
            .when()
                .get("/users/" + createdUserId)
            .then()
                .statusCode(200)
                .body("id", equalTo(createdUserId))
                .extract()
                .response();

        System.out.println("Got user details for ID: " + createdUserId);
        System.out.println("Response Body: " + response.asPrettyString());
    }

    @Test(priority = 3, dependsOnMethods = "getUserDetails")
    public void updateUserDetails() {
        // JSON payload to update user status and name
        String updateJson = "{"
                + "\"name\": \"Updated User\","
                + "\"status\": \"inactive\""
                + "}";

        // Send PUT request to update user info
        Response response = given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(updateJson)
            .when()
                .put("/users/" + createdUserId)
            .then()
                .statusCode(200)
                .body("name", equalTo("Updated User"))
                .body("status", equalTo("inactive"))
                .extract()
                .response();

        System.out.println("User updated successfully! ID: " + createdUserId);
        System.out.println("Response Body: " + response.asPrettyString());
    }
}
