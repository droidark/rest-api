package xyz.krakenkat.restapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.krakenkat.restapi.domain.model.Name;
import xyz.krakenkat.restapi.domain.model.PersonV1;
import xyz.krakenkat.restapi.domain.model.PersonV2;

@RestController
public class PersonVersioningController {
    // URI VERSIONING
    @GetMapping(path = "/v1/person")
    public PersonV1 personV1() {
        return new PersonV1("Ken Masters");
    }

    @GetMapping(path = "/v2/person")
    public PersonV2 personV2() {
        return new PersonV2(new Name("Ken", "Masters"));
    }

    // REQUEST PARAMETER VERSIONING
    // .../person/params?version=1
    @GetMapping(path = "/person/params", params = "version=1")
    public PersonV1 paramsV1() {
        return new PersonV1("Ken Masters");
    }

    // .../person/params?version=2
    @GetMapping(path = "/person/params",  params = "version=2")
    public PersonV2 paramsV2() {
        return new PersonV2(new Name("Ken", "Masters"));
    }

    // ACCEPT HEADER VERSIONING
    @GetMapping(path = "/person/header", headers = "X-API-VERSION=1")
    public PersonV1 headerV1() {
        return new PersonV1("Ken Masters");
    }

    @GetMapping(path = "/person/header", headers = "X-API-VERSION=2")
    public PersonV2 headerV2() {
        return new PersonV2(new Name("Ken", "Masters"));
    }

    // MIME TYPE VERSIONING
    @GetMapping(path = "/person/produces", produces = "application/xyz.krakenkat.app-v1+json")
    public PersonV1 producesV1() {
        return new PersonV1("Ken Masters");
    }

    @GetMapping(path = "/person/produces", produces = "application/xyz.krakenkat.app-v2+json")
    public PersonV2 producesV2() {
        return new PersonV2(new Name("Ken", "Masters"));
    }
}
