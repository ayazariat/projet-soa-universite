package com.university.apigateway.controller;

import com.university.apigateway.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private RestTemplate restTemplate;

    private final String studentServiceUrl = "http://student-service:3000";

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        try {
            String url = studentServiceUrl + "/api/students";
            ResponseEntity<Student[]> response = restTemplate.getForEntity(url, Student[].class);
            List<Student> students = Arrays.asList(response.getBody());
            return ResponseEntity.ok(students);
        } catch (Exception e) {
            // Return empty list if service is not available
            return ResponseEntity.ok(Arrays.asList());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable String id) {
        try {
            // Try to get by MongoDB _id first
            String url = studentServiceUrl + "/api/students/" + id;
            ResponseEntity<Student> response = restTemplate.getForEntity(url, Student.class);
            if (response.getBody() != null) {
                return ResponseEntity.ok(response.getBody());
            }
        } catch (Exception e) {
            // If not found by _id, try by CIN
            try {
                String url = studentServiceUrl + "/api/students/cin/" + id;
                ResponseEntity<Student> response = restTemplate.getForEntity(url, Student.class);
                return ResponseEntity.ok(response.getBody());
            } catch (Exception e2) {
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        try {
            String url = studentServiceUrl + "/api/students";
            ResponseEntity<Student> response = restTemplate.postForEntity(url, student, Student.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable String id, @RequestBody Student student) {
        try {
            // Since frontend sends CIN as id, use the CIN route directly
            String url = studentServiceUrl + "/api/students/cin/" + id;

            // Create a copy of the student object without the _id field to avoid MongoDB
            // immutable field error
            Student studentToUpdate = new Student();
            studentToUpdate.setNom(student.getNom());
            studentToUpdate.setPrenom(student.getPrenom());
            studentToUpdate.setCin(student.getCin());
            studentToUpdate.setEmail(student.getEmail());
            studentToUpdate.setTelephone(student.getTelephone());
            studentToUpdate.setNiveau(student.getNiveau());
            studentToUpdate.setGenre(student.getGenre());
            studentToUpdate.setDateDeNaissance(student.getDateDeNaissance());

            HttpEntity<Student> requestEntity = new HttpEntity<>(studentToUpdate);
            ResponseEntity<Student> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Student.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            e.printStackTrace(); // Add logging for debugging
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable String id) {
        try {
            // Since frontend sends CIN as id, use the CIN route directly
            String url = studentServiceUrl + "/api/students/cin/" + id;
            restTemplate.delete(url);
            return ResponseEntity.ok("Student deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}