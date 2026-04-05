package org.avito.api;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.avito.model.Item;
import org.avito.model.Statistics;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Epic("API Tests")
@Feature("Seller API endpoint testing")
public class SellerApiTest extends BaseApiTest {

  @Test
  @DisplayName("3.1 Get seller items")
  void testGetSellerItems() {
    int sellerId = generateSellerId();
    Item payload = new Item(null, sellerId, "Seller Item", 200, new Statistics(2, 2, 2), null);
    given().body(payload).post("/api/1/item").then().statusCode(200);

    Item[] items =
        given()
            .pathParam("sellerID", sellerId)
            .get("/api/1/{sellerID}/item")
            .then()
            .statusCode(200)
            .extract()
            .as(Item[].class);

    assertThat(items).isNotEmpty();
    for (Item item : items) {
      assertThat(item.getSellerId()).isEqualTo(sellerId);
    }
  }

  @Test
  @DisplayName("3.2 Request for sellerID with no items")
  void testGetSellerWithoutItems() {
    int emptySellerId = 999999;

    given()
        .pathParam("sellerID", emptySellerId)
        .get("/api/1/{sellerID}/item")
        .then()
        .statusCode(
            org.hamcrest.Matchers.anyOf(
                org.hamcrest.Matchers.is(200), org.hamcrest.Matchers.is(404)));
  }

  @Test
  @DisplayName("3.3 Request with invalid sellerID")
  void testGetInvalidSellerId() {
    given().pathParam("sellerID", "abc").get("/api/1/{sellerID}/item").then().statusCode(400);
  }

  @Test
  @DisplayName("3.4 Request with negative sellerID")
  void testGetNegativeSellerId() {
    given().pathParam("sellerID", "-111111").get("/api/1/{sellerID}/item").then().statusCode(400);
  }
}
