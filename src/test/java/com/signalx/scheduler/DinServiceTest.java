package com.signalx.scheduler;

import com.signalx.scheduler.dto.DinRequest;
import com.signalx.scheduler.model.Cin;
import com.signalx.scheduler.model.Din;
import com.signalx.scheduler.model.TaskStatus;
import com.signalx.scheduler.repository.CinRepository;
import com.signalx.scheduler.repository.DinRepository;
import com.signalx.scheduler.service.DependencyService;
import com.signalx.scheduler.service.DinService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DinServiceTest {

    @InjectMocks
    private DinService dinService;

    @Mock
    private DinRepository dinRepository;

    @Mock
    private CinRepository cinRepository;

    @Mock
    private DependencyService dependencyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateDin() {
        // Arrange
        DinRequest request = new DinRequest();
        request.setName("DIN-A");
        request.setCinIds(Arrays.asList(1L, 2L));

        Cin cin1 = new Cin();
        cin1.setId(1L);

        Cin cin2 = new Cin();
        cin2.setId(2L);

        when(cinRepository.findAllById(request.getCinIds())).thenReturn(Arrays.asList(cin1, cin2));

        Din savedDin = new Din();
        savedDin.setId(101L);
        savedDin.setName("DIN-A");

        when(dinRepository.save(any(Din.class))).thenReturn(savedDin);

        // Act
        Din result = dinService.createDin(request);

        // Assert
        assertNotNull(result);
        assertEquals("DIN-A", result.getName());
        verify(dinRepository, times(1)).save(any(Din.class));
    }

    @Test
    void testGetAllDins() {
        // Arrange
        Din din1 = new Din();
        din1.setId(1L);
        Din din2 = new Din();
        din2.setId(2L);
        when(dinRepository.findAll()).thenReturn(Arrays.asList(din1, din2));

        // Act
        List<Din> result = dinService.getAllDins();

        // Assert
        assertEquals(2, result.size());
        verify(dinRepository).findAll();
    }

    @Test
    void testDeleteDin() {
        Long dinId = 42L;

        // Act
        dinService.deleteDin(dinId);

        // Assert
        verify(dependencyService).removeDin(dinId);
        verify(dinRepository).deleteById(dinId);
    }
}
