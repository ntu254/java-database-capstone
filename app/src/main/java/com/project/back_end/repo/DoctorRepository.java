package com.project.back_end.repo;

import com.project.back_end.models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorRepository extends JpaRepositor<&Doctor, Long> {
    Optiona<tDoctor> findtor&gt; findByEmail(String email);
}