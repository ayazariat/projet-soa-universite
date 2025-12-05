package com.university;

public class Cours {
    private int idCours;
    private String nomCours;
    private String salle;
    private String professeur;
    private String jour;
    private String heureDebut;
    private String heureFin;

    // Constructeur vide nécessaire pour JAXB
    public Cours() {}

    // Constructeur avec paramètres
    public Cours(int idCours, String nomCours, String salle, String professeur, String jour, String heureDebut, String heureFin) {
        this.idCours = idCours;
        this.nomCours = nomCours;
        this.salle = salle;
        this.professeur = professeur;
        this.jour = jour;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
    }

    // Getters et Setters
    public int getIdCours() { return idCours; }
    public String getNomCours() { return nomCours; }
    public String getSalle() { return salle; }
    public String getProfesseur() { return professeur; }
    public String getJour() { return jour; }
    public String getHeureDebut() { return heureDebut; }
    public String getHeureFin() { return heureFin; }

    public void setIdCours(int idCours) { this.idCours = idCours; }
    public void setNomCours(String nomCours) { this.nomCours = nomCours; }
    public void setSalle(String salle) { this.salle = salle; }
    public void setProfesseur(String professeur) { this.professeur = professeur; }
    public void setJour(String jour) { this.jour = jour; }
    public void setHeureDebut(String heureDebut) { this.heureDebut = heureDebut; }
    public void setHeureFin(String heureFin) { this.heureFin = heureFin; }
}
