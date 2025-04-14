//package com.signalx.scheduler.service;
//
//import org.springframework.stereotype.Service;
//
//import java.util.*;
//
//@Service
//public class DependencyService {
//
//// Bidirectional dependencies
//
//   private final Map<Long, Set<Long>> cinToDins = new HashMap<>();
//
//   private final Map<Long, Set<Long>> dinToCins = new HashMap<>();
//
//   public Map<Long, Set<Long>> getDinToCins() {
//    return dinToCins;
//   }
//
//    public Map<Long, Set<Long>> getCinToDins() {
//       return cinToDins;
//    }
//
//    public void addCinDependencies(Long cinId, List<Long> dinIds) {
//       cinToDins.putIfAbsent(cinId, new HashSet<>());
//        for (Long dinId : dinIds) {
//           cinToDins.get(cinId).add(dinId);
//           dinToCins.computeIfAbsent(dinId, k -> new HashSet<>()).add(cinId);
//       }
//   }
//
//    public void addDinDependencies(Long dinId, List<Long> cinIds) {
//    dinToCins.putIfAbsent(dinId, new HashSet<>());
//       for (Long cinId : cinIds) {
//           dinToCins.get(dinId).add(cinId);
//           cinToDins.computeIfAbsent(cinId, k -> new HashSet<>()).add(dinId);
//        }
//    }
//    public void removeCin(Long cinId) {
//       if (cinToDins.containsKey(cinId)) {
//           for (Long dinId : cinToDins.get(cinId)) {
//               dinToCins.get(dinId).remove(cinId);
//           }
//            cinToDins.remove(cinId);
//       }
//   }
//
//    public void removeDin(Long dinId) {
//        if (dinToCins.containsKey(dinId)) {
//           for (Long cinId : dinToCins.get(dinId)) {
//                cinToDins.get(cinId).remove(dinId);
//           }
//            dinToCins.remove(dinId);
//        }
//   }
//
//    // For deadlock detection using DFS
//    public boolean hasCycle() {
//        Set<Long> visited = new HashSet<>();
//        Set<Long> recursionStack = new HashSet<>();
//
//        for (Long cinId : cinToDins.keySet()) {
//            if (detectCycleDFS(cinId, visited, recursionStack)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private boolean detectCycleDFS(Long current, Set<Long> visited, Set<Long> stack) {
//        if (stack.contains(current)) return true;
//        if (visited.contains(current)) return false;
//
//        visited.add(current);
//        stack.add(current);
//
//        for (Long dinId : cinToDins.getOrDefault(current, Set.of())) {
//            for (Long nextCin : dinToCins.getOrDefault(dinId, Set.of())) {
//                if (detectCycleDFS(nextCin, visited, stack)) return true;
//            }
//        }
//
//        stack.remove(current);
//        return false;
//    }
//}
package com.signalx.scheduler.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DependencyService {

    // Bidirectional dependencies
    private final Map<Long, Set<Long>> cinToDins = new HashMap<>();
    private final Map<Long, Set<Long>> dinToCins = new HashMap<>();

    public Map<Long, Set<Long>> getDinToCins() {
        return dinToCins;
    }

    public Map<Long, Set<Long>> getCinToDins() {
        return cinToDins;
    }

    public void addCinDependencies(Long cinId, List<Long> dinIds) {
        cinToDins.putIfAbsent(cinId, new HashSet<>());
        for (Long dinId : dinIds) {
            if (cinToDins.get(cinId).add(dinId)) { // Only add if not already present
                dinToCins.computeIfAbsent(dinId, k -> new HashSet<>()).add(cinId);
            }
        }
    }

    public void addDinDependencies(Long dinId, List<Long> cinIds) {
        dinToCins.putIfAbsent(dinId, new HashSet<>());
        for (Long cinId : cinIds) {
            if (dinToCins.get(dinId).add(cinId)) { // Only add if not already present
                cinToDins.computeIfAbsent(cinId, k -> new HashSet<>()).add(dinId);
            }
        }
    }

    public void removeCin(Long cinId) {
        Set<Long> dinIds = cinToDins.remove(cinId);
        if (dinIds != null) {
            for (Long dinId : dinIds) {
                dinToCins.get(dinId).remove(cinId);
                if (dinToCins.get(dinId).isEmpty()) {
                    dinToCins.remove(dinId);
                }
            }
        }
    }

    public void removeDin(Long dinId) {
        Set<Long> cinIds = dinToCins.remove(dinId);
        if (cinIds != null) {
            for (Long cinId : cinIds) {
                cinToDins.get(cinId).remove(dinId);
                if (cinToDins.get(cinId).isEmpty()) {
                    cinToDins.remove(cinId);
                }
            }
        }
    }

//    // Optimized deadlock detection using DFS
//    public boolean hasCycle() {
//        Set<Long> visited = new HashSet<>();
//        Set<Long> recursionStack = new HashSet<>();
//
//        // Iterate over all Cins only, as all dependencies are interconnected
//        for (Long cinId : cinToDins.keySet()) {
//            if (!visited.contains(cinId)) {
//                if (detectCycleDFS(cinId, visited, recursionStack)) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//
//    private boolean detectCycleDFS(Long current, Set<Long> visited, Set<Long> stack) {
//        if (stack.contains(current)) return true; // Found a cycle
//        if (visited.contains(current)) return false; // Already visited, no cycle here
//
//        visited.add(current);
//        stack.add(current);
//
//        // Explore all dependencies (Cins to Dins and vice versa)
//        Set<Long> dinIds = cinToDins.getOrDefault(current, Collections.emptySet());
//        for (Long dinId : dinIds) {
//            Set<Long> dependentCins = dinToCins.getOrDefault(dinId, Collections.emptySet());
//            for (Long nextCin : dependentCins) {
//                if (detectCycleDFS(nextCin, visited, stack)) return true;
//            }
//        }
//
//        stack.remove(current);
//        return false;
//    }

    public boolean hasCycle() {
        Set<Long> visited = new HashSet<>();
        Set<Long> recursionStack = new HashSet<>();

        // Check Cins for cycles
        for (Long cinId : cinToDins.keySet()) {
            if (!visited.contains(cinId)) {
                if (detectCycleDFS(cinId, visited, recursionStack)) {
                    return true; // Cycle detected
                }
            }
        }

        // Check Dins for cycles
        for (Long dinId : dinToCins.keySet()) {
            if (!visited.contains(dinId)) {
                if (detectCycleDFS(dinId, visited, recursionStack)) {
                    return true; // Cycle detected
                }
            }
        }

        return false; // No cycle detected
    }

    private boolean detectCycleDFS(Long current, Set<Long> visited, Set<Long> stack) {
        if (stack.contains(current)) return true; // Found a cycle
        if (visited.contains(current)) return false; // Already visited, no cycle here

        stack.add(current); // Add to recursion stack

        // Explore Cins to Dins dependencies
        Set<Long> dinIds = cinToDins.getOrDefault(current, Collections.emptySet());
        for (Long dinId : dinIds) {
            if (detectCycleDFS(dinId, visited, stack)) {
                return true; // Cycle found in Cin -> Din direction
            }
        }

        // Explore Dins to Cins dependencies
        Set<Long> cinIds = dinToCins.getOrDefault(current, Collections.emptySet());
        for (Long cinId : cinIds) {
            if (detectCycleDFS(cinId, visited, stack)) {
                return true; // Cycle found in Din -> Cin direction
            }
        }

        stack.remove(current); // Backtrack
        visited.add(current); // Mark as fully processed

        return false; // No cycle detected from this node
    }



}

