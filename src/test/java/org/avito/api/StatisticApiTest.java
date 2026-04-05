package org.avito.api;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import java.util.UUID;
import org.avito.model.Item;
import org.avito.model.Statistics;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Epic("API Tests")
@Feature("Statistic API endpoint testing")
public class StatisticApiTest extends BaseApiTest {

  @Test
  @DisplayName("4.1 Get statistics for existing item")
  void testGetExistingStatistic() {
    int sellerId = generateSellerId();
    Item payload = new Item(null, sellerId, "Stat Check", 100, new Statistics(20, 50, 5), null);

    String st =
        given().body(payload).post("/api/1/item").then().statusCode(200).extract().path("status");
    String id = extractId(st);

    Statistics[] stats =
        given()
            .pathParam("id", id)
            .get("/api/1/statistic/{id}")
            .then()
            .statusCode(200)
            .extract()
            .as(Statistics[].class);

    assertThat(stats).isNotEmpty();
    assertThat(stats[0].getLikes()).isEqualTo(20);
    assertThat(stats[0].getViewCount()).isEqualTo(50);
    assertThat(stats[0].getContacts()).isEqualTo(5);
  }

  @Test
  @DisplayName("4.2 Get statistics for non-existent ID")
  void testGetNonExistentStatistic() {
    given()
        .pathParam("id", UUID.randomUUID().toString())
        .get("/api/1/statistic/{id}")
        .then()
        .statusCode(404);
  }
}
