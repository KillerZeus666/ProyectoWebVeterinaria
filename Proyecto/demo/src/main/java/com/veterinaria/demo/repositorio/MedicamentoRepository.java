package com.veterinaria.demo.repositorio;

import com.veterinaria.demo.entidad.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicamentoRepository extends JpaRepository<Medicamento, Integer> {

}