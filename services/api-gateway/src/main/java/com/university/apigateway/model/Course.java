package com.university.apigateway.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    private int idCours;
    private String nomCours;
    private String salle;
    private String professeur;
    private String jour;
    private String heureDebut;
    private String heureFin;
}