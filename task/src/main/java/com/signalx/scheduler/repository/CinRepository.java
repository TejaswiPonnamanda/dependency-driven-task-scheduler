package com.signalx.scheduler.repository;


import com.signalx.scheduler.model.Cin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CinRepository extends JpaRepository<Cin, Long> {
}

