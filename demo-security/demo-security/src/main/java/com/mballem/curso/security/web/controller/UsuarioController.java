package com.mballem.curso.security.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mballem.curso.security.domain.Perfil;
import com.mballem.curso.security.domain.Usuario;
import com.mballem.curso.security.service.UsuarioService;

@Controller
@RequestMapping("u")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	// abrir pagina
	@GetMapping({ "/novo/cadastro/usuario" })
	public String cadastroPorAdminParaMedicoPaciente(Usuario usuario) {
		return "usuario/cadastro";
	}

	// abrir listagem de usuario
	@GetMapping({ "/lista" })
	public String listaUsuario(Usuario usuario) {
		return "usuario/lista";
	}

	// abrir listar usuario
	@GetMapping({ "/datatables/server/usuarios" })
	public ResponseEntity<?> listaUsuarioDatatables(HttpServletRequest request) {
		return ResponseEntity.ok(usuarioService.buscarTodos(request));
	}

	// salvar usuario

	@PostMapping("/cadastro/salvar")
	public String salvarUsuario(Usuario usuario, RedirectAttributes attr) {
		List<Perfil> perfis = usuario.getPerfis();
		if (perfis.size() > 2 || perfis.containsAll(Arrays.asList(new Perfil(1L), new Perfil(3L)))
				|| perfis.containsAll(Arrays.asList(new Perfil(2L), new Perfil(3L)))) {
			attr.addFlashAttribute("falha", "Não pode esta combinação de perfis");
			attr.addFlashAttribute("usuario", usuario);

		} else {
			try {
				usuarioService.salvarUsuario(usuario);
				attr.addFlashAttribute("sucesso", "Cadastrado com sucesso.");
			} catch (DataIntegrityViolationException e) {
				attr.addFlashAttribute("falha", "Email já existente.");
			}
		}

		return "redirect:/u/novo/cadastro/usuario";
	}

	// pre Editar Credenciais
	@GetMapping({ "editar/credenciais/usuario/{id}" })
	public ModelAndView preEditarCredenciais(@PathVariable("id") Long id) {
		return new ModelAndView("usuario/cadastro", "usuario", usuarioService.buscarPorId(id));
	}

}
