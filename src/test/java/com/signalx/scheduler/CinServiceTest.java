package com.signalx.scheduler;

import com.signalx.scheduler.model.Cin;
import com.signalx.scheduler.model.Din;
import com.signalx.scheduler.model.TaskStatus;
import com.signalx.scheduler.repository.CinRepository;
import com.signalx.scheduler.repository.DinRepository;
import com.signalx.scheduler.service.CinService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CinServiceTest {

    private CinRepository cinRepository;
    private DinRepository dinRepository;
    private CinService cinService;

    @BeforeEach
    void setUp() {
        cinRepository = mock(CinRepository.class);
        dinRepository = mock(DinRepository.class);
        cinService = new CinService();

        inject(cinService, "cinRepository", cinRepository);
        inject(cinService, "dinRepository", dinRepository);
    }

    @Test
    void testCreateCinWithDependencies() {
        Cin cin = new Cin("TestCin", TaskStatus.PENDING, LocalDateTime.now(), 0, null);

        List<Long> dinIds = Arrays.asList(101L, 102L);

        Din din1 = new Din();
        din1.setId(101L);

        Din din2 = new Din();
        din2.setId(102L);

        List<Din> dinList = Arrays.asList(din1, din2);

        when(cinRepository.save(any(Cin.class))).thenAnswer(invocation -> {
            Cin c = invocation.getArgument(0);
            c.setId(1L); // Simulate DB assigned ID
            return c;
        });

        when(dinRepository.findAllById(dinIds)).thenReturn(dinList);
        when(dinRepository.save(any(Din.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cin result = cinService.createCin(cin, dinIds);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(2, result.getDependencies().size());

        for (Din d : dinList) {
            assertNotNull(d.getDependentCins());
            assertTrue(d.getDependentCins().contains(result));
        }

        verify(cinRepository, times(2)).save(any(Cin.class));
        verify(dinRepository, times(2)).save(any(Din.class));
    }

    @Test
    void testGetAllCins() {
        when(cinRepository.findAll()).thenReturn(List.of(new Cin(), new Cin()));
        List<Cin> cins = cinService.getAllCins();
        assertEquals(2, cins.size());
        verify(cinRepository).findAll();
    }

    @Test
    void testDeleteCin() {
        Long id = 5L;
        cinService.deleteCin(id);
        verify(cinRepository).deleteById(id);
    }

    // Reflection injection helper (for testing private fields)
    private void inject(Object target, String fieldName, Object toInject) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, toInject);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
