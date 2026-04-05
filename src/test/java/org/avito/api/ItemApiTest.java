package org.avito.api;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.avito.model.Item;
import org.avito.model.Statistics;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Epic("API Tests")
@Feature("Item API endpoint testing")
public class ItemApiTest extends BaseApiTest {

  @Test
  @DisplayName("1.1 Create with valid positive data")
  void testCreateValidPositive() {
    int sellerId = generateSellerId();
    Item payload = new Item(null, sellerId, "Valid Item", 100, new Statistics(10, 50, 3), null);

    String statusMsg =
        given().body(payload).post("/api/1/item").then().statusCode(200).extract().path("status");

    String id = extractId(statusMsg);
    assertThat(id).isNotBlank();
  }

  @Test
  @DisplayName("1.2 Create with zero values for price and statistics")
  void testCreateZeroValues() {
    int sellerId = generateSellerId();
    Item payload = new Item(null, sellerId, "Zero values item", 0, new Statistics(0, 0, 0), null);

    String statusMsg =
        given().body(payload).post("/api/1/item").then().statusCode(200).extract().path("status");

    String id = extractId(statusMsg);
    assertThat(id).isNotBlank();
  }

  @Test
  @DisplayName("1.3 Create with negative numbers for price and statistics")
  void testCreateNegativeValues() {
    int sellerId = generateSellerId();
    Item payload =
        new Item(null, sellerId, "Negative values", -100, new Statistics(-1, -5, -1), null);

    given().body(payload).post("/api/1/item").then().statusCode(400);
  }

  @Test
  @DisplayName("1.4 Create without mandatory field sellerID")
  void testCreateWithoutSellerId() {
    Map<String, Object> payload = new HashMap<>();
    payload.put("name", "No seller");
    payload.put("price", 100);
    payload.put("statistics", new Statistics(1, 1, 1));

    given().body(payload).post("/api/1/item").then().statusCode(400);
  }

  @Test
  @DisplayName("1.5 Create item with invalid data type (string instead of integer)")
  void testCreateWithInvalidDataType() {
    Map<String, Object> payload = new HashMap<>();
    payload.put("sellerID", generateSellerId());
    payload.put("name", "String price");
    payload.put("price", "сто");
    payload.put("statistics", new Statistics(1, 1, 1));

    given().body(payload).post("/api/1/item").then().statusCode(400);
  }

  @Test
  @DisplayName("1.6 Duplicate identical requests")
  void testDuplicateRequests() {
    int sellerId = generateSellerId();
    Item payload = new Item(null, sellerId, "Duplicate check", 150, new Statistics(5, 5, 5), null);

    String st1 =
        given().body(payload).post("/api/1/item").then().statusCode(200).extract().path("status");
    String st2 =
        given().body(payload).post("/api/1/item").then().statusCode(200).extract().path("status");

    assertThat(extractId(st1)).isNotEqualTo(extractId(st2));
  }

  @Test
  @DisplayName("2.1 Get existing item")
  void testGetExistingItem() {
    int sellerId = generateSellerId();
    Item payload = new Item(null, sellerId, "Get Me", 500, new Statistics(1, 2, 3), null);
    String st =
        given().body(payload).post("/api/1/item").then().statusCode(200).extract().path("status");
    String id = extractId(st);

    Item[] fetched =
        given()
            .pathParam("id", id)
            .get("/api/1/item/{id}")
            .then()
            .statusCode(200)
            .extract()
            .as(Item[].class);

    assertThat(fetched).isNotEmpty();
    assertThat(fetched[0].getId()).isEqualTo(id);
    assertThat(fetched[0].getName()).isEqualTo("Get Me");
    assertThat(fetched[0].getPrice()).isEqualTo(500);
  }

  @Test
  @DisplayName("2.2 Get item by non-existent ID")
  void testGetNonExistentItem() {
    given()
        .pathParam("id", UUID.randomUUID().toString())
        .get("/api/1/item/{id}")
        .then()
        .statusCode(404);
  }

  @Test
  @DisplayName("2.3 Get item with invalid ID format")
  void testGetInvalidIdFormat() {
    given()
        .pathParam("id", "!@#$%^&")
        .get("/api/1/item/{id}")
        .then()
        .statusCode(
            org.hamcrest.Matchers.anyOf(
                org.hamcrest.Matchers.is(400), org.hamcrest.Matchers.is(404)));
  }
}
