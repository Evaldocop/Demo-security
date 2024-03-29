package com.mballem.curso.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mballem.curso.security.domain.Paciente;
import com.mballem.curso.security.repository.PacienteRepository;

@Service
public class PacienteService {
	@Autowired
	private PacienteRepository pacienteRepository;
    
	@Transactional(readOnly = true)
	public Paciente buscarPorUsuarioPorEmail(String email) {
		
		//orElse -- nunca retorna null, ou retorna o que vem do bd ou retorna uma instancia do construtor
		return pacienteRepository.findByUsuarioEmail(email).orElse(new Paciente());
	}

	@Transactional(readOnly = false)
	public void salvar(Paciente paciente) {
	   pacienteRepository.save(paciente);
		
	}

	@Transactional(readOnly = false)
	public void editar(Paciente paciente) {
		//como o bean eh gerenciado o JPA executa o update de maneira implicita 
		Paciente p2= pacienteRepository.findById(paciente.getId()).get();
		p2.setNome(paciente.getNome());
		p2.setDtNascimento(paciente.getDtNascimento());
		
		
	}
}
