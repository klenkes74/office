/*
 * Copyright Kaiserpfalz EDV-Service, Roland T. Lichti , 2019. All rights reserved.
 *
 *  This file is part of Kaiserpfalz EDV-Service Office.
 *
 *  This is free software: you can redistribute it and/or modify it under the terms of
 *  the GNU Lesser General Public License as published by the Free Software
 *  Foundation, either version 3 of the License.
 *
 *  This file is distributed in the hope that it will be useful, but WITHOUT ANY
 *  WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 *  License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License along
 *  with this file. If not, see <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package de.kaiserpfalzedv.office.application;

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
import static org.hamcrest.Matchers.*;

@QuarkusTest
@Tag("integration")
public class ServiceTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceTest.class);

    @Test
    public void shouldReturnCorrectFolderWhenGivenTheUuid() {
        given()
                .when()
                .auth().preemptive().basic("scott", "jb0ss")
                .get("/folders/3ca1aa42-4ae0-4066-ae5b-1ab2d1eab7f8")
                .then()
                .statusCode(200);
    }

    @Test
    public void shouldReturnNotFoundWhenGivenTheWrongUuid() {
        given()
                .when()
                .auth().preemptive().basic("scott", "jb0ss")
                .get("/folders/00000000-0000-0000-0000-000000000000")
                .then()
                .statusCode(404);
    }

    @Test
    public void shouldReturnNotFoundWhenGivenAnUnknownScopeAndKey() {
        given()
                .pathParam("scope", "no-scope")
                .pathParam("key", "not-there")
                .when()
                .auth().preemptive().basic("scott", "jb0ss")
                .get("/folders/{scope}/{key}")
                .then()
                .statusCode(404);
    }

    @Test
    public void shouldReturnCorrectFolderWhenGivenTheCorrectScopeAndKey() {
        given()
                .pathParam("scope", "de.kaiserpfalz-edv")
                .pathParam("key", "I-19-0001")
                .when()
                .auth().preemptive().basic("scott", "jb0ss")
                .get("/folders/{scope}/{key}")
                .then()
                .statusCode(200);
    }

    @Test
    public void shouldCreateANewFolderWithValidData() throws IOException {
        Path path = Paths.get("target/test-classes/json/create_folder.json");
        String body = new String(Files.readAllBytes(path));
        assert !body.isEmpty();

        given()
                .when()
                .header("content-type", MediaType.APPLICATION_JSON)
                .auth().preemptive().basic("scott", "jb0ss")
                .body(body)
                .put("/folders")
                .then()
                .statusCode(allOf(greaterThanOrEqualTo(200), lessThan(300)));
    }

    @Test
    public void shouldReturnFailureWhenCreatingADoublette() throws IOException {
        Path path = Paths.get("target/test-classes/json/create_doublette.json");
        String body = new String(Files.readAllBytes(path));
        assert !body.isEmpty();

        given()
                .when()
                .header("content-type", MediaType.APPLICATION_JSON)
                .auth().preemptive().basic("scott", "jb0ss")
                .body(body)
                .put("/folders")
                .then()
                .statusCode(409);
    }

    @Test
    public void shouldReturnHealthWhenCallingReady() {
        given()
                .when()
                .auth().preemptive().basic("scott", "jb0ss")
                .get("/health/ready")
                .then()
                .statusCode(200);
    }

    @Test
    public void shouldReturnHealthWhenCallingLive() {
        given()
                .when()
                .auth().preemptive().basic("scott", "jb0ss")
                .get("/health/live")
                .then()
                .statusCode(200);
    }
}