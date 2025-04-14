package com.signalx.scheduler.service;


import com.signalx.scheduler.dto.DinRequest;
import com.signalx.scheduler.model.Cin;
import com.signalx.scheduler.model.Din;
import com.signalx.scheduler.model.TaskStatus;
import com.signalx.scheduler.repository.CinRepository;
import com.signalx.scheduler.repository.DinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DinService {

    @Autowired
    private  DinRepository dinRepository;

    @Autowired
    private CinRepository cinRepository;

    @Autowired
    private  com.signalx.scheduler.service.DependencyService dependencyService;

    public Din createDin(DinRequest dto) {
        Din din = new Din();
        din.setName(dto.getName());
        din.setStatus(TaskStatus.PENDING);
        din.setRoundRobinIndex(0);

        // Get all CINs by ID
        List<Cin> cins = cinRepository.findAllById(dto.getCinIds());

        // Set reverse mapping only in one direction
        din.setDependentCins(new HashSet<>(cins));

        // ✅ DO NOT also do cin.getDependencies().add(din);
        // If you set mappedBy correctly in @ManyToMany, JPA will manage it

        return dinRepository.save(din);
    }



    public List<Din> getAllDins() {
        return dinRepository.findAll();
    }

    public void deleteDin(Long dinId) {
        dependencyService.removeDin(dinId);
        dinRepository.deleteById(dinId);
    }
}
