package com.br.sccon.api.service;

import com.br.sccon.api.exception.BusinessException;
import com.br.sccon.api.exception.ResourceNotFoundException;
import com.br.sccon.api.repository.entity.Person;
import com.br.sccon.api.repository.entity.SalaryConfig;
import com.br.sccon.api.repository.PersonRepository;
import com.br.sccon.api.repository.SalaryConfigRepository;
import com.br.sccon.api.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private SalaryConfigRepository configRepository;

    public List<Person> findAll() {
        return personRepository.findAllByOrderByNomeAsc();
    }

    public Person findById(Long id) {
        return personRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Pessoa não encontrada com ID: " + id));
    }

    @Transactional
    public Person save(Person person) {
        if (person.getId() != null && personRepository.existsById(person.getId())) {
            throw BusinessException.of("Conflito: ID " + person.getId() + " já existe.");
        }
        return personRepository.save(person);
    }

    @Transactional
    public Person partialUpdate(Long id, Person partialPerson) {
        Person person = findById(id);
        if (partialPerson.getNome() != null) {
            person.setNome(partialPerson.getNome());
        }
        if (partialPerson.getDataDeNascimento() != null) {
            person.setDataDeNascimento(partialPerson.getDataDeNascimento());
        }
        if (partialPerson.getDataDeAdmissao() != null) {
            person.setDataDeAdmissao(partialPerson.getDataDeAdmissao());
        }

        return personRepository.save(person);
    }

    @Transactional
    public Person update(Long id, Person personDetails) {
        Person person = findById(id);

        person.setNome(personDetails.getNome());
        person.setDataDeNascimento(personDetails.getDataDeNascimento());
        person.setDataDeAdmissao(personDetails.getDataDeAdmissao());

        return personRepository.save(person);
    }

    @Transactional
    public void delete(Long id) {
        Person person = findById(id);
        personRepository.delete(person);
    }

    public long calculateAge(Long id, String output) {
        Person person = findById(id);
        // Pattern Matching for switch
        return switch (output.toLowerCase()) {
            case "days" -> DateUtils.calculateDiff(person.getDataDeNascimento(), LocalDate.now(), "days");
            case "months" -> DateUtils.calculateDiff(person.getDataDeNascimento(), LocalDate.now(), "months");
            case "years" -> DateUtils.calculateDiff(person.getDataDeNascimento(), LocalDate.now(), "years");
            default -> throw new IllegalArgumentException("Unidade de tempo inválida");
        };
    }

    public BigDecimal calculateSalary(Long id, String output) {
        Person person = findById(id);
        SalaryConfig config = configRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Configuração salarial não encontrada"));

        int years = DateUtils.getFullYearsBetween(person.getDataDeAdmissao(), LocalDate.now());

        BigDecimal finalSalary = Stream.iterate(config.getInitialSalary(),
                        s -> s.multiply(config.getIncreaseRate()).add(config.getAnnualBonus()))
                .limit(years + 1)
                .reduce((first, second) -> second)
                .orElse(config.getInitialSalary());

        return switch (output.toLowerCase()) {
            case "full" -> finalSalary.setScale(2, RoundingMode.CEILING);
            case "min"  -> finalSalary.divide(config.getMinSalaryRef(), 2, RoundingMode.CEILING);
            default     -> throw new IllegalArgumentException("Formato inválido");
        };
    }
}