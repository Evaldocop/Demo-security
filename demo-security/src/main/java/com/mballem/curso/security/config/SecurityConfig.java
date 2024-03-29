package com.mballem.curso.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.mballem.curso.security.domain.Paciente;
import com.mballem.curso.security.domain.PerfilTipo;
import com.mballem.curso.security.service.UsuarioService;


@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private static final String ADMIN = PerfilTipo.ADMIN.getDesc();
	private static final String MEDICO = PerfilTipo.MEDICO.getDesc();
	private static final String PACIENTE = PerfilTipo.PACIENTE.getDesc();
	
	@Autowired
	private UsuarioService usuarioService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.authorizeRequests().
		     antMatchers("/webjars/**","/css/**", "/image/**", "/js/**").permitAll().
		     
		     antMatchers("/","home").permitAll()
		    
		     .antMatchers("/u/editar/senha","/u/confirmar/senha").hasAnyAuthority(PACIENTE,MEDICO,ADMIN)
		     ///acesso privado para admin
		     .antMatchers("/u/**").hasAuthority(ADMIN)
		     
		     .antMatchers("/especialidades//datatables/server/medico/*").hasAnyAuthority(ADMIN,MEDICO)
		     .antMatchers("/especialidades/titulo").hasAnyAuthority(ADMIN,MEDICO)
		     ///acesso privado para admin
		     .antMatchers("/especialidades/**").hasAuthority(ADMIN)
		     
		     ///acesso privado para medico
		     
		     .antMatchers("/medicos/dados","/medicos/salvar","/medicos/editar").hasAnyAuthority(ADMIN,MEDICO)
		     .antMatchers("/medicos/**").hasAuthority(MEDICO)
		     
		     
		     ///acesso privado para PACIENTE
		     .antMatchers("/PACIENTES/**").hasAuthority(PACIENTE)
		     
		     .anyRequest().authenticated()
		     .and()
		     	.formLogin()
		     	.loginPage("/login")
		     	.defaultSuccessUrl("/",true)
		     	.failureUrl("/login-error")
		     	.permitAll()
		     .and()
		     	.logout()
		     	.logoutSuccessUrl("/")
		     .and()
		     	.exceptionHandling()
		     	.accessDeniedPage("/acesso-negado");
		
		
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(usuarioService).passwordEncoder(new BCryptPasswordEncoder());
	}
	
	

	
			
			
	
	

}
