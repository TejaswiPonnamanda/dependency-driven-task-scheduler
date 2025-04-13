package com.signalx.scheduler.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.signalx.scheduler.model.Din;

import com.signalx.scheduler.model.TaskStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Cin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private TaskStatus status = TaskStatus.PENDING;

    private LocalDateTime lastExecuted;

    private int roundRobinIndex;

    @ManyToMany
    @JoinTable(
            name = "Cin_Din_Dependency",
            joinColumns = @JoinColumn(name = "cin_id"),
            inverseJoinColumns = @JoinColumn(name = "din_id")
    )
    private Set<Din> dependencies;


    // Default constructor
    public Cin() {}

    // Parameterized constructor
    public Cin(String name, TaskStatus status, LocalDateTime lastExecuted, int roundRobinIndex, Set<Din> dependencies) {
        this.name = name;
        this.status = status;
        this.lastExecuted = lastExecuted;
        this.roundRobinIndex = roundRobinIndex;
        this.dependencies = dependencies;
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

    public Set<Din> getDependencies() {
        return dependencies;
    }

    public void setDependencies(Set<Din> dependencies) {
        this.dependencies = dependencies;
    }
}



