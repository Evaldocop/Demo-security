package com.mballem.curso.security.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mballem.curso.security.domain.Paciente;
import com.mballem.curso.security.domain.Usuario;
import com.mballem.curso.security.service.PacienteService;
import com.mballem.curso.security.service.UsuarioService;

@Controller
@RequestMapping("pacientes")
public class PacienteController {
	
	@Autowired
	private PacienteService pacienteService;
	
	@Autowired
	private UsuarioService usuarioService;

	// abrir pagina home
	@GetMapping({"/dados"})
	public String Cadastrar(Paciente paciente, ModelMap model, @AuthenticationPrincipal User user) {	
		paciente = pacienteService.buscarPorUsuarioPorEmail(user.getUsername());
		if(paciente.hasNotId()) {
			paciente.setUsuario(new Usuario(user.getUsername()));		
		}
		model.addAttribute("paciente", paciente);
		return "paciente/cadastro";
	}	
	
	// salvar o form de dados do paciente
	@PostMapping("/salvar")
	public String salvar(Paciente paciente, ModelMap model, @AuthenticationPrincipal User user) {	
	    Usuario usuario = usuarioService.buscarPorEmail(user.getUsername());
	    if(UsuarioService.isSenhaCorreta(paciente.getUsuario().getSenha(), usuario.getSenha())) {
	    	paciente.setUsuario(usuario);
	    	pacienteService.salvar(paciente);
	    	model.addAttribute("sucesso","Seu dados foram inseridos com sucesso.");
	    }else {
	    	model.addAttribute("falha","Sua senha não confere");
	    }
		return "paciente/cadastro";
	}	
	
	
	@PostMapping("/editar")
	public String editar(Paciente paciente, ModelMap model, @AuthenticationPrincipal User user) {	
	    Usuario usuario = usuarioService.buscarPorEmail(user.getUsername());
	    if(UsuarioService.isSenhaCorreta(paciente.getUsuario().getSenha(), usuario.getSenha())) {
	    	paciente.setUsuario(usuario);
	    	pacienteService.editar(paciente);
	    	model.addAttribute("sucesso","Seu dados foram editados com sucesso.");
	    }else {
	    	model.addAttribute("falha","Sua senha não confere");
	    }
		return "paciente/cadastro";
	}	
}
