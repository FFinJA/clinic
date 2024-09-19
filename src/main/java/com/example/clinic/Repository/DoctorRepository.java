package com.example.clinic.Repository;

import com.example.clinic.model.Appointment;
import com.example.clinic.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.role = 'DOCTOR'")
    List<User> findAllDoctors();

    @Query("SELECT u FROM User u WHERE u.role = 'DOCTOR' and u.id = :id")
    Optional<User> findDoctorByUserId(@Param("id") Long id);

    User findDoctorByEmail(String email);
}
