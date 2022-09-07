package com.mballem.curso.security.web.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mballem.curso.security.domain.Medico;
import com.mballem.curso.security.domain.Perfil;
import com.mballem.curso.security.domain.PerfilTipo;
import com.mballem.curso.security.domain.Usuario;
import com.mballem.curso.security.service.MedicoService;
import com.mballem.curso.security.service.UsuarioService;

@Controller
@RequestMapping("u")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;
	
	
	@Autowired
	private MedicoService medicoService;


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
	};
	
	// pre Editar Credenciais
	@GetMapping("/editar/dados/usuario/{id}/perfis/{perfis}")
	public ModelAndView preEditarCadstroDadosPessoais(@PathVariable("id") Long usuarioId, 
			                                         @PathVariable("perfis") Long[] perfisId) {
		Usuario us= usuarioService.buscarPorIdEPerfis(usuarioId,perfisId);
		if(us.getPerfis().contains(new Perfil(PerfilTipo.ADMIN.getCod())) &&
			!us.getPerfis().contains(new Perfil(PerfilTipo.MEDICO.getCod())	)) {
			
			return new ModelAndView("usuario/cadastro","usuario",us);
		} else if(us.getPerfis().contains(new Perfil(PerfilTipo.MEDICO.getCod())	)) {
			
			
			Medico medico= medicoService.buscarPorUsuarioId(usuarioId);
	
			return medico.hasNotId() ? new ModelAndView("medico/cadastro", "medico", new Medico(new Usuario(usuarioId))):
				                       new ModelAndView("medico/cadastro", "medico", medico);
			
			
			
		}else if(us.getPerfis().contains(new Perfil(PerfilTipo.PACIENTE.getCod())	)) {
			ModelAndView model = new ModelAndView("error");
			model.addObject("status", 403);
			model.addObject("error", "Área restrita!");
			model.addObject("message", "Dados do paciente são restritos.");
			return model;
			
		}
			                                        	 
		return new ModelAndView("redirect:/u/lista");
	};

	
	
	// abrir edicao senha
		@GetMapping({ "/editar/senha" })
		public String abrirEditarSenha() {
			return "usuario/editar-senha";
		}
		
		
		@PostMapping("/confirmar/senha")
		public String editarSenha(@RequestParam("senha1") String s1,
				                  @RequestParam("senha2") String s2, 
				                  @RequestParam("senha3") String s3, 
				                  @AuthenticationPrincipal User user,
				                  RedirectAttributes attr) {
			
			if(!s1.equals(s2)) {
				attr.addFlashAttribute("falha","Senha não conferem, tente novamente");
				return "redirect:/u/editar/senha";
			}
			
			Usuario u=usuarioService.buscarPorEmail(user.getUsername());
			if(!UsuarioService.isSenhaCorreta(s3,u.getSenha())) {
				attr.addFlashAttribute("falha","Senha atual não conferem, tente novamente");
				return "redirect:/u/editar/senha";
			}
			usuarioService.alterarSenha(u,s1);
			attr.addFlashAttribute("sucesso","Senha atualizada com sucesso");
			return "redirect:/u/editar/senha";
			
		}



}
