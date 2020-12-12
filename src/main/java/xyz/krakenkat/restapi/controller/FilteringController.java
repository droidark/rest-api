package xyz.krakenkat.restapi.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.krakenkat.restapi.domain.model.FilteringBean;
import xyz.krakenkat.restapi.service.FilteringService;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@RestController
public class FilteringController {

    private FilteringService filteringService;

    // STATIC FILTERING
//    @GetMapping(path = "/filtering")
//    public FilteringBean filtering() {
//        return new FilteringBean("Field 1", "Field Two", "Field Three");
//    }
//
//    @GetMapping(path = "/filtering-list")
//    public List<FilteringBean> filteringList() {
//        return Arrays.asList(
//                new FilteringBean("Filed One", "Field Two", "Field Three"),
//                new FilteringBean("Filed Four", "Field Five", "Field Six")
//        );
//    }

    // DYNAMIC FILTERING
    @GetMapping(path = "filtering")
    public MappingJacksonValue filtering() {
        FilteringBean filteringBean = new FilteringBean("Field One", "Field Two", "Field Three");
        return filteringService.filtering(filteringBean, "FilteringBeanFilter", "fieldOne", "fieldThree");
    }

    @GetMapping(path = "filtering-list")
    public MappingJacksonValue filteringList() {
        List<FilteringBean> filteringBeanList = Arrays.asList(
                new FilteringBean("Filed One", "Field Two", "Field Three"),
                new FilteringBean("Filed Four", "Field Five", "Field Six")
        );
        return filteringService.filtering(filteringBeanList, "FilteringBeanFilter", "fieldTwo", "fieldThree");
    }
}
