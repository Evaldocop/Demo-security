package com.mballem.curso.security.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mballem.curso.security.domain.Especialidade;
import com.mballem.curso.security.service.EspecialidadeService;

@Controller
@RequestMapping("especialidades")
public class EspecialidadeController {

	
	@Autowired
	private EspecialidadeService service;
	
	
	// abrir pagina
	@GetMapping({"/", ""})
	public String cadastroEspecialidade(Especialidade especialidade) {
		return "especialidade/especialidade";
	}	
	
	@PostMapping("/salvar")
	public String salvar(Especialidade especialidade, RedirectAttributes attr) {
		service.salvar(especialidade);
		attr.addFlashAttribute("sucesso", "Operação realizadacom sucesso.");
		return "redirect:/especialidades";
	}
	
	
	// abrir pagina
	@GetMapping({"/datatables/server"})
	public ResponseEntity<?> getEspecialidades(HttpServletRequest request) {
		
		return  ResponseEntity.ok(service.buscarEspecialidades(request));
	}
		
	
}
