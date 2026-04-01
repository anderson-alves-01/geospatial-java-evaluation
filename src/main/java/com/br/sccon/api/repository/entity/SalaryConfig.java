package com.br.sccon.api.repository.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class SalaryConfig {
    @Id
    private Long id; // Usaremos ID 1 para a configuração ativa
    private BigDecimal initialSalary;
    private BigDecimal minSalaryRef;
    private BigDecimal increaseRate;
    private BigDecimal annualBonus;
}