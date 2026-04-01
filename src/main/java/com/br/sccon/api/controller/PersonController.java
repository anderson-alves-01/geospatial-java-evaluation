package com.br.sccon.api.controller;

import com.br.sccon.api.controller.dto.PersonRequestDTO;
import com.br.sccon.api.controller.mapper.PersonMapper;
import com.br.sccon.api.repository.entity.Person;
import com.br.sccon.api.service.PersonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    private PersonService service;

    @Autowired
    private PersonMapper mapper;

    @GetMapping
    public ResponseEntity<List<PersonRequestDTO>> listAll() {
        List<PersonRequestDTO> dtos = service.findAll().stream()
                .map(mapper::toDTO)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonRequestDTO> getById(@PathVariable Long id) {
        Person person = service.findById(id);
        return ResponseEntity.ok(mapper.toDTO(person));
    }

    @PostMapping
    public ResponseEntity<PersonRequestDTO> create(@Valid @RequestBody PersonRequestDTO dto) {
        Person personEntity = mapper.toEntity(dto);
        Person savedPerson = service.save(personEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(savedPerson));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonRequestDTO> update(@PathVariable Long id, @Valid @RequestBody PersonRequestDTO dto) {
        Person personEntity = mapper.toEntity(dto);
        Person updatedPerson = service.update(id, personEntity);
        return ResponseEntity.ok(mapper.toDTO(updatedPerson));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PersonRequestDTO> partialUpdate(
            @PathVariable Long id,
            @RequestBody PersonRequestDTO dto) {

        Person partialEntity = mapper.toEntity(dto);
        Person updatedPerson = service.partialUpdate(id, partialEntity);

        return ResponseEntity.ok(mapper.toDTO(updatedPerson));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/age")
    public ResponseEntity<Long> getAge(@PathVariable Long id, @RequestParam String output) {
        return ResponseEntity.ok(service.calculateAge(id, output));
    }

    @GetMapping("/{id}/salary")
    public ResponseEntity<BigDecimal> getSalary(@PathVariable Long id, @RequestParam String output) {
        return ResponseEntity.ok(service.calculateSalary(id, output));
    }
}