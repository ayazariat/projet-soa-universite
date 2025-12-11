package com.university.apigateway.service;

import com.university.apigateway.model.Course;
import com.university.apigateway.model.Cours;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class CourseServiceClient {

    private final RestTemplate restTemplate;

    public CourseServiceClient() {
        this.restTemplate = new RestTemplate();
    }

    public List<Cours> getAllCourses() {
        try {
            // TODO: Implement SOAP call to course-service
            // For now, return empty list
            return Arrays.asList();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get courses", e);
        }
    }

    public Cours getCourseById(int id) {
        try {
            // TODO: Implement SOAP call to course-service
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get course by id", e);
        }
    }

    public List<Cours> getCoursesByDay(String day) {
        try {
            // TODO: Implement SOAP call to course-service
            return Arrays.asList();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get courses by day", e);
        }
    }

    public boolean addCourse(Course course) {
        try {
            // TODO: Implement SOAP call to course-service
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Failed to add course", e);
        }
    }

    public boolean deleteCourse(int id) {
        try {
            // TODO: Implement SOAP call to course-service
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete course", e);
        }
    }
}