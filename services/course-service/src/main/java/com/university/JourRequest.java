package com.university;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class JourRequest {
    private String jour;

    public JourRequest() {} // obligatoire pour JAXB

    public JourRequest(String jour) {
        this.jour = jour;
    }

    public String getJour() { return jour; }
    public void setJour(String jour) { this.jour = jour; }
}
