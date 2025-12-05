package com.university;

import javax.xml.ws.Endpoint;

public class Publisher {
    public static void main(String[] args) {
        Endpoint.publish("http://localhost:9090/ws/courses", new CourseService());
        System.out.println("Service SOAP lancé sur http://localhost:9090/ws/courses?wsdl");
    }
}
