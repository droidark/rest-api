package xyz.krakenkat.restapi.controller;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import xyz.krakenkat.restapi.domain.model.HelloWorld;

@AllArgsConstructor
@RestController
public class HelloWorldController {
    // GET
    // URI - /hello-world
    // METHOD - helloWorld()

    // SOLUTION 1
    //@RequestMapping(method = RequestMethod.GET, path = "/hello-world")

    // INTERNATIONALIZATION BEAN
    private MessageSource messageSource;

    // SOLUTION 2
    @GetMapping(path = "/hello-world")
    public String helloWorld() {
        return "Hello World";
    }

    @GetMapping(path = "/hello-world-bean")
    public HelloWorld helloWorldBean() {
        return new HelloWorld("Hello World Bean");
    }

    @GetMapping(path = "/hello-world/path-variable/{name}")
    public HelloWorld helloWorldBeanName(@PathVariable String name) {
        return new HelloWorld(String.format("Hello World, %s", name));
    }

    // INTERNATIONALIZATION

    // SOLUTION 1:
    // REQUIRES CONFIGURE LocalResolver & ResourceBundleMessageSource BEANS.
    // REQUIRES CONFIGURE Accept-Language AS HEADER PARAMETER
    //    @GetMapping(path = "/hello-world-internationalization")
    //    public String helloWorldInternationalization(@RequestHeader(name = "Accept-Language", required = false) Locale locale) {
    //        return messageSource.getMessage("good.morning.message", null,locale);
    //    }

    // SOLUTION 2 [RECOMMENDED]
    //  REQUIRES CONFIGURE spring.messages.basename in application.yml
    @GetMapping(path = "/hello-world-internationalization")
    public String helloWorldInternationalization() {
        return messageSource.getMessage("good.morning.message", null, LocaleContextHolder.getLocale());
    }
}
