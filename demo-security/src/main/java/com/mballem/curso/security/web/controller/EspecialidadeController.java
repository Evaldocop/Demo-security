package com.mballem.curso.security.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	
	
	// editard
	@GetMapping("/editar/{id}")
	public String preEditar(@PathVariable Long id,ModelMap map) {
		map.addAttribute("especialidade",service.buscarPorId(id));
		return "especialidade/especialidade";
	}	
	
	
	// abrir pagina
	@GetMapping("/excluir/{id}")
	public String preeditar(@PathVariable Long id,RedirectAttributes  attr) {
		service.remover(id);
		attr.addFlashAttribute("secesso","Operação realizada com sucesso");
		return "redirect:/especialidades";
	}	
}
