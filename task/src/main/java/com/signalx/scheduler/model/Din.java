package com.signalx.scheduler.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Din {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private TaskStatus status = TaskStatus.PENDING;

    private LocalDateTime lastExecuted;

    private int roundRobinIndex;

    @ManyToMany(mappedBy = "dependencies")
    @JsonIgnore
    private Set<Cin> dependentCins;


    // Default constructor
    public Din() {}

    // Parameterized constructor
    public Din(String name, TaskStatus status, LocalDateTime lastExecuted, int roundRobinIndex, Set<Cin> dependentCins) {
        this.name = name;
        this.status = status;
        this.lastExecuted = lastExecuted;
        this.roundRobinIndex = roundRobinIndex;
        this.dependentCins = dependentCins;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public LocalDateTime getLastExecuted() {
        return lastExecuted;
    }

    public void setLastExecuted(LocalDateTime lastExecuted) {
        this.lastExecuted = lastExecuted;
    }

    public int getRoundRobinIndex() {
        return roundRobinIndex;
    }

    public void setRoundRobinIndex(int roundRobinIndex) {
        this.roundRobinIndex = roundRobinIndex;
    }

    public Set<Cin> getDependentCins() {
        return dependentCins;
    }

    public void setDependentCins(Set<Cin> dependentCins) {
        this.dependentCins = dependentCins;
    }
}




