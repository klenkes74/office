package de.kaiserpfalzedv.office.folders;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class ServiceTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/folders/kes")
          .then()
             .statusCode(200);
    }

}