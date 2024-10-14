package com.example.youthconnectproject;

public class Events {
    private String eventID;
    private String eventName;
    private String eventDate;
    private String eventDescription;
    private String eventActivities;
    private String eventUploaded;

    public Events() {}

    public Events(String eventID, String eventName, String eventDate, String eventDescription, String eventActivities, String eventUploaded) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventDescription = eventDescription;
        this.eventActivities = eventActivities;
        this.eventUploaded = eventUploaded;
    }

    public String getEventID() {
        return eventID;
    }
    public void setEventID(String eventID) {
        this.eventID = eventID;
    }
    public String getEventName() {
        return eventName;
    }
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
    public String getEventDate() {
        return eventDate;
    }
    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }
    public String getEventDescription() {
        return eventDescription;
    }
    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }
    public String getEventActivities() {
        return eventActivities;
    }
    public void setEventActivities(String eventActivities) {
        this.eventActivities = eventActivities;
    }
    public String getEventUploaded() {
        return eventUploaded;
    }
    public void setEventUploaded(String eventUploaded) {
        this.eventUploaded = eventUploaded;
    }
}
