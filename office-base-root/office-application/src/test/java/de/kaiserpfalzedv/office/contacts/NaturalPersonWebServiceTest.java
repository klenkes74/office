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

package de.kaiserpfalzedv.office.contacts;

import de.kaiserpfalzedv.security.tenant.cdi.TenantProvider;
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
public class NaturalPersonWebServiceTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(NaturalPersonWebServiceTest.class);

    public static final String TENANT = "de.kaiserpfalz-edv";
    public static final String USER = "scott";
    public static final String PASSWORD = "jb0ss";

    @Test
    public void shouldReturnCorrectNaturalPersonWhenGivenTheUuid() {
        given()
                .when()
                .pathParam(TenantProvider.TENANT_MDC_MARKER, TENANT)
                .auth().preemptive().basic(USER, PASSWORD)
                .get("/contacts/{tenant}/natural/?uuid=f422fcf1-ebe1-4b1e-adc2-0a0cfdeb0d81")
                .then()
                .statusCode(200);
    }

    @Test
    public void shouldReturnNotFoundWhenGivenTheWrongUuid() {
        given()
                .when()
                .pathParam(TenantProvider.TENANT_MDC_MARKER, TENANT)
                .auth().preemptive().basic(USER, PASSWORD)
                .get("/contacts/{tenant}/natural/?uuid=00000000-0000-0000-0000-000000000000")
                .then()
                .statusCode(404);
    }

    @Test
    public void shouldReturnNotFoundWhenGivenAnUnknownKey() {
        given()
                .pathParam(TenantProvider.TENANT_MDC_MARKER, TENANT)
                .pathParam("key", "not-there")
                .when()
                .auth().preemptive().basic(USER, PASSWORD)
                .get("/contacts/{tenant}/natural/{key}")
                .then()
                .statusCode(NOT_FOUND.getStatusCode());
    }

    @Test
    public void shouldReturnForbiddenWhenGivenWrongTenant() {
        given()
                .pathParam(TenantProvider.TENANT_MDC_MARKER, "wrong-tenant")
                .pathParam("key", "not-there")
                .when()
                .auth().preemptive().basic(USER, PASSWORD)
                .get("/contacts/{tenant}/natural/{key}")
                .then()
                .statusCode(FORBIDDEN.getStatusCode());
    }

    @Test
    public void shouldReturnCorrectNaturalPersonWhenGivenTheCorrectScopeAndKey() {
        given()
                .pathParam(TenantProvider.TENANT_MDC_MARKER, TENANT)
                .pathParam("key", "D10005")
                .when()
                .auth().preemptive().basic(USER, PASSWORD)
                .get("/contacts/{tenant}/natural/{key}")
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    @Test
    public void shouldCreateANewNaturalPersonWithValidData() throws IOException {
        Path path = Paths.get("target/test-classes/json/contacts/create_natural_person.json");
        String body = new String(Files.readAllBytes(path));
        assert !body.isEmpty();

        given()
                .when()
                .pathParam(TenantProvider.TENANT_MDC_MARKER, TENANT)
                .header("content-type", MediaType.APPLICATION_JSON)
                .auth().preemptive().basic(USER, PASSWORD)
                .body(body)
                .post("/contacts/{tenant}/natural")
                .then()
                .statusCode(allOf(greaterThanOrEqualTo(200), lessThan(300)));
    }

    @Test
    public void shouldModifyNaturalPersonWithValidData() throws IOException {
        Path path = Paths.get("target/test-classes/json/contacts/modify_natural_person.json");
        String body = new String(Files.readAllBytes(path));
        assert !body.isEmpty();

        given()
                .when()
                .pathParam(TenantProvider.TENANT_MDC_MARKER, TENANT)
                .header("content-type", MediaType.APPLICATION_JSON)
                .auth().preemptive().basic(USER, PASSWORD)
                .body(body)
                .put("/contacts/{tenant}/natural/")
                .prettyPeek()
                .then()
                .statusCode(allOf(greaterThanOrEqualTo(200), lessThan(300)));
    }

    @Test
    public void shouldReturnFailureWhenCreatingADoublette() throws IOException {
        Path path = Paths.get("target/test-classes/json/contacts/create_natural_person_doublette.json");
        String body = new String(Files.readAllBytes(path));
        assert !body.isEmpty();

        given()
                .when()
                .pathParam(TenantProvider.TENANT_MDC_MARKER, TENANT)
                .header("content-type", MediaType.APPLICATION_JSON)
                .auth().preemptive().basic(USER, PASSWORD)
                .body(body)
                .post("/contacts/{tenant}/natural/")
                .then()
                .statusCode(409);
    }

    @Test
    public void shouldReturnFailureWhenCreatingANaturalPersonWithWrongTenant() throws IOException {
        Path path = Paths.get("target/test-classes/json/contacts/create_natural_person_doublette.json");
        String body = new String(Files.readAllBytes(path));
        assert !body.isEmpty();

        given()
                .when()
                .pathParam(TenantProvider.TENANT_MDC_MARKER, "de.lichti")
                .header("content-type", MediaType.APPLICATION_JSON)
                .auth().preemptive().basic(USER, PASSWORD)
                .body(body)
                .post("/contacts/{tenant}/natural/")
                .then()
                .statusCode(FORBIDDEN.getStatusCode());
    }

    @Test
    public void shouldDeletePersonWhenCorrectUuidIsGiven() {
        given()
                .when()
                .pathParam(TenantProvider.TENANT_MDC_MARKER, TENANT)
                .header("content-type", MediaType.APPLICATION_JSON)
                .auth().preemptive().basic(USER, PASSWORD)
                .delete("/contacts/{tenant}/natural/?uuid=41d56361-c099-4a55-82cc-3c25680b7df6")
                .then()
                .statusCode(allOf(greaterThanOrEqualTo(200), lessThan(300)));
    }

    @Test
    public void shouldReturnHealthWhenCallingReady() {
        given()
                .when()
                .auth().preemptive().basic(USER, PASSWORD)
                .get("/health/ready")
                .then()
                .statusCode(200);
    }

    @Test
    public void shouldReturnHealthWhenCallingLive() {
        given()
                .when()
                .auth().preemptive().basic(USER, PASSWORD)
                .get("/health/live")
                .then()
                .statusCode(200);
    }
}