package org.avito.api;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import java.util.concurrent.ThreadLocalRandom;
import org.junit.jupiter.api.BeforeAll;

public class BaseApiTest {

  @BeforeAll
  static void setup() {
    RestAssured.baseURI = "https://qa-internship.avito.com";
    RestAssured.requestSpecification =
        new RequestSpecBuilder()
            .setContentType(ContentType.JSON)
            .setAccept(ContentType.JSON)
            .addFilter(new AllureRestAssured())
            .log(LogDetail.ALL)
            .build();
  }

  protected int generateSellerId() {
    return ThreadLocalRandom.current().nextInt(111111, 999999);
  }

  protected String extractId(String statusMsg) {
    if (statusMsg == null || statusMsg.length() < 36) return "";
    // A UUID is 36 characters long
    return statusMsg.substring(statusMsg.length() - 36).trim();
  }
}
