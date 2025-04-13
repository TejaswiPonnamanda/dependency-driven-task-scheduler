package com.signalx.scheduler.dto;

import java.util.List;

public class DinRequest {

    private String name;
    private List<Long> cinIds;

    // Constructors
    public DinRequest() {
    }

    public DinRequest(String name, List<Long> cinIds) {
        this.name = name;
        this.cinIds = cinIds;
    }

    // Getters
    public String getName() {
        return name;
    }

    public List<Long> getCinIds() {
        return cinIds;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setCinIds(List<Long> cinIds) {
        this.cinIds = cinIds;
    }


}
