package com.university.apigateway.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    private String _id;
    private String nom;
    private String prenom;
    private String cin;
    private String email;
    private String telephone;
    private String niveau;
    private String genre;
    private String dateDeNaissance;
    private String createdAt;
    private String updatedAt;
}