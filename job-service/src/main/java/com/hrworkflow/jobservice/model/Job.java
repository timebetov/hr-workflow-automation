package com.hrworkflow.jobservice.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "jobs")
public class Job {

    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Text)
    private String department;

    @Field(type = FieldType.Keyword)
    private JobStatus status;

    @Field(type = FieldType.Date, format = DateFormat.date)
    private LocalDate postedAt;

    @Field(type = FieldType.Date, format = DateFormat.date)
    private LocalDate deadline;

    @Field(type = FieldType.Double)
    private Double salary;

    @Field(type = FieldType.Keyword)
    private JobType jobType;
}
