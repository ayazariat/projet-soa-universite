package com.university.apigateway.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponse {
    private String id;
    private String code;
    private String name;
    private String description;
    private int credits;
    private String instructor;
    private String schedule;
    private int capacity;
    private int enrolled;
    private String semester;
    private String status;
}