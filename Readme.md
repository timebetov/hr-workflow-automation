# Task 5

* Each microservice communicating with each other via Feign
* New Auth Service is responsible for registering new users and generating the JWT token

### NOT IMPLEMENTED
* Unit and Integration Testing
* JWT authentication IDK but here's a problem when using OncePerRequestFilter

## ENDPOINTS

* TO REGISTER A NEW USER `POST /auth-app/api/auth/register`
```json
{
  "firstName": "someName",
  "lastName": "someLastName",
  "email": "example@mail.com",
  "password": "somePassword12345",
  "position": "Java Developer"
}
```

* TO LOGIN AND GET THE JWT TOKEN `POST /auth-app/api/auth/login`
```json
{
  "email": "example@mail.com",
  "password": "somePassword12345"
}
```

* TO CREATE A NEW VACANCY `POST /jobs-app/api/jobs`
```json
{
  "title": "someTitle",
  "description": "someDescription",
  "department": "Developer"
}
```

* TO GET ALL VACANCIES `GET /jobs-app/api/jobs`
* TO GET A VACANCY BY ID `GET /jobs-app/api/jobs/{jobId}`
* TO GET ALL VACANCIES BY STATUS `GET /jobs-app/api/jobs/status/{status}`
* TO APPLY FOR JOB `POST /users-app/api/applies/apply`
```json
{
  "jobId": "jobId",
  "candidateId": 1
}
```
* TO CHANGE THE JOB STATUS `POST /users-app/api/applies/{appid}`
```json
{
  "userId": 1,
  "newStatus": "INVITED"
}
```