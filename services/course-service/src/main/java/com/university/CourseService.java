package com.university;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

@WebService
public class CourseService {

    private MongoCollection<Document> collection;

    // Constructeur : connexion MongoDB
    public CourseService() {
        try {
            MongoClient client = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = client.getDatabase("courseDB");
            collection = database.getCollection("cours");



            System.out.println("MongoDB connecté !");
        } catch (Exception e) {
            System.out.println("MongoDB non disponible pour l'instant. Le service démarre quand même.");
            collection = null;
        }
    }


    // Ajouter un cours — CORRIGÉ AVEC RETURN
    @WebMethod
    @WebResult(name="return")
    public boolean ajouterCours(@WebParam(name="cours") Cours cours) {

        try {
            Document doc = new Document("idCours", cours.getIdCours())
                    .append("nomCours", cours.getNomCours())
                    .append("salle", cours.getSalle())
                    .append("professeur", cours.getProfesseur())
                    .append("jour", cours.getJour())
                    .append("heureDebut", cours.getHeureDebut())
                    .append("heureFin", cours.getHeureFin());

            collection.insertOne(doc);
            return true;   // ✔ SOAP UI affichera <return>true</return>
        } catch (Exception e) {
            return false;
        }
    }

    // Obtenir tous les cours
    @WebMethod
    public List<Cours> getCours() {
        List<Cours> list = new ArrayList<>();

        for (Document doc : collection.find()) {
            Cours c = new Cours(
                    doc.getInteger("idCours"),
                    doc.getString("nomCours"),
                    doc.getString("salle"),
                    doc.getString("professeur"),
                    doc.getString("jour"),
                    doc.getString("heureDebut"),
                    doc.getString("heureFin")
            );
            list.add(c);
        }
        return list;
    }

    // Obtenir les cours par jour
    @WebMethod
    public List<Cours> getCoursParJour(@WebParam(name="jour") String jour) {
        List<Cours> list = new ArrayList<>();

        for (Document doc : collection.find(Filters.eq("jour", jour))) {
            Cours c = new Cours(
                    doc.getInteger("idCours"),
                    doc.getString("nomCours"),
                    doc.getString("salle"),
                    doc.getString("professeur"),
                    doc.getString("jour"),
                    doc.getString("heureDebut"),
                    doc.getString("heureFin")
            );
            list.add(c);
        }
        return list;
    }

    // Supprimer un cours par ID — CORRIGÉ AVEC RETURN
    @WebMethod
    @WebResult(name="return")
    public boolean supprimerCours(@WebParam(name="id") int id) {
        long count = collection.deleteOne(Filters.eq("idCours", id)).getDeletedCount();
        return count > 0;   // ✔ SOAP UI affichera <return>true</return>
    }

    // Obtenir un cours par ID
    @WebMethod
    public Cours getCoursById(@WebParam(name="id") int id) {
        Document doc = collection.find(Filters.eq("idCours", id)).first();

        if (doc == null) return null;

        return new Cours(
                doc.getInteger("idCours"),
                doc.getString("nomCours"),
                doc.getString("salle"),
                doc.getString("professeur"),
                doc.getString("jour"),
                doc.getString("heureDebut"),
                doc.getString("heureFin")
        );
    }
}
