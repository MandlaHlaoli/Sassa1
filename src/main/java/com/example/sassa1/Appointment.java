package com.example.sassa1;

public class Appointment
{
    private String objectId;
    private String fullNames;
    private String surname;
    private String idNumber;
    private String residentialAddress;
    private String nearestSassaStation;
    private String grantType;
    private String grandFor;
    private String bookDate;
    private String otherRequest;
    private String bookingNumber;
    private String time;
    private  String counterNumber;
    private  String queueNumber;
    private  String email;


    public Appointment()
    {

        objectId = null;
        email = null;
        time = null;
        counterNumber = null;
        queueNumber = null;
        fullNames = null;
        surname = null;
        idNumber = null;
        residentialAddress = null;
        nearestSassaStation = null;
        grantType = null;
        grandFor = null;
        bookDate = null;
        otherRequest = null;
        bookingNumber = null;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getFullNames() {
        return fullNames;
    }

    public void setFullNames(String fullNames) {
        this.fullNames = fullNames;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getResidentialAddress() {
        return residentialAddress;
    }

    public void setResidentialAddress(String residentialAddress) {
        this.residentialAddress = residentialAddress;
    }

    public String getNearestSassaStation() {
        return nearestSassaStation;
    }

    public void setNearestSassaStation(String nearestSassaStation) {
        this.nearestSassaStation = nearestSassaStation;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getGrandFor() {
        return grandFor;
    }

    public void setGrandFor(String grandFor) {
        this.grandFor = grandFor;
    }

    public String getBookDate() {
        return bookDate;
    }

    public void setBookDate(String bookDate) {
        this.bookDate = bookDate;
    }

    public String getOtherRequest() {
        return otherRequest;
    }

    public void setOtherRequest(String otherRequest) {
        this.otherRequest = otherRequest;
    }

    public String getBookingNumber() {
        return bookingNumber;
    }

    public void setBookingNumber(String bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCounterNumber() {
        return counterNumber;
    }

    public void setCounterNumber(String counterNumber) {
        this.counterNumber = counterNumber;
    }

    public String getQueueNumber() {
        return queueNumber;
    }

    public void setQueueNumber(String queueNumber) {
        this.queueNumber = queueNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
