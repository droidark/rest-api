# REST API TUTORIAL WITH SPRING BOOT
## Introduction to Web Services
### Web Service definition
Software system designed to support **interoperable** **machine-to-machine** interaction **over a network**.
### Web Service communication
![web service communication](https://raw.githubusercontent.com/droidark/rest-api/master/diagrams/web-service-communication.svg)
### Web services three Keys
1. Designed for machine-to-machine interaction.
1. Should be interoperable (no platform dependent).
1. Should allow communication over a network.
### Web Service communication
The system communication should be platform independent. To achieve that there are two popular options.
* JSON
* XML
#### Service definition
The service definition defines what is the format of the request and response. It’s the contract between the service provider and the service consumer.
* Request/Response format.
* Request structure.
* Response structure.
* Endpoint.
#### Transport
Transport defines how a service is called.
##### HTTP (Hyper Text Transfer Protocol)
The service is exposed over the Web (through a URL).
##### Message Queue
Message queues enable asynchronous communication, which means that the endpoints that are producing and consuming messages interact with the queue, not each other. Producers can add requests to the queue without waiting for them to be processed. Consumers process messages only when they are available. No component in the system is ever stalled waiting for another, optimizing data flow.
![message queue](https://raw.githubusercontent.com/droidark/rest-api/master/diagrams/message-queue.svg)
### Key Terminology
* Request and response.
* Message Exchange Format.
    * XML.
    * JSON.
* Service provider or Server.
* Service customer or Client.
* Service definition.
* Transport.
    * HTTP.
    * Message Queue.
## REST (REPRESENTATIONAL STATE TRANSFER)
A REST API defines a set of functions which developers can perform requests and receive responses via HTTP protocols.
### Resource
* A resource has a URI (Uniform Resource Identifier).
* A resource can have different representations.
## SPRING BOOT AUTOCONFIGURATION AND DISPATCHER SERVLET
### What's dispatcher servlet?
The dispatcher-servlet handles all the requests. Dispatcher-servlet is the front controller for Spring MVC.
### Who is configuring the dispatcher servlet?
Spring Boot auto-configuration automatically when found the dispatcher-servlet on the classpath.
### What does the dispatcher servlet do?
The job of the DispatcherServlet is to take an incoming URI and find the right combination of handlers (generally methods on Controller classes) and views that combine to form the page or resource that's supposed to be found at that location. The Dispatcher servlet is the bit that *"knows"* to call that method when a browser requests the page, and to combine its results with the matching document.
### How does the Bean object get converted to JSON?
HTTPMessageConvertersAutoConfiguration. JacksonObjectMapper who's convert Java Objects to JSON (It's configured in the dispatcher-servlet)
### Who is configuring the error mapping?
Spring Boot auto-configuration automatically when found the ErrorMVC on the classpath.
Spring Boot looks at all the classes, all the JARs which are available and based on whatever is in the classpath, it tries to auto-configure different things like dispatcher servlet.
## REST BEST PRACTICES
https://docs.microsoft.com/en-us/azure/architecture/best-practices/api-design
## GENERIC EXCEPTION HANDLING
It’s necessary to build standard structure to handle the exceptions.
The steps to create a exception handling
1. Create a Response Bean
1. Create the exception class **(Use @ResponseStatus to send the correct code error when a problem exists)**
    ```java
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }
    ```
1. It’s possible to create a *"default controller"* to handle the exceptions. The **@ControllerAdvice** annotation allows us to consolidate our multiple, scattered **@ExceptionHandler** from before into a single, global error handling component.
    ````java
    @ControllerAdvice
    @RestController
    public class CustomizedResponseEntityExceptionHandle extends ResponseEntityExceptionHandler {
    
        @ExceptionHandler(Exception.class)
        public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
            ExceptionResponse exceptionResponse = new ExceptionResponse(
                    new Date(), ex.getMessage(), request.getDescription(false));
    
            return new ResponseEntity(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    
        @ExceptionHandler(UserNotFoundException.class)
        public final ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
            ExceptionResponse exceptionResponse = new ExceptionResponse(
                    new Date(), ex.getMessage(), request.getDescription(false));
    
            return new ResponseEntity(exceptionResponse, HttpStatus.NOT_FOUND);
        }
    }
    ````
1. More options, review https://www.baeldung.com/exception-handling-for-rest-with-spring
## VALIDATIONS
The most common is to use Java Validation API. The steps to add a validation in our REST API are:
1. Add **@Valid** annotation in out controller method signature.
1. Set the rules in the entity class (POJO) using a valid annotation (to know more about the available annotations, please visit https://docs.oracle.com/javaee/7/api/javax/validation/constraints/package-summary.html)
1. To send a validation error message it’s necessary to override the **handleMethodArgumentNotValid()** method in our Exception Handle Controller.
1. It’s possible to customize the details message, To do that you need to follow the next steps:
    1. Set message property in the Validation Annotations attached to the property field in Entity Bean (POJO, Model Class).
> With getBindingResult() it's possible to manage the information displayed about the error.

Hibernate validator is the most important implementation of `javax.validation`.
## HATEOAS (HYPERMEDIA AS THE ENGINE OF APPLICATION STATE)
Each HTTP GET request should return the information necessary to find the resources related directly to the requested object through hyperlinks included in the response, and it should also be provided with information that describes the operations available on each of these resources.

https://docs.microsoft.com/en-us/azure/architecture/best-practices/api-design#use-hateoas-to-enable-navigation-to-related-resources

> Currently, there are no standards or specifications that define how to model the HATEOAS principle.

## INTERNATIONALIZATION (i18n)
The steps to configure the internationalization are:
1. Configure **LocalResolver** Bean
    ```java
    @Bean
    public LocaleResolver localResolver() {
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(Locale.US);
        return localeResolver;
    }
    ```
1. Configure application.yml with the message basename file
    ```yml
    spring:
      messages:
        basename: [message-name]
    ```
1. Configure the internationalization messages in a properties file under the next format.
    1. `messages.properties` (default messages file).
    1. `messages_[language code].properties`. Example, `message_es.properties`.
1. Configure the controller
    ```java
    @GetMapping(path = "/hello-world-internationalization")
    public String helloWorldInternationalization() {
       return messageSource.getMessage("good.morning.message", null, LocaleContextHolder.getLocale());
    }
    ```
## CONTENT NEGOTIATION - SUPPORT FOR XML
The steps to support XML responses are:
1. Add Jackson Dataformat XML dependency on `pom.xml`
    ```xml
    <dependency>
        <groupId>com.fasterxml.jackson.dataformat</groupId>
        <artifactId>jackson-dataformat-xml</artifactId>
    </dependency>
    ```
1. To test you need to add the next header **Accept: application/xml** and **Content-Type: application/xml** (for POST, PUT or PATCH requests)
## CONFIGURING OPENAPI 3.0 DOCUMENTATION
To configure OpenAPI, it’s necessary to import the next dependencies:
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-ui</artifactId>
    <version>${version}</version>
</dependency>
```
### View documentation in JSON format
> http://[host]:[port]/v3/api-docs/
### View documentation with UI
> http://[host]:[port]/swagger-ui.html
### Edit configuration
https://www.dariawan.com/tutorials/spring/documenting-spring-boot-rest-api-springdoc-openapi-3/
### Edit general information
#### Option 1
Create a configuration bean to overwrite the default information
```java
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI().
                components(new Components())
                .info(new Info()
                        .title("REST API")
                        .description("Tutorial about REST API with Spring Boot")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Fozz")
                                .email("droidark@mail.com")
                                .url("https://github.com/droidark")));
    }
}
```
#### Option 2
Put the next annotations in the main class.
```java
@OpenAPIDefinition(
        info = @Info(
                title = "REST API",
                version = "1.0",
                description = "Tutorial about REST API with Spring Boot",
                license = @License(name = "Apache 2.0", url = "http:foo.bar"),
                contact = @Contact(name = "Fozz", email = "droidark@mail.com", url = "https://github.com/droidark")
        )
)
```
## MONITORING APIS WITH SPRING BOOT ACTUATOR
### Install Spring Actuator in our project.
To enable Spring Boot Actuator we need to import two dependencies
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.data</groupId>
    <artifactId>spring-data-rest-hal-explorer</artifactId>
</dependency>
```
#### HAL - Hypertext Application Language
It specifies a simple format to specify how to hyperlink between resources in your API. Spring Actuator is in HAL format.
#### HAL Browser
It looks at those APIs, identifies the links, and shows them on the screen, so you can easily browse through the API by just clicking the links which are present in there. So, the HAL browser makes it easy to consume the HAL services which are being exposed by spring-boot-starter-actuator.
### Configure Spring Boot Actuator
After to install boot dependencies, Spring Actuator creates a new endpoint `http://[host]:[port]/actuator`, also Spring Actuator will create another three different endpoints to manage the application health.
#### Endpoints
https://docs.spring.io/spring-boot/docs/1.5.x/reference/html/production-ready-endpoints.html
#### /actuator/health
Shows application health information (when the application is secure, a simple *"status"* when accessed over an unauthenticated connection or full message details when authenticated).
#### /actuator/info
By default, this endpoint doesn't show anything, to configure it, it’s necessary to edit the `application.yml` file with the next parameter.
```yml
management:
  endpoints:
    web:
      exposure:
        include: "*"
```
## FILTERING
It’s important remember, when you enable a lot of tracking, a lot of auditing, what would happen is? there would be a performance impact as well.
### Static Filtering
The static filtering is used to skip some bean property for the response. There are two approaches to achieve this.
### @JsonIgnore
This annotation is used on top of property to ignore in the response
```java
public class SomeBean {
    private String field1;
    
    // FIELD 2 WILL BE EXCLUDED FOR THE RESPONSE
    @JsonIgnore
    private String field2; 
    
    private String field3;
}
```
### @JsonIgnoreProperties
On the top of the class, `@JsonIgnoreProperties` will ignore the properties defined inside the annotation.
```java
// field1 and field3 will be ignored for the response
@JsonIgnoreProperties(value = {"field1", "field3"})
public class SomeBean {
    private String field1;
    private String field2;
    private String field3;
}
```
### Dynamic Filtering
1. Add `@JsonFilter({filterName})` at the class header.
    ```java
    @JsonFilter("FilteringBeanFilter")
    public class FilteringBean { ... }
    ```
1. Use `MappingJacksonValue` to specify the fields to filter.
    ```java
    public <T> MappingJacksonValue filtering(T t, String filterName, String... fields) {
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept(fields);
        FilterProvider filters = new SimpleFilterProvider().addFilter(filterName, filter);
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(t);
        mappingJacksonValue.setFilters(filters);
        return mappingJacksonValue;
    }
    ```
## VERSIONING
### URI versioning
```java
@GetMapping(path = "/{version}/person")
public PersonV1 personV1() { ... }
```
### Request parameter versioning
```java
@GetMapping(path = "/person/params", params = "version={version}")
public PersonV1 paramsV1() { ... }
```
### Accept the header versioning
```java
@GetMapping(path = "/person/header", headers = "X-API-VERSION={version}")
public PersonV1 headerV1() { ... }
```
### MIME type versioning
```java
@GetMapping(path = "/person/produces", produces = "application/xyz.krakenkat.app-{version}+json")
public PersonV1 producesV1() { ... }
```
## BASIC AUTHENTICATION WITH SPRING SECURITY
The first step to implement a basic authentication with Spring Boot is import Spring Security dependency. Once the dependency is downloaded, Spring Boot automatically will start to configure the basic security for **ALL METHODS**.
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```
The way to do a test over protected API with Spring Security basic authentication is:
1. Configure WebSecurityConfig class (not necessary if you’re using the default configuration).
```java
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests().anyRequest().authenticated().and().httpBasic();
    }
}
```
Configure a username and password in `application.yml`. By default, the username is **user**, and **the password is provided by the Spring Boot terminal**.
Terminal
```shell
Using generated security password: 4c04e019-2b8b-41ef-8ca2-b04edfa00a63
```
application.yml (custom username and password)
```yml
spring:
  security:
    user:
        name: username
        password: password
```
## RICHARDSON MATURITY MODEL
https://developers.redhat.com/blog/2017/09/13/know-how-restful-your-api-is-an-overview-of-the-richardson-maturity-model/
## REST BEST PRACTICES
### Consumer first
* Have a clear idea about who your consumers are.
* The first and the last best practice is consumer first.
* Have a great documentation for your APIs.
### Make the best use of HTTP
* Request methods
    * GET
    * POST
    * PUT
    * PATCH
    * DELETE
* Response status
    * 200: Success.
    * 404: Resource not found.
    * 400: Bad request.
    * 201: Created (When you create a resource do not send just success. Send created back).
    * 401: Unauthorized.
    * 500: Server error.
* No Secure info in the URI.
* Use plurals
* Use nouns for resources
    * For exceptions, define a consistent approach.


