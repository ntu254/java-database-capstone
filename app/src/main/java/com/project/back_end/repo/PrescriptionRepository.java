package com.project.back_end.repo;

import com.project.back_end.models.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
}