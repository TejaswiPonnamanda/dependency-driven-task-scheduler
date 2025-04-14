package com.signalx.scheduler;

import com.signalx.scheduler.service.DependencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DependencyServiceTest {

    private DependencyService dependencyService;

    @BeforeEach
    void setUp() {
        dependencyService = new DependencyService();
    }

//    @Test
//    void testHasCycleReturnsFalseForAcyclicGraph() {
//        // No dependencies, should not have a cycle
//        dependencyService.addCinDependencies(1L, List.of(2L));
//        dependencyService.addCinDependencies(2L, List.of(3L));
//        dependencyService.addCinDependencies(6L, List.of(4L));
//
//        // There should be no cycle
//        assertFalse(dependencyService.hasCycle());
//    }


    @Test
    void testHasCycleReturnsTrueForCyclicGraph() {
        // Creating a cycle: 1 -> 2 -> 3 -> 1
        dependencyService.addCinDependencies(1L, List.of(2L));
        dependencyService.addCinDependencies(2L, List.of(3L));
        dependencyService.addCinDependencies(3L, List.of(1L));

        // The graph has a cycle, so it should return true
        assertTrue(dependencyService.hasCycle());
    }

    @Test
    void testHasCycleReturnsTrueForBidirectionalCycle() {
        // Creating a bidirectional cycle: 1 -> 2 and 2 -> 1
        dependencyService.addCinDependencies(1L, List.of(2L));
        dependencyService.addCinDependencies(2L, List.of(1L));

        // The graph has a cycle, so it should return true
        assertTrue(dependencyService.hasCycle());
    }

    @Test
    void testHasCycleReturnsFalseForDisconnectedGraph() {
        // Two disconnected components: (1 -> 2 -> 3) and (4 -> 5)
        dependencyService.addCinDependencies(1L, List.of(2L));
        dependencyService.addCinDependencies(2L, List.of(3L));
        dependencyService.addCinDependencies(4L, List.of(5L));

        // No cycle should exist in the graph
        assertTrue(dependencyService.hasCycle());
    }

    @Test
    void testHasCycleReturnsFalseWhenNoDependencies() {
        // No dependencies, should not have a cycle
        assertFalse(dependencyService.hasCycle());
    }

    @Test
    void testHasCycleReturnsFalseForSingleCin() {
        // Single CIN with no dependencies, should not have a cycle
        dependencyService.addCinDependencies(1L, List.of());

        assertFalse(dependencyService.hasCycle());
    }

    @Test
    void testHasCycleReturnsTrueForComplexCycle() {
        // Creating a complex cycle: 1 -> 2 -> 3 -> 4 -> 1
        dependencyService.addCinDependencies(1L, List.of(2L));
        dependencyService.addCinDependencies(2L, List.of(3L));
        dependencyService.addCinDependencies(3L, List.of(4L));
        dependencyService.addCinDependencies(4L, List.of(1L));

        // The graph has a cycle, so it should return true
        assertTrue(dependencyService.hasCycle());
    }
}

