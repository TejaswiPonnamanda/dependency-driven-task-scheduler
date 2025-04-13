package com.signalx.scheduler.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "task_execution_log") // Optional: specify table name
public class TaskExecutionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long taskId;

    @Enumerated(EnumType.STRING)
    private TaskType taskType;

    private LocalDateTime executionTime;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    // Required by JPA
    public TaskExecutionLog() {
    }

    private TaskExecutionLog(Long taskId, TaskType taskType, LocalDateTime executionTime, TaskStatus status) {
        this.taskId = taskId;
        this.taskType = taskType;
        this.executionTime = executionTime;
        this.status = status;
    }

    // Getters
    public Long getTaskId() { return taskId; }
    public TaskType getTaskType() { return taskType; }
    public LocalDateTime getExecutionTime() { return executionTime; }
    public TaskStatus getStatus() { return status; }

    // Builder pattern
    public static class Builder {
        private Long taskId;
        private TaskType taskType;
        private LocalDateTime executionTime;
        private TaskStatus status;

        public Builder taskId(Long taskId) {
            this.taskId = taskId;
            return this;
        }

        public Builder taskType(TaskType taskType) {
            this.taskType = taskType;
            return this;
        }

        public Builder executionTime(LocalDateTime executionTime) {
            this.executionTime = executionTime;
            return this;
        }

        public Builder status(TaskStatus status) {
            this.status = status;
            return this;
        }

        public TaskExecutionLog build() {
            return new TaskExecutionLog(taskId, taskType, executionTime, status);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
