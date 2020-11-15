package com.academiasi.spring.app.models.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.academiasi.spring.app.models.entities.Horario;

public interface IHorarioService {
	
	public List<Horario> findAll();
	
	// Pageable de Spring Framework data domain obligatorio
	
	public Page<Horario> findAll(Pageable pageable);
	
	public void save(Horario horario);
	
	public Horario findOne(Long id);
	
	public void delete(Long id);

}
