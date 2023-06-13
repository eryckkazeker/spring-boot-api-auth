# Spring API Best Practices

This project includes some Spring API best practices like:

* Correct HTTP response codes
* Request body validation
* Proper error handling
* Spring Security

## Response Codes

The response codes should be in according with the situation. For example:

* **200 - OK**: When the request was processed correctly
* **201 - Created**: When a new resource was created
* **204 - No Content**: When the request was processed correctly but
there's no data to be returned (deleting a resource, for example)
* **400 - Bad Request**: When there's something wrong with the request body (validation error, for example)
* **401 - Unauthorized**: When the user is not authenticated with the server
* **403 - Forbidden**: When the user is authenticated but doesn't have the permission to perform the request.
* **404 - Not Found**: When the resource requested doesn't exist.
* **500 - Internal Server Error**: When an error happened on the server side.

## Request Body Validation

There are some annotations provided by the framework to require a validation to be performed on the request.

The @Valid annotation is put on the @RequestBody annotated object.

Inside the object, we can annotate each attribute with a specific type of validation to be performed. For example:

* **@NotNull**: to say that a field must not be null.
* **@NotEmpty**: to say that a list field must not empty.
* **@NotBlank**: to say that a string field must not be the empty string (i.e. it must have at least one character).
* **@Min and @Max**: to say that a numerical field is only valid when it’s value is above or below a certain value.
* **@Pattern**: to say that a string field is only valid when it matches a certain regular expression.
* **@Email**: to say that a string field must be a valid email address.

Here's an example of a class with validation annotations:
```java
        @NotBlank
        String name,

        @NotBlank
        @Email
        String email,

        @NotBlank
        String phone,

        @NotBlank
        @Pattern(regexp = "\\d{4,6}")
        String crm,
```

With the annotation, we can provide the default message to be returned in case of an error:
```java
        @NotBlank
        @Email(message="invalid e-mail")
        String email
```

If we want, we can create a file called ValidationMessages.properties on the src/main/resources folder and
fill it with the messages we want to display. That way, we can center all the messages in a single file.
We fill the file like we do with the application.properties:
```
email.error=invalid e-mail
name.null=name should not be null
name.empty=name should not be empty
```

## Error Handling

The Spring API provides the @RestControllerAdvice annotation to make a class into an error handler for the controllers.

Each method of this class can be annotated with @ExceptionHandler(<exceptionclassname>.class) to be called when any endpoint
throws that exception.

We can also pass the exception thrown as a parameter to the handler method so we can use the data to provide more useful
information on the error response.

## Spring Security

Objectives of Spring Security:

* Authentication
* Authorization (access control)
* Attack protection (CSRF, clickjacking, etc.)

The main dependencies to be added to the project in order to enable Spring Security are:

```xml
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.security</groupId>
        <artifactId>spring-security-test</artifactId>
        <scope>test</scope>
    </dependency>
```

After adding these dependencies, every endpoint is automatically secured, returning the 401 code (Unauthorized) by default.

The default behavior of Spring Security is to generate a random password and show a login page when the requests come from the browser.
The default username is "user". As it doesn't solve the problem of performing http requests, we need to modify the application
to enable JWT generation.

The steps are:

* Create User Entity
* Create migration to create the user table on the database
* Create User Repository interface
* Create the AuthenticationService class, implementing the UserDetailsService interface
* Create SecurityConfigurations class
* Create Authentication controller with a POST method to perform authentication
* Inject the AuthenticationManager class to the controller
* On the SecurityConfigurations class, add the AuthenticationManager Bean
* To enable BCrypt password encryption, add the Bean to the SecurityConfigurations class
* The User entity class should implement the UserDetails interface
* Add the Auth0 dependency to the project
* Implement the TokenService class
* Inject the TokenService class on the Authentication Controller

The basic SecurityConfigurations class is:
```java
    @Configuration
    @EnableWebSecurity
    public class SecurityConfigurations {

        // We need to expose the result of this method as a bean to be used throughout the application.
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

            // We can disable csrf becaus we'll be working with JWT, which already
            // protects against CSRF attacks.

            return httpSecurity.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .build();
        }
        
    }
```

The Auth0 dependency added is:
```xml
<dependency>
    <groupId>com.auth0</groupId>
    <artifactId>java-jwt</artifactId>
    <version>4.2.1</version>
</dependency>
```

After adding all these configurations, the endpoints are not protected by default anymore.

To add the authentication process to every endpoint, without adding code duplication,
we can follow the next steps:

* First, adjust the SecurityConfiguration to remove the authentication endpoint from the 
authentication chain

* Create a OncePerRequestFilter extending class, SecurityFilter in out case
* Inside the doFilterInternal method, implement token validation
* In the same method, never forget to call filterChain.doFilter() to continue the request processing
* After validating the token fetch the user info from the database and set the authentication
using the SecurityContextHolder like done in the SecurityFilter class
* Configure the filtering order to validate the token before everything else
* Configure the matchers to authorize endpoints based on the user role

To put the token filter before the default filters, add this line to the SecurityFilterChain:
```java
.and().addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
```

It's necessary to inject our security filter to the class.

To authorize the requests individually, add this line to the filter chain:
```java
.requestMatchers(HttpMethod.POST, "/medics").hasRole("ADMIN")
```

This makes the application only authorize users who have "ROLE_ADMIN" in it's authorities grant list.
To enable that feature dinamically, we created a new attribute to the User called "profile", and set this
profile as the list of autorities.

Another option is to add the annotation:
```java
@EnableMethodSecurity(securedEnabled = true)
```

to the SecurityConfigurations class, and then we can add the annotation
```java
@Secured("ROLE_ADMIN")
```

to each endpoint in the application

