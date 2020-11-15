package com.academiasi.spring.app.models.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.academiasi.spring.app.models.entities.Horario;

public interface IHorarioDao extends PagingAndSortingRepository<Horario, Long>{

}
