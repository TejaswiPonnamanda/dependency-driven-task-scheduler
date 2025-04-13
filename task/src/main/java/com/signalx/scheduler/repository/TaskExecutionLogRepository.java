package com.signalx.scheduler.repository;


import com.signalx.scheduler.model.TaskExecutionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskExecutionLogRepository extends JpaRepository<TaskExecutionLog, Long> {
}
