package com.mballem.curso.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mballem.curso.security.domain.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente,Long> {
	
	@Query("select p from Paciente p where p.usuario.email like :email")
	public Optional<Paciente> findByUsuarioEmail(String email);

}
