package com.inde.indytrack.model;

public enum CourseType {
    CORE("Core Required"), 
    TECHNICAL("Technical Elective"), 
    HSS("Humanities and Social Sciences Elective"),  
    CS("Complementary Studies Elective");

    private String displayName;

    CourseType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
