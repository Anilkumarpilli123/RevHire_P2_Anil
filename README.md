# RevHire Job Portal Backend

RevHire is a comprehensive job portal application designed to connect job seekers with potential employers. The platform facilitates a streamlined recruitment process, allowing employers to post job openings and seekers to apply for them. The system also includes an administrative dashboard for managing users and job postings.

This repository contains the **Spring Boot backend** for the RevHire application.

## Key Features

*   **User Authentication**: Secure registration and login for Seekers, Employers, and Admins using JSON Web Tokens (JWT).
*   **Job Management**: Employers can create, read, update, and delete job postings.
*   **Application Tracking**: Seekers can apply for jobs, and employers can track applications.
*   **Profile Management**: Users can manage their profiles and resumes.
*   **Admin Dashboard**: Comprehensive view and management of system activities.

## Technological Stack

*   **Backend Framework**: Spring Boot 2.7.x
*   **Language**: Java (compatible with 8/17)
*   **Database**: Oracle Database
*   **ORM**: Spring Data JPA
*   **Security**: Spring Security + JWT Authentication
*   **Templating**: Thymeleaf (for certain server-rendered views)
*   **Build Tool**: Maven

## Prerequisites

Before you begin, ensure you have the following installed:
*   [Java Development Kit (JDK) 8 or 17](https://www.oracle.com/java/technologies/downloads/)
*   [Apache Maven](https://maven.apache.org/download.cgi)
*   [Oracle Database](https://www.oracle.com/database/technologies/appdev/xe.html)

## Setup and Installation

1.  **Clone the Repository**
    ```bash
    git clone <repository_url>
    cd P2_RevHire
    ```

2.  **Database Configuration**
    Update the database connection settings in `src/main/resources/application.properties` (or `.yml`) to match your Oracle DB instance:
    ```properties
    spring.datasource.url=jdbc:oracle:thin:@//localhost:1521/XEPDB1
    spring.datasource.username=your_username
    spring.datasource.password=your_password
    ```

3.  **Build the Project**
    Use Maven to clean and package the application:
    ```bash
    mvn clean package
    ```

4.  **Run the Application**
    You can run the application directly using the Spring Boot Maven plugin:
    ```bash
    mvn spring-boot:run
    ```
    Or run the generated packaged application in the `target/` directory:
    *(Note: The project is configured to produce a WAR for deployment on servers like IBM WebSphere or embedded Tomcat)*
    ```bash
    java -jar target/revhire-x.x.x.war
    ```

The application will typically start on `http://localhost:8080` (or `8081` depending on your `server.port` configuration).

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details (if applicable).
