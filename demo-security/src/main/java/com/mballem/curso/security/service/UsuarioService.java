package com.mballem.curso.security.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mballem.curso.security.datatables.Datatables;
import com.mballem.curso.security.datatables.DatatablesColunas;
import com.mballem.curso.security.domain.Perfil;
import com.mballem.curso.security.domain.Usuario;
import com.mballem.curso.security.repository.UsuarioReposirory;

@Service
public class UsuarioService implements UserDetailsService{
	
	@Autowired
	private UsuarioReposirory usuarioReposirory;
	
	@Autowired
	private Datatables dataTables;
	
	@Transactional(readOnly = true)
	public Usuario buscarPorEmail(String email) {
		return usuarioReposirory.findByEmail(email);
	}

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    Usuario usuario= buscarPorEmail(username);
		return new User(usuario.getEmail(),
				        usuario.getSenha(),
				        AuthorityUtils.createAuthorityList(getAtuthorities(usuario.getPerfis()))
				        );
	}
	
	
	
	private String[] getAtuthorities(List<Perfil> perfis) {
		String[] authorities = new String[perfis.size()];
		for (int i =0; i<perfis.size();i++) {
			authorities[i] = perfis.get(i).getDesc();
			
		}
		return authorities;
	}
	
	@Transactional(readOnly = true)
	public Map<String,Object> buscarTodos(HttpServletRequest request) {
	    dataTables.setRequest(request);
	    dataTables.setColunas(DatatablesColunas.USUARIOS);
	    Page<Usuario> page =  dataTables.getSearch().isEmpty() ? usuarioReposirory.findAll(dataTables.getPageable())
	    : usuarioReposirory.fyndByEmailOrPerfil(dataTables.getSearch(),dataTables.getPageable());
		return dataTables.getResponse(page);
	}

	@Transactional(readOnly = false)
	public void salvarUsuario(Usuario usuario) {
		String encrypt= new BCryptPasswordEncoder().encode(usuario.getSenha());
		usuario.setSenha(encrypt);
		
		usuarioReposirory.save(usuario);
		
	}
@Transactional(readOnly = true)
public Usuario buscarPorId(Long id) {
		// TODO Auto-generated method stub
		return usuarioReposirory.findById(id).get();
	}
@Transactional(readOnly = true)
public Usuario buscarPorIdEPerfis(Long usuarioId, Long[] perfisId) {
	
	return usuarioReposirory.fyndByIdAndPerfis(usuarioId,perfisId).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado!"));
}

}
