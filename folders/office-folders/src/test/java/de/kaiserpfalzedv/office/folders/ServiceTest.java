package de.kaiserpfalzedv.office.folders;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class ServiceTest {

    @Test
    public void shouldGetHTTP200WithAValidRequest() {
        given()
                .when().get("/folders/kes")
                .then()
                .statusCode(200);
    }

}