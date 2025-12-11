package com.university.apigateway.controller;

import com.university.apigateway.model.Course;
import com.university.apigateway.model.CourseResponse;
import com.university.apigateway.model.Cours;
import com.university.apigateway.model.CreateCourseRequest;
import com.university.apigateway.model.UpdateCourseRequest;
import com.university.apigateway.service.CourseServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseServiceClient courseServiceClient;

    @GetMapping
    public ResponseEntity<List<CourseResponse>> getAllCourses() {
        List<Cours> coursList = courseServiceClient.getAllCourses();
        List<CourseResponse> courses = coursList.stream()
                .map(this::convertToCourseResponse)
                .collect(Collectors.toList());
        // TODO: Remove this mock data once SOAP integration is complete
        if (courses.isEmpty()) {
            courses = Arrays.asList(
                    new CourseResponse("1", "MATH101", "Mathematics", "Introduction to Mathematics", 3, "Dr. Smith",
                            "Mon/Wed 9:00-10:30", 30, 25, "Fall 2024", "ACTIVE"),
                    new CourseResponse("2", "PHYS101", "Physics", "Introduction to Physics", 4, "Dr. Johnson",
                            "Tue/Thu 11:00-12:30", 25, 20, "Fall 2024", "ACTIVE"));
        }
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getCourseById(@PathVariable String id) {
        try {
            int courseId = Integer.parseInt(id);
            Cours cours = courseServiceClient.getCourseById(courseId);
            if (cours == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(convertToCourseResponse(cours));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<String> addCourse(@RequestBody CreateCourseRequest courseRequest) {
        // For now, just return success since SOAP integration is pending
        // TODO: Implement actual course creation via SOAP service
        return ResponseEntity.ok("Course added successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCourse(@PathVariable String id, @RequestBody UpdateCourseRequest course) {
        // For now, just return success since SOAP integration is pending
        // TODO: Implement actual course update via SOAP service
        return ResponseEntity.ok("Course updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCourse(@PathVariable String id) {
        try {
            boolean success = courseServiceClient.deleteCourse(Integer.parseInt(id));
            if (success) {
                return ResponseEntity.ok("Course deleted successfully");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting course: " + e.getMessage());
        }
    }

    private CourseResponse convertToCourseResponse(Cours cours) {
        return new CourseResponse(
                String.valueOf(cours.getIdCours()),
                "COURSE" + cours.getIdCours(),
                cours.getNomCours(),
                "Course: " + cours.getNomCours(),
                3, // Default credits
                cours.getProfesseur(),
                cours.getJour() + " " + cours.getHeureDebut() + "-" + cours.getHeureFin(),
                30, // Default capacity
                0, // Default enrolled
                "Fall 2024", // Default semester
                "ACTIVE" // Default status
        );
    }
}