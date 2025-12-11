package com.university.apigateway.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCourseRequest {
    private String code;
    private String name;
    private String description;
    private Integer credits;
    private String instructor;
    private String schedule;
    private Integer capacity;
    private String semester;
}