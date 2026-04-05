package org.avito.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {
  private String id;

  @JsonProperty("sellerID")
  @JsonAlias("sellerId")
  private Integer sellerId;

  private String name;
  private Integer price;
  private Statistics statistics;
  private String createdAt;
}
