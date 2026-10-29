# Dependency upgrade

This template requires Java 17 and a Jakarta Servlet 6 container such as Tomcat 10.1. The Spring modules move together to 6.2.19; a single Spring 6 or 7 module mixed with Spring 4 is incompatible.

Run `mvn -Plocal verify` to compile, run the complete test suite, and package the WAR. Run `mvn -Plocal package cargo:run` to start the configured Tomcat 10.1.61 container at `/`, using the existing local application database settings. Cargo replaces the obsolete custom Tomcat 8.5 plugin. The old Tomcat 7 deployment profile is removed; deploy the WAR through your Jakarta container's deployment mechanism.

API documentation moves from `/v2/api-docs` to `/v3/api-docs`. Swagger UI remains at `/swagger-ui.html`. The configured `api.path` still filters documented operations. Setting `application.devel=false` disables Swagger UI and the development resource handlers.

Springfox is replaced by Springdoc, servlet and validation imports use Jakarta, multipart parsing uses the servlet container, and JSP forms use `modelAttribute`. Default message converters are retained so OpenAPI byte responses remain JSON objects. The unused AsyncRestTemplate bean is removed because Spring 6 removed that API; the existing RestTemplate bean remains available.

The compatible maintenance lines are Spring 6.2, MyBatis Spring 3.0, Jackson 2.18, Logback 1.5 with SLF4J 2, POI 5.5, jQuery 3.7, and Font Awesome 5.15. Major upgrades to Spring 7, MyBatis Spring 4, jQuery 4, and Font Awesome 7 require separate application changes. WebJar references match the resolved package paths, including the changed Moment and Font Awesome layouts.

Validation used Maven 3.9.16 and Temurin 17.0.21. All eight tests pass. A real Tomcat 10.1.61 instance using disposable MariaDB 10.11 databases loaded from the repository SQL served the login JSP, filtered OpenAPI JSON object, Swagger UI, Swagger JavaScript, and all eight referenced WebJar assets successfully. The tests also check `application.devel=false`, Jakarta validation, Joda JSON, and converter ordering. Production deployment and a complete authenticated browser workflow were not performed.
