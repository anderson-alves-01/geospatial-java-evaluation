package com.br.sccon.api.repository;

import com.br.sccon.api.repository.entity.SalaryConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalaryConfigRepository extends JpaRepository<SalaryConfig, Long> {
}