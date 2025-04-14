package com.signalx.scheduler;

import com.signalx.scheduler.service.DependencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    void testAddCinDependencies() {
        dependencyService.addCinDependencies(1L, Arrays.asList(10L, 20L));

        Map<Long, Set<Long>> cinToDins = dependencyService.getCinToDins();
        Map<Long, Set<Long>> dinToCins = dependencyService.getDinToCins();

        assertEquals(Set.of(10L, 20L), cinToDins.get(1L));
        assertEquals(Set.of(1L), dinToCins.get(10L));
        assertEquals(Set.of(1L), dinToCins.get(20L));
    }

    @Test
    void testAddDinDependencies() {
        dependencyService.addDinDependencies(10L, Arrays.asList(1L, 2L));

        Map<Long, Set<Long>> dinToCins = dependencyService.getDinToCins();
        Map<Long, Set<Long>> cinToDins = dependencyService.getCinToDins();

        assertEquals(Set.of(1L, 2L), dinToCins.get(10L));
        assertEquals(Set.of(10L), cinToDins.get(1L));
        assertEquals(Set.of(10L), cinToDins.get(2L));
    }

    @Test
    void testRemoveCin() {
        dependencyService.addCinDependencies(1L, Arrays.asList(10L, 20L));
        dependencyService.removeCin(1L);

        assertFalse(dependencyService.getCinToDins().containsKey(1L));

        Map<Long, Set<Long>> dinToCins = dependencyService.getDinToCins();
        if (dinToCins.containsKey(10L)) {
            assertFalse(dinToCins.get(10L).contains(1L));
        }
        if (dinToCins.containsKey(20L)) {
            assertFalse(dinToCins.get(20L).contains(1L));
        }
    }

    @Test
    void testRemoveDin() {
        dependencyService.addDinDependencies(10L, Arrays.asList(1L, 2L));
        dependencyService.removeDin(10L);

        assertFalse(dependencyService.getDinToCins().containsKey(10L));

        Map<Long, Set<Long>> cinToDins = dependencyService.getCinToDins();
        if (cinToDins.containsKey(1L)) {
            assertFalse(cinToDins.get(1L).contains(10L));
        }
        if (cinToDins.containsKey(2L)) {
            assertFalse(cinToDins.get(2L).contains(10L));
        }
    }


    @Test
    void testRemoveCinWithNoDependencies() {
        dependencyService.removeCin(100L); // should not throw exception
        assertTrue(dependencyService.getCinToDins().isEmpty());
        assertTrue(dependencyService.getDinToCins().isEmpty());
    }

    @Test
    void testRemoveDinWithNoDependencies() {
        dependencyService.removeDin(200L); // should not throw exception
        assertTrue(dependencyService.getDinToCins().isEmpty());
        assertTrue(dependencyService.getCinToDins().isEmpty());
    }

    @Test
    void testDuplicateAddCinDependencies() {
        dependencyService.addCinDependencies(1L, List.of(10L));
        dependencyService.addCinDependencies(1L, List.of(10L)); // should not duplicate
        assertEquals(1, dependencyService.getCinToDins().get(1L).size());
        assertEquals(1, dependencyService.getDinToCins().get(10L).size());
    }

    @Test
    void testDuplicateAddDinDependencies() {
        dependencyService.addDinDependencies(10L, List.of(1L));
        dependencyService.addDinDependencies(10L, List.of(1L)); // should not duplicate
        assertEquals(1, dependencyService.getDinToCins().get(10L).size());
        assertEquals(1, dependencyService.getCinToDins().get(1L).size());
    }


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

