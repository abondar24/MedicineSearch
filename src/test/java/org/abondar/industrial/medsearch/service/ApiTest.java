package org.abondar.industrial.medsearch.service;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.vertx.junit5.VertxExtension;
import io.vertx.reactivex.core.Vertx;
import org.abondar.industrial.medsearch.api.ApiVerticle;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.abondar.industrial.medsearch.api.ApiUtil.API_ROOT;
import static org.abondar.industrial.medsearch.api.ApiUtil.SEARCH_ENDPOINT;
import static org.abondar.industrial.medsearch.api.ApiUtil.SERVER_PORT;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

@ExtendWith({VertxExtension.class})
public class ApiTest {

  private static RequestSpecification spec;

  @BeforeAll
  public static void initMockServer(Vertx vertx) throws Exception {
    spec =
        new RequestSpecBuilder()
            .addFilters(List.of(new ResponseLoggingFilter(), new RequestLoggingFilter()))
            .setBaseUri("http://localhost:" + SERVER_PORT)
            .setBasePath(API_ROOT)
            .build();

    var service = new SearchServiceImpl("testDataset.json");
    var verticle = new ApiVerticle(service);
    vertx.deployVerticle(verticle);
  }

  @Test
  public void findByNameTest() {
    var id = "cdf1dcda-19cf-4cbc-ae9c-75d7ae4088ae";
    given(spec)
        .contentType(ContentType.JSON)
        .get(SEARCH_ENDPOINT + "/Emsam")
        .then()
        .assertThat()
        .statusCode(200)
        .and()
        .body("id", hasItem(id));
  }

  @Test
  public void findByDiseaseTest() {
    var disease = "bladder disease";
    given(spec)
        .contentType(ContentType.JSON)
        .get(SEARCH_ENDPOINT + "/" + disease)
        .then()
        .assertThat()
        .statusCode(200)
        .and()
        .body(
            "id",
            hasItems(
                "b52d7619-da1f-4d63-805d-1d124fe53df4", "58b565e6-32a9-45f6-adef-2e24b9b5582e"))
        .body("size()", is(2));
  }

  @Test
  public void findEmptyResultTest() {
    given(spec)
        .contentType(ContentType.JSON)
        .get(SEARCH_ENDPOINT + "/test")
        .then()
        .assertThat()
        .statusCode(404);
  }
}
