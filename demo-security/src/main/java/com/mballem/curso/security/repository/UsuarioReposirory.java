package com.mballem.curso.security.repository;


import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mballem.curso.security.domain.Usuario;


public interface UsuarioReposirory extends JpaRepository<Usuario, Long> {

	
		@Query("select u from Usuario u where u.email like :email")
		Usuario findByEmail(@Param("email") String email);
		
		@Query("select distinct u from Usuario u  "
				+ " join u.perfis p "
				+ " where u.email like :search%  or p.desc like :search%")
		Page<Usuario> fyndByEmailOrPerfil(String search, Pageable pageable);

		@Query("select distinct u from Usuario u  "
				+ " join u.perfis p "
				+ " where u.id = :usuarioId and p.id in :perfisId")
		Optional<Usuario> fyndByIdAndPerfis(Long usuarioId, Long[] perfisId);
}
