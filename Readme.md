# Task 4

* Here 3 microservices
* They are communicating with each other via only Kafka (NO REST)
* Used AOP for logging and exception handling in each microservice
* Integrated fully ELK stack (Elasticsearch, logstash, kibana)
* Added validation
* Used Spring Boot 3.x.x and Java 17


Here basic CRUD methods. Also methods like:
1. ApplyForJob in UserService
2. ChangeApplicationStatus in UserService

### Apply For Job - method workflow
1. UserService sends to kafka topic `[application.created]` `jobId` && `candidateId`.
2. Workflow service consume this topic also, and gets the json format.
3. And then Workflow service sends the message to kafka topic `[job-req]` containing `jobId`, `candidateId` to verify if job is not closed.
4. JobService accepts these credentials and checks if vacancy is open or not. And sends to Kafka topic `[job-res]` message containing `jobId`, `status`, `candidateId`.
5. And Workflow service accepts these and if job is closed its going to log and end the method execution.
6. Otherwise its going to create a new application. Also it sends the to Kafka `[logs.info]` and logs it.
7. Also only candidate can apply for job.

### Change job status - method workflow
1. Only HR or Admins can change the stauts of job
2. And from `PENDING` can transit to => `INVITED`, `REJECTED`, `WITHDRAWN`
3. From `INVITED` to => `HIRED`, `REJECTED`, `DECLINED`

All screenshots are in the folder `screenshots`.