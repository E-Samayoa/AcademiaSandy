package com.academiasi.spring.app.models.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.academiasi.spring.app.models.entities.Alumno;

public interface IAlumnoService {

	public List<Alumno> findAll();
	
	// Pageable de Spring Framework data domain obligatorio
	
		public Page<Alumno> findAll(Pageable pageable);

	public void save(Alumno alumno);

	public Alumno findOne(Long id);
	
	public void delete(Long id);

}
