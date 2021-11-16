package com.fourth.ondaeng;

public class HealthcheckData {

    private String diseaseName;
    private String explanation;
    private String cause;
    private String symptom;
    private String preventive;

    public HealthcheckData(String diseaseName, String explanation, String cause, String symptom, String preventive) {
        this.diseaseName = diseaseName;
        this.explanation = explanation;
        this.cause = cause;
        this.symptom = symptom;
        this.preventive = preventive;
    }

    public String getdiseaseName() {
        return this.diseaseName;
    }

    public String getexplanation() {
        return this.explanation;
    }

    public String getcause() {
        return this.cause;
    }

    public String getsymptom() {
        return this.symptom;
    }

    public String getpreventive() {
        return this.preventive;
    }
}
