package com.inde.indytrack.model;

public enum AcademicFocus {
    AI_ML("Artificial Intelligence and Machine Learning"), 
    HF("Human Factors"), 
    OR("Operations Research"), 
    IE("Information Engineering");

    private String displayName;

    AcademicFocus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
