package com.signalx.scheduler;

import com.signalx.scheduler.model.TaskExecutionLog;
import com.signalx.scheduler.repository.TaskExecutionLogRepository;
import com.signalx.scheduler.service.DependencyService;
import com.signalx.scheduler.service.SchedulerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SchedulerServiceTest {

    @Mock
    private DependencyService dependencyService;

    @Mock
    private TaskExecutionLogRepository logRepository;

    @InjectMocks
    private SchedulerService schedulerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testScheduleTasks_successfulExecution() {
        // Mocking CIN -> DIN mapping
        Map<Long, Set<Long>> cinToDins = Map.of(
                1L, Set.of(101L),
                2L, Set.of(102L)
        );

        // Mocking DIN -> CIN mapping (No dependencies for DINs)
        Map<Long, Set<Long>> dinToCins = Map.of(
                101L, Set.of(),
                102L, Set.of()
        );

        // No cycle in this case
        when(dependencyService.hasCycle()).thenReturn(false);
        when(dependencyService.getCinToDins()).thenReturn(cinToDins);
        when(dependencyService.getDinToCins()).thenReturn(dinToCins);

        // Execute scheduler
        schedulerService.scheduleTasks();

        // Total logs: 2 CINs + 2 DINs
        verify(logRepository, times(4)).save(any(TaskExecutionLog.class));
    }

    @Test
    void testScheduleTasks_deadlockDueToCycle() {
        // Create a cycle: CIN 1 -> DIN 101 -> CIN 1
        Map<Long, Set<Long>> cinToDins = Map.of(
                1L, Set.of(101L)
        );

        Map<Long, Set<Long>> dinToCins = Map.of(
                101L, Set.of(1L)
        );

        when(dependencyService.hasCycle()).thenReturn(true);
        when(dependencyService.getCinToDins()).thenReturn(cinToDins);
        when(dependencyService.getDinToCins()).thenReturn(dinToCins);

        // Verify exception due to cycle
        assertThrows(IllegalStateException.class, () -> schedulerService.scheduleTasks());

        // No task should be executed, so logRepository.save should not be called
        verify(logRepository, never()).save(any(TaskExecutionLog.class));
    }
}



