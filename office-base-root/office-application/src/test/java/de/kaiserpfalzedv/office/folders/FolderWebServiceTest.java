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
import static javax.ws.rs.core.Response.Status.FORBIDDEN;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@Tag("integration")
public class FolderWebServiceTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(FolderWebServiceTest.class);

    public static final String TENANT = "de.kaiserpfalz-edv";
    public static final String USER = "scott";
    public static final String PASSWORD = "jb0ss";

    @Test
    public void shouldReturnCorrectFolderWhenGivenTheUuid() {
        given()
                .when()
                .pathParam("tenant", TENANT)
                .auth().preemptive().basic(USER, PASSWORD)
                .get("/folders/{tenant}/?uuid=3ca1aa42-4ae0-4066-ae5b-1ab2d1eab7f8")
                .then()
                .statusCode(200);
    }

    @Test
    public void shouldReturnNotFoundWhenGivenTheWrongUuid() {
        given()
                .when()
                .pathParam("tenant", TENANT)
                .auth().preemptive().basic(USER, PASSWORD)
                .get("/folders/{tenant}/?uuid=00000000-0000-0000-0000-000000000000")
                .then()
                .statusCode(NOT_FOUND.getStatusCode());
    }

    @Test
    public void shouldReturnNotFoundWhenGivenAnUnknownKey() {
        given()
                .pathParam("tenant", TENANT)
                .pathParam("key", "not-there")
                .when()
                .auth().preemptive().basic(USER, PASSWORD)
                .get("/folders/{tenant}/{key}")
                .then()
                .statusCode(NOT_FOUND.getStatusCode());
    }

    @Test
    public void shouldReturnForbiddenWhenGivenWrongTenant() {
        given()
                .pathParam("tenant", "wrong-tenant")
                .pathParam("key", "not-there")
                .when()
                .auth().preemptive().basic(USER, PASSWORD)
                .get("/folders/{tenant}/{key}")
                .then()
                .statusCode(FORBIDDEN.getStatusCode());
    }

    @Test
    public void shouldReturnCorrectFolderWhenGivenTheCorrectScopeAndKey() {
        given()
                .pathParam("tenant", TENANT)
                .pathParam("key", "I-19-0001")
                .when()
                .auth().preemptive().basic(USER, PASSWORD)
                .get("/folders/{tenant}/{key}")
                .prettyPeek()
                .then()
                .statusCode(allOf(greaterThanOrEqualTo(200), lessThan(300)));
    }

    @Test
    public void shouldCreateANewFolderWithValidData() throws IOException {
        Path path = Paths.get("target/test-classes/json/folders/create_folder.json");
        String body = new String(Files.readAllBytes(path));
        assert !body.isEmpty();

        given()
                .when()
                .pathParam("tenant", TENANT)
                .header("content-type", MediaType.APPLICATION_JSON)
                .auth().preemptive().basic(USER, PASSWORD)
                .body(body)
                .post("/folders/{tenant}")
                .then()
                .statusCode(allOf(greaterThanOrEqualTo(200), lessThan(300)));
    }

    @Test
    public void shouldReturnFailureWhenCreatingADoublette() throws IOException {
        Path path = Paths.get("target/test-classes/json/folders/create_doublette.json");
        String body = new String(Files.readAllBytes(path));
        assert !body.isEmpty();

        given()
                .when()
                .pathParam("tenant", TENANT)
                .header("content-type", MediaType.APPLICATION_JSON)
                .auth().preemptive().basic(USER, PASSWORD)
                .body(body)
                .post("/folders/{tenant}")
                .then()
                .statusCode(409);
    }

    @Test
    public void shouldReturnFailureWhenCreatingAFolderWithWrongTenant() throws IOException {
        Path path = Paths.get("target/test-classes/json/folders/create_doublette.json");
        String body = new String(Files.readAllBytes(path));
        assert !body.isEmpty();

        given()
                .when()
                .pathParam("tenant", "de.lichti")
                .header("content-type", MediaType.APPLICATION_JSON)
                .auth().preemptive().basic(USER, PASSWORD)
                .body(body)
                .post("/folders/{tenant}")
                .then()
                .statusCode(FORBIDDEN.getStatusCode());
    }

    @Test
    public void shouldReturnHealthWhenCallingReady() {
        given()
                .when()
                .auth().preemptive().basic(USER, PASSWORD)
                .get("/health/ready")
                .then()
                .statusCode(allOf(greaterThanOrEqualTo(200), lessThan(300)));
    }

    @Test
    public void shouldReturnHealthWhenCallingLive() {
        given()
                .when()
                .auth().preemptive().basic(USER, PASSWORD)
                .get("/health/live")
                .then()
                .statusCode(allOf(greaterThanOrEqualTo(200), lessThan(300)));
    }
}