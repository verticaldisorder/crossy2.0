package ru.rmp.crossy20.models;

public class Exchange {
    String exchangeId;
    String accepted;
    String applicant;
    String book;
    boolean isCrossedByAccepted;
    boolean isCrossedByApplicant;

    public Exchange(String exchangeId, String accepted, String applicant, String book) {
        this.exchangeId = exchangeId;
        this.accepted = accepted;
        this.applicant = applicant;
        this.book = book;
        this.isCrossedByAccepted = false;
        this.isCrossedByApplicant = false;
    }

    public Exchange(String exchangeId, String accepted, String applicant, String book, boolean isCrossedByAccepted, boolean isCrossedByApplicant) {
        this.exchangeId = exchangeId;
        this.accepted = accepted;
        this.applicant = applicant;
        this.book = book;
        this.isCrossedByAccepted = isCrossedByAccepted;
        this.isCrossedByApplicant = isCrossedByApplicant;
    }

    public String getAccepted() {
        return accepted;
    }

    public void setAccepted(String accepted) {
        this.accepted = accepted;
    }

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public boolean isCrossedByAccepted() {
        return isCrossedByAccepted;
    }

    public void setCrossedByAccepted(boolean crossedByAccepted) {
        isCrossedByAccepted = crossedByAccepted;
    }

    public boolean isCrossedByApplicant() {
        return isCrossedByApplicant;
    }

    public void setCrossedByApplicant(boolean crossedByApplicant) {
        isCrossedByApplicant = crossedByApplicant;
    }

    public String getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(String exchangeId) {
        this.exchangeId = exchangeId;
    }

    @Override
    public String toString() {
        return "Exchange{" +
                "exchangeId='" + exchangeId + '\'' +
                ", accepted='" + accepted + '\'' +
                ", applicant='" + applicant + '\'' +
                ", book='" + book + '\'' +
                ", isCrossedByAccepted=" + isCrossedByAccepted +
                ", isCrossedByApplicant=" + isCrossedByApplicant +
                '}';
    }
}
