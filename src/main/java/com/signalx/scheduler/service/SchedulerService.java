package com.signalx.scheduler.service;

import com.signalx.scheduler.model.*;
import com.signalx.scheduler.repository.CinRepository;
import com.signalx.scheduler.repository.DinRepository;
import com.signalx.scheduler.repository.TaskExecutionLogRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.*;
//
//@Service
//public class SchedulerService {
//
//
//    private static final Logger log = LoggerFactory.getLogger(SchedulerService.class);
//
//    @Autowired
//    private  CinRepository cinRepository;
//
//    @Autowired
//    private  DinRepository dinRepository;
//
//    @Autowired
//    private  TaskExecutionLogRepository logRepository;
//
//    @Autowired
//    private  DependencyService dependencyService;
//
//    private final Queue<Long> cinQueue = new LinkedList<>();
//    private final Queue<Long> dinQueue = new LinkedList<>();
//    private final Set<Long> executedCins = new HashSet<>();
//    private final Set<Long> executedDins = new HashSet<>();
//
//    public void scheduleTasks() {
//
//        cinQueue.addAll(cinRepository.findAll().stream().map(Cin::getId).toList());
//        dinQueue.addAll(dinRepository.findAll().stream().map(Din::getId).toList());
//
//        while (!cinQueue.isEmpty() || !dinQueue.isEmpty()) {
//
//            if (!cinQueue.isEmpty()) {
//                Long cinId = cinQueue.poll();
//                Set<Long> dinDeps = dependencyService.getCinToDins().getOrDefault(cinId, Set.of());
//                if (executedDins.containsAll(dinDeps)) {
//                    logExecution(cinId, TaskType.CIN);
//                    executedCins.add(cinId);
//                } else {
//                    cinQueue.offer(cinId); // Retry later
//                }
//            }
//
//            if (!dinQueue.isEmpty()) {
//                Long dinId = dinQueue.poll();
//                Set<Long> cinDeps = dependencyService.getDinToCins().getOrDefault(dinId, Set.of());
//                if (executedCins.containsAll(cinDeps)) {
//                    logExecution(dinId, TaskType.DIN);
//                    executedDins.add(dinId);
//                } else {
//                    dinQueue.offer(dinId); // Retry later
//                }
//            }
//
//            // Deadlock detection (all tasks pending but no progress)
//            if (!cinQueue.isEmpty() && !dinQueue.isEmpty() &&
//                    executedCins.size() + executedDins.size() == 0 &&
//                    dependencyService.hasCycle()) {
//                throw new IllegalStateException("Deadlock detected in dependencies!");
//            }
//        }
//
//        executedCins.clear();
//        executedDins.clear();
//    }
//
//    private void logExecution(Long taskId, TaskType type) {
//        TaskExecutionLog logEntry = TaskExecutionLog.builder()
//                .taskId(taskId)
//                .taskType(type)
//                .executionTime(LocalDateTime.now())
//                .status(TaskStatus.COMPLETED)
//                .build();
//        logRepository.save(logEntry);
//        log.info("Executed {} with ID {}", type, taskId);
//    }
//}
@Service
public class SchedulerService {

    private static final Logger log = LoggerFactory.getLogger(SchedulerService.class);

    @Autowired
    private CinRepository cinRepository;

    @Autowired
    private DinRepository dinRepository;

    @Autowired
    private TaskExecutionLogRepository logRepository;

    @Autowired
    private DependencyService dependencyService;

    public void scheduleTasks() {
        // Track in-degree for each task
        Map<Long, Integer> cinInDegree = new HashMap<>();
        Map<Long, Integer> dinInDegree = new HashMap<>();

        // Initialize in-degree
        for (Long cinId : dependencyService.getCinToDins().keySet()) {
            cinInDegree.put(cinId, 0);
        }
        for (Long dinId : dependencyService.getDinToCins().keySet()) {
            dinInDegree.put(dinId, 0);
        }

        // Calculate in-degrees
        for (Long cinId : dependencyService.getCinToDins().keySet()) {
            for (Long dinId : dependencyService.getCinToDins().get(cinId)) {
                dinInDegree.put(dinId, dinInDegree.getOrDefault(dinId, 0) + 1);
            }
        }
        for (Long dinId : dependencyService.getDinToCins().keySet()) {
            for (Long cinId : dependencyService.getDinToCins().get(dinId)) {
                cinInDegree.put(cinId, cinInDegree.getOrDefault(cinId, 0) + 1);
            }
        }

        // Queues for tasks that are ready to execute
        Queue<Long> cinQueue = new LinkedList<>();
        Queue<Long> dinQueue = new LinkedList<>();

        // Add tasks with in-degree zero to the ready queue
        for (Long cinId : cinInDegree.keySet()) {
            if (cinInDegree.get(cinId) == 0) {
                cinQueue.offer(cinId);
            }
        }
        for (Long dinId : dinInDegree.keySet()) {
            if (dinInDegree.get(dinId) == 0) {
                dinQueue.offer(dinId);
            }
        }


        // ✅ EARLY deadlock detection BEFORE entering the loop
        if (cinQueue.isEmpty() && dinQueue.isEmpty() && dependencyService.hasCycle()) {
            throw new IllegalStateException("Deadlock detected in dependencies!");
        }


        // Track executed tasks
        Set<Long> executedCins = new HashSet<>();
        Set<Long> executedDins = new HashSet<>();

        while (!cinQueue.isEmpty() || !dinQueue.isEmpty()) {
            // Round-robin execution
            if (!cinQueue.isEmpty()) {
                Long cinId = cinQueue.poll();
                logExecution(cinId, TaskType.CIN);
                executedCins.add(cinId);
                // Update the in-degrees of dependent Dins
                for (Long dinId : dependencyService.getCinToDins().getOrDefault(cinId, Set.of())) {
                    dinInDegree.put(dinId, dinInDegree.get(dinId) - 1);
                    if (dinInDegree.get(dinId) == 0) {
                        dinQueue.offer(dinId);
                    }
                }
            }

            if (!dinQueue.isEmpty()) {
                Long dinId = dinQueue.poll();
                logExecution(dinId, TaskType.DIN);
                executedDins.add(dinId);
                // Update the in-degrees of dependent Cins
                for (Long cinId : dependencyService.getDinToCins().getOrDefault(dinId, Set.of())) {
                    cinInDegree.put(cinId, cinInDegree.get(cinId) - 1);
                    if (cinInDegree.get(cinId) == 0) {
                        cinQueue.offer(cinId);
                    }
                }
            }

            // Deadlock detection: If no tasks are executed in this round, we might have a cycle
            if (cinQueue.isEmpty() && dinQueue.isEmpty()) {
                if (executedCins.size() + executedDins.size() == 0 || dependencyService.hasCycle()) {
                    throw new IllegalStateException("Deadlock detected in dependencies!");
                }
            }
        }
    }

    private void logExecution(Long taskId, TaskType type) {
        TaskExecutionLog logEntry = TaskExecutionLog.builder()
                .taskId(taskId)
                .taskType(type)
                .executionTime(LocalDateTime.now())
                .status(TaskStatus.COMPLETED)
                .build();
        logRepository.save(logEntry);
        log.info("Executed {} with ID {}", type, taskId);
    }
}
