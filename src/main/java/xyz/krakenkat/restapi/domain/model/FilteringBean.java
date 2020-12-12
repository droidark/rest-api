package xyz.krakenkat.restapi.domain.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
// APPROACH 1: STATIC FILTERING
// @JsonIgnoreProperties(value = {"fieldOne", "fieldTwo"})
// APPROACH 2: DYNAMIC FILTERING
@JsonFilter("FilteringBeanFilter")
public class FilteringBean {
    // APPROACH 1: STATIC FILTERING
    // @JsonIgnore
    private String fieldOne;
    private String fieldTwo;
    private String fieldThree;
}
