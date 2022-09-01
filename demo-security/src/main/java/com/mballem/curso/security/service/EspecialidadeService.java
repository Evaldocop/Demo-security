package com.mballem.curso.security.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mballem.curso.security.datatables.Datatables;
import com.mballem.curso.security.datatables.DatatablesColunas;
import com.mballem.curso.security.domain.Especialidade;
import com.mballem.curso.security.repository.EspecialidadeReposirory;

@Service
public class EspecialidadeService {

	
	@Autowired
	private EspecialidadeReposirory especialidadeRespository;
	
	@Autowired
	private Datatables  dataTables;
	
	@Transactional(readOnly = false)
	public void salvar(Especialidade especialidade) {
		especialidadeRespository.save(especialidade);
	}

	@Transactional(readOnly = true)
	public Map<String, Object> buscarEspecialidades(HttpServletRequest request) {
		dataTables.setRequest(request);
		dataTables.setColunas(DatatablesColunas.ESPECIALIDADES);
		Page<?> page =dataTables.getSearch().isEmpty() 
				? especialidadeRespository.findAll(dataTables.getPageable())
			    : especialidadeRespository.findAllByTitulo(dataTables.getSearch(),dataTables.getPageable());
		return dataTables.getResponse(page);
	}

	@Transactional(readOnly = true)
	public Especialidade buscarPorId(Long id) {
		return especialidadeRespository.findById(id).get();
		
	}
	@Transactional(readOnly = false)
	public void remover(Long id) {
	   especialidadeRespository.deleteById(id);
		
	}

	@Transactional(readOnly = true)
	public List<String> buscarEspecialidadesByTermo(String termo) {
		// TODO Auto-generated method stub
		return especialidadeRespository.findEspecialidadeByTermo(termo);
	}
	
	
}
