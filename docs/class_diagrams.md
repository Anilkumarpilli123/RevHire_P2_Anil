# RevHire - Class Diagrams

Here are 3 distinct Class Diagrams breaking down the core domains of the application:

## 1. User & Profile Management
This diagram shows the relationship between the base `User` and the respective profiles for `Employer` and `JobSeeker`.

```mermaid
classDiagram
    class User {
        -int id
        -String email
        -String password
        -String role
        -boolean enabled
        -LocalDateTime createdAt
    }

    class EmployerProfile {
        -int id
        -String name
        -String phone
        -String location
        -String jobRole
        +isComplete() boolean
    }

    class JobSeekerProfile {
        -int id
        -String name
        -String phone
        -String location
        -EmploymentStatus employmentStatus
        -String companyName
        -String collegeName
        -String branch
        -String experience
        -Integer experienceYears
        -Integer profileCompletion
        +isComplete() boolean
    }

    User "1" -- "0..1" EmployerProfile : profile
    User "1" -- "0..1" JobSeekerProfile : profile
```

## 2. Job & Company Management
This diagram illustrates the relationship between `Company` and `Job` postings.

```mermaid
classDiagram
    class Company {
        -int id
        -String name
        -String industry
        -String size
        -String description
        -String website
        -String location
    }

    class Job {
        -int id
        -String title
        -String description
        -String skillsRequired
        -Integer experienceRequired
        -String educationRequired
        -String location
        -String salaryRange
        -String jobType
        -LocalDate deadline
        -String status
        -Integer numberOfOpenings
        -LocalDateTime createdAt
    }

    Company "1" *-- "*" Job : posts
    Company "1" *-- "*" EmployerProfile : employs
```

## 3. Application & Resume Management
This diagram shows the relationship between `Application`, `Resume`, and the core entities they interact with.

```mermaid
classDiagram
    class Application {
        -int id
        -String coverLetter
        -String status
        -LocalDateTime appliedDate
        -String withdrawReason
        -String employerNotes
    }

    class Resume {
        -int id
        -String objective
        -String education
        -String experience
        -String skills
        -String projects
        -String certifications
        -String resumePath
        -String originalFilename
    }

    class FavoriteJob {
        -int id
        -LocalDateTime createdAt
    }

    JobSeekerProfile "1" *-- "0..1" Resume : owns
    JobSeekerProfile "1" -- "*" Application : submits
    Job "1" -- "*" Application : receives
    Application "*" -- "0..1" Resume : attached
    JobSeekerProfile "1" -- "*" FavoriteJob : favorites
    Job "1" -- "*" FavoriteJob : favorited
```
