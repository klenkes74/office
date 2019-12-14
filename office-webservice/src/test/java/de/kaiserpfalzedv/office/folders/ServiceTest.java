package de.kaiserpfalzedv.office.folders;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;

@QuarkusTest
@Tag("integration")
public class ServiceTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceTest.class);

    @Test
    public void shouldGetHTTP200WithAValidRequest() throws IOException {
        Path path = Paths.get("target/test-classes/json/create_folder.json");
        String body = new String(Files.readAllBytes(path));
        assert !body.isEmpty();

        given()
                .when()
                .header("content-type", MediaType.APPLICATION_JSON)
                .auth().basic("scott", "jb0ss")
                .body(body)
                .put("/folders")
                .then()
                .statusCode(200);
    }

}