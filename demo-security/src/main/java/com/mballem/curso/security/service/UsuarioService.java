package com.mballem.curso.security.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mballem.curso.security.domain.Perfil;
import com.mballem.curso.security.domain.Usuario;
import com.mballem.curso.security.repository.UsuarioReposirory;

@Service
public class UsuarioService implements UserDetailsService{
	
	@Autowired
	private UsuarioReposirory usuarioReposirory;
	
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

}