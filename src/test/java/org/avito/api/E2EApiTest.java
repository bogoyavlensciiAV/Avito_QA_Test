package org.avito.api;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import org.avito.model.Item;
import org.avito.model.Statistics;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Epic("E2E API Scenarios")
@Feature("E2E Flows")
public class E2EApiTest extends BaseApiTest {

  @Test
  @DisplayName("E2E-1. Full cycle: create and get item")
  void testCreateAndGetE2E() {
    int sellerId = generateSellerId();
    Item payload = new Item(null, sellerId, "E2E Test", 100, new Statistics(15, 2, 11), null);

    String createdId = stepCreateItem(payload);
    Item[] fetchedArray = stepGetItem(createdId);

    assertThat(fetchedArray).isNotEmpty();
    Item fetched = fetchedArray[0];

    assertThat(fetched.getSellerId()).isEqualTo(sellerId);
    assertThat(fetched.getName()).isEqualTo("E2E Test");
    assertThat(fetched.getPrice()).isEqualTo(100);
  }

  @Test
  @DisplayName("E2E-2. Full cycle: create and check statistics")
  void testCreateAndCheckStatE2E() {
    int sellerId = generateSellerId();
    Statistics statsPayload = new Statistics(10, 5, 3);
    Item payload = new Item(null, sellerId, "E2E Stat", 150, statsPayload, null);

    String createdId = stepCreateItem(payload);
    Statistics fetchedStats = stepGetStatistic(createdId);

    assertThat(fetchedStats).isNotNull();
    assertThat(fetchedStats.getLikes()).isEqualTo(10);
    assertThat(fetchedStats.getViewCount()).isEqualTo(5);
    assertThat(fetchedStats.getContacts()).isEqualTo(3);
  }

  @Test
  @DisplayName("E2E-3. Full cycle: publish multiple items and get seller list")
  void testCreateMultipleAndListE2E() {
    int sellerId = generateSellerId();

    Item payload1 = new Item(null, sellerId, "E2E Ad 1", 100, new Statistics(1, 1, 1), null);
    Item payload2 = new Item(null, sellerId, "E2E Ad 2", 200, new Statistics(2, 2, 2), null);

    stepCreateItem(payload1);
    stepCreateItem(payload2);

    Item[] items = stepGetItemsBySeller(sellerId);

    assertThat(items).hasSize(2);
    assertThat(items[0].getSellerId()).isEqualTo(sellerId);
    assertThat(items[1].getSellerId()).isEqualTo(sellerId);
  }

  @Step("1. Call POST /api/1/item to create")
  public String stepCreateItem(Item payload) {
    String msg =
        given().body(payload).post("/api/1/item").then().statusCode(200).extract().path("status");
    return extractId(msg);
  }

  @Step("2. Call GET /api/1/item/:id with received ID")
  public Item[] stepGetItem(String id) {
    return given()
        .pathParam("id", id)
        .get("/api/1/item/{id}")
        .then()
        .statusCode(200)
        .extract()
        .as(Item[].class);
  }

  @Step("3. Call GET /api/1/statistic/:id with received ID")
  public Statistics stepGetStatistic(String id) {
    Statistics[] statsArray =
        given()
            .pathParam("id", id)
            .get("/api/1/statistic/{id}")
            .then()
            .statusCode(200)
            .extract()
            .as(Statistics[].class);
    return statsArray.length > 0 ? statsArray[0] : null;
  }

  @Step("4. Call GET /api/1/:sellerID/item")
  public Item[] stepGetItemsBySeller(int sellerId) {
    return given()
        .pathParam("sellerID", sellerId)
        .get("/api/1/{sellerID}/item")
        .then()
        .statusCode(200)
        .extract()
        .as(Item[].class);
  }
}
