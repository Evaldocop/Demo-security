package com.mballem.curso.security.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mballem.curso.security.domain.Usuario;
import com.mballem.curso.security.service.UsuarioService;

@Controller
@RequestMapping("u")
public class UsuarioController {

	@Autowired
	private UsuarioService  usuarioService ;
	
	// abrir pagina
	@GetMapping({"/novo/cadastro/usuario"})
	public String cadastroPorAdminParaMedicoPaciente(Usuario usuario) {
		return "usuario/cadastro";
	}	
	
	// abrir listagem de usuario
	@GetMapping({"/lista"})
	public String listaUsuario(Usuario usuario) {
		return "usuario/lista";
	}	
	
	// abrir listar usuario
	@GetMapping({"/datatables/server/usuarios"})
	public ResponseEntity<?> listaUsuarioDatatables(HttpServletRequest request) {
		return ResponseEntity.ok(usuarioService.buscarTodos(request));
	}	
	
	
}
