package com.inde.indytrack.model;

public enum CourseType {
    CORE("Core Required"), 
    TECHNICAL("Technical Elective"), 
    HSS("HSS Elective"),  
    CS("CS Elective");

    private String displayName;

    CourseType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
