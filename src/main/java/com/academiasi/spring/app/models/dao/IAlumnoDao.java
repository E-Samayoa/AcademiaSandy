package com.academiasi.spring.app.models.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.academiasi.spring.app.models.entities.Alumno;

public interface IAlumnoDao extends PagingAndSortingRepository<Alumno, Long>{

}
