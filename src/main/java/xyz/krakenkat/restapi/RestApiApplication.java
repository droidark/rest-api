package xyz.krakenkat.restapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@OpenAPIDefinition(
        info = @Info(
                title = "REST API",
                version = "1.0",
                description = "Tutorial about REST API with Spring Boot",
                license = @License(name = "Creative Commons Attribution 4.0 International License.",
                        url = "https://creativecommons.org"),
                contact = @Contact(name = "Fozz", email = "droidark@outlook.com", url = "https://github.com/droidark")
        )
)
@SpringBootApplication
public class RestApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestApiApplication.class, args);
    }

    // INTERNATIONALIZATION
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(Locale.US);
        return localeResolver;
    }

//    THIS APPROACH APPLIES ONLY FOR INTERNATIONALIZATION OPTION 1
//    @Bean
//    public ResourceBundleMessageSource messageSource() {
//        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
//        messageSource.setBasename("messages");
//        return messageSource;
//    }
}
