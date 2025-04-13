package com.signalx.scheduler.dto;

import java.util.List;

public class CinRequest {

    private String name;
    private List<Long> dinIds;

    // Constructors
    public CinRequest() {
    }

    public CinRequest(String name, List<Long> dinIds) {
        this.name = name;
        this.dinIds = dinIds;
    }

    // Getters
    public String getName() {
        return name;
    }

    public List<Long> getDinIds() {
        return dinIds;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setDinIds(List<Long> dinIds) {
        this.dinIds = dinIds;
    }
}

