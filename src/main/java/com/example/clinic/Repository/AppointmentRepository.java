package com.example.clinic.Repository;

import com.example.clinic.model.Appointment;
import com.example.clinic.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    @Query("SELECT a FROM Appointment a WHERE a.doctor.id= :id")
    List<Appointment> findAppointmentsByDoctor_Id(@Param("id") Long id);

    @Query("SELECT a FROM Appointment a WHERE a.patient.id= :id")
    List<Appointment> findByPatientId(@Param("id") Long id);

    long countByPatient_Id(Long id);

    long countByPatient_Email(String email);
}
