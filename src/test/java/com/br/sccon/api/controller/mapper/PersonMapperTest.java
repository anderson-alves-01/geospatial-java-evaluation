package com.br.sccon.api.controller.mapper;

import com.br.sccon.api.controller.dto.PersonRequestDTO;
import com.br.sccon.api.repository.entity.Person;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class PersonMapperTest {

    private final PersonMapper mapper = new PersonMapper();

    @Test
    void shouldMapDtoToEntity() {
        PersonRequestDTO dto = new PersonRequestDTO(1L, "João", LocalDate.now(), LocalDate.now());

        Person entity = mapper.toEntity(dto);

        assertEquals(dto.id(), entity.getId());
        assertEquals(dto.nome(), entity.getNome());
    }

    @Test
    void shouldMapEntityToDto() {
        Person entity = new Person(1L, "Maria", LocalDate.now(), LocalDate.now());

        PersonRequestDTO dto = mapper.toDTO(entity);

        assertEquals(entity.getId(), dto.id());
        assertEquals(entity.getNome(), dto.nome());
    }
}