# Shopify Inventory Management System

### TASK:
Build an inventory tracking web application for a logistics company.
We are looking for a web application that meets the requirements listed below, 
along with one additional feature, with the options also listed below.

### Requirements:
- Basic CRUD Functionality. You should be able to:
  - Create inventory items 
  - Edit Them 
  - Delete Them 
  - View a list of them

- Additional functionality:
  - Push a button export product data to a CSV

### Tech Stack used:
- Java
- [Spring boot](https://spring.io/projects/spring-boot)
- [Jooq](https://www.jooq.org/)
- [Postgres](https://www.postgresql.org/)
- [Gradle](https://gradle.org/)

### Installation:
- Install postgres for the required OS using the link above.
- Create a DB called "shopify-ims-db".
- Update the `spring.datasource.url`, `spring.datasource.username`, `spring.datasource.password` with the dbUrl, username and password respectively in `src/main/resources/application.properties`.
- Update the dbUrl, username, password of `flyway` and `jooq` configurations in`build.gradle`.
- The project is bundled with gradle, so you do not have to download it.
- Run `bootRun` task with gradle wrapper.
- Installing an IDE like IntelliJIDEA is recommended as access to gradle commands become easier.
- Note: DB schema is located in [./src/main/resources/db/migration/V1__initdb.sql](./src/main/resources/db/migration/V1__initdb.sql)

### Testing and Using
The backend service has been developed with a [OpenAPI](https://www.openapis.org/) [spec](./reference/api-spec.yaml)
- Please install [Postman](https://www.postman.com/) as this service does not have any front end.
- Import the API spec located in `./reference/api-spec.yaml`.
- Change the port in the Postman variables if needed.

You should be able to test out all the feature that have been implemented.


### Contact Info:
email: `aditarun2008@gmail.com`, `aditya.subramani.ufl.edu`