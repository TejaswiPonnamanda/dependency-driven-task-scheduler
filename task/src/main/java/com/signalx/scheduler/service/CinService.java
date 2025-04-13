package com.signalx.scheduler.service;

import com.signalx.scheduler.model.Cin;
import com.signalx.scheduler.model.Din;
import com.signalx.scheduler.repository.CinRepository;
import com.signalx.scheduler.repository.DinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class CinService {

    @Autowired
    private CinRepository cinRepository;

    @Autowired
    private DinRepository dinRepository;

    public Cin createCin(Cin cin, List<Long> dinIds) {
        // Save CIN first
        Cin savedCin = cinRepository.save(cin);

        // Fetch Dins using IDs
        List<Din> dinList = dinRepository.findAllById(dinIds);

        // Set Din dependencies in CIN
        savedCin.setDependencies(new HashSet<>(dinList));

        // Maintain reverse relationship: update each Din to include this CIN
        for (Din din : dinList) {
            if (din.getDependentCins() == null) {
                din.setDependentCins(new HashSet<>());
            }
            din.getDependentCins().add(savedCin);
            dinRepository.save(din); // Save updated Din
        }

        // Save updated CIN with dependency links
        return cinRepository.save(savedCin);
    }

    public List<Cin> getAllCins() {
        return cinRepository.findAll();
    }

    public void deleteCin(Long cinId) {
        cinRepository.deleteById(cinId);
    }
}

