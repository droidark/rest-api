package xyz.krakenkat.restapi.service.impl;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Component;
import xyz.krakenkat.restapi.service.FilteringService;

@Component
public class FilteringServiceImpl implements FilteringService {

    @Override
    public <T> MappingJacksonValue filtering(T t, String filterName, String... fields) {
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept(fields);
        FilterProvider filters = new SimpleFilterProvider().addFilter(filterName, filter);
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(t);
        mappingJacksonValue.setFilters(filters);
        return mappingJacksonValue;
    }
}
