package com.mballem.curso.security.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mballem.curso.security.domain.Especialidade;


public interface EspecialidadeReposirory extends JpaRepository<Especialidade, Long> {

	@Query("select e from Especialidade e where e.titulo like  :search%")
	Page<Especialidade> findAllByTitulo(String search, Pageable pageable);

	@Query("select e.titulo from Especialidade e where e.titulo like :termo% ")
	List<String> findEspecialidadeByTermo(String termo);

}
