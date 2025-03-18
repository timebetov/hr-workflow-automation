# Task 6

## Requirements
- Use Stream API for data processing (15%).
- Use Stream API inside REST endpoints to process and return filtered data (at least 2 /filter endpoints to get filtered data from Elasticsearch by request param/body by building custom queries (no need to build complicated ones)) (15%).
- Implement lambda expressions & functional interfaces (10%).
- Use Optional to handle null values safely (15%).
- Work with LocalDate or LocalTime, or LocalDateTime (15%).
- Compare sequential vs. parallel streams for performance (10%).
- Apply reduction operations with reduce() (5%).
- Group and partition data using Collectors (10%).
- Write JUnit tests for Java 8 features, coverage must be at least 85% (10%).


## Notice
I've changed my project structure. Merged two microservices: UserService with AuthService. And There is a new microservice merged version is IdentityService. Manages for storing, authentication and authorization.
But not completed authentication yet. I am doing it. Also I added API Gateway, currently working with it.

## Implementation
Implemented all these requirements inside Job Service. Cause my jobs stored in elasticsearch.
And this only one microservice using it. So, here are all endpoints:

### ENDPOINTS
#### BASIC CRUD Operations
* TO CREATE A NEW JOB `POST /job-app/api/jobs`
```json
{
  "title": "Junior Java Developer",
  "description": "Develop and maintain microservices using Spring Boot",
  "department": "IT",
  "deadline": "2025-03-20",
  "salary": 400000,
  "jobType": "FULL_TIME"
}
```
* TO GET ALL JOBS `GET /jobs-app/api/jobs`
* TO GET JOB BY ID `GET /jobs-app/api/jobs/{id}`
* TO UPDATE JOB STATUS BY ID `POST /jobs-app/api/jobs/{id}?status=closed`
* TO DELETE JOB BY ID `DELETE /jobs-app/api/jobs/{id}`
* TO GET CURRENT JOB STATUS `GET /jobs-app/api/jobs/checkStatus/{id}`

#### Using StreamAPI
* TO GET FILTERED DATA with RequestBody

`POST /jobs-app/api/jobs/filter`
```json
{
  "title": "someTitle",
  "department": "IT",
  "status": "OPEN",
  "minSalary": 350000,
  "maxSalary": 1500000,
  "jobType": "FULL_TIME",
  "postedAfter": "2025-03-15",
  "postedBefore": "2025-04-01"
}
```
* TO GET FILTERED DATA with RequestParam All fields are optional
`GET /jobs-app/api/jobs/search?title=sometitle&department=it&minSalary=350000&maxSalary=1500000`
* TO COMPARE sequential and parallel streams for performance
`GET /jobs-app/api/jobs/performance`
* REDUCTION operation with method `reduce()`
* It should return the department with highest salary in that department
`GET /jobs-app/api/jobs/highest-salary`
* FUNCTIONAL INTERFACE `Predicate` that I used in filter method
* It should return all vacancies grouping them by department and by job status
`GET /jobs-app/api/jobs/jobs-by-department`
* TO GET ALL JOBS and group them by status
* in `true` all jobs with status OPEN
* in `false` all jobs with status CLOSED
`GET /jobs-app/api/jobs/jobs-by-status`