package xyz.krakenkat.restapi.service;

import org.springframework.http.converter.json.MappingJacksonValue;

public interface FilteringService {
    public <T>MappingJacksonValue filtering(T t, String filterName, String... fields);
}
