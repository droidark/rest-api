# JWT AUTHENTICATION AND AUTHORIZATION WITH SPRING BOOT AND SPRING SECURITY

## Before to start
### Read first
It's recommendable check this link before reading this document [What is JWT?](https://jwt.io/introduction).

### Watch this
This document is based on Java Brains Tutorial [Spring Boot + Spring Security + JWT from scratch - Java Brains](https://www.youtube.com/watch?v=X80nJ5T7YpE)

## Import dependencies
In ```pom.xml```
```xml
<!-- SPRING SECURITY -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<!-- JWT TOKEN -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt</artifactId>
    <version>0.9.1</version>
</dependency>
<!-- NECESSARY AFTER JAVA 9 -->
<dependency>
    <groupId>javax.xml.bind</groupId>
    <artifactId>jaxb-api</artifactId>
    <version>2.3.1</version>
</dependency>
```

## Add signing key to crypt and decrypt the tokens
In ```application.yaml```
```yaml
security:
  jwt:
    signing-key: [custom signing-key]
```

## Configuration beans
Under configuration package, create a new class called ```SecurityConfig```
```java
@AllArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private MyUserDetailsService myUserDetailsService;
    private JwtRequestFilter jwtRequestFilter;

    
    // This is used to use MyUserDetailService to do the authentication.
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService);
    }

    // Bean to manage the authentication in different endpoints.
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable().
                authorizeRequests()
                .antMatchers("/authentication/login")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    // Bean to crypt and decrypt password stored in database.
    @Bean
    public PasswordEncoder passwordEncoder() {
        String idForEncode = "bcrypt";
        Map<String, PasswordEncoder> encoderMap = new HashMap<>();
        encoderMap.put(idForEncode, new BCryptPasswordEncoder());
        return new DelegatingPasswordEncoder(idForEncode, encoderMap);
    }
}
```

## MyUserDetailsService
This service will manage the authentication. This class is placed in ```service.impl``` package.
```java
@AllArgsConstructor
@Service
public class MyUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        xyz.krakenkat.restapi.domain.model.User user = userRepository.findByName(username);

        if(user == null) {
            throw new UsernameNotFoundException(String.format("The username %username doesn't exist", username));
        }

        return new User(user.getName(), user.getPassword(), new ArrayList<>());
    }
}
```

## JWT Util class
This class is used to run common operations in JWT token. this class is placed in ```util``` package
```java
@Service
public class JwtUtil {

    @Value("${security.jwt.signing-key}")
    private String SECRET_KEY;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
```

## JwtRequestFilter
This is used to filter all http requests and responses. This component is placed in ```filter``` package.
```java
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private MyUserDetailsService userDetailsService;
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            username = jwtUtil.extractUsername(jwt);
        }

        if(username != null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            if(jwtUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        chain.doFilter(request, response);
    }
}
```