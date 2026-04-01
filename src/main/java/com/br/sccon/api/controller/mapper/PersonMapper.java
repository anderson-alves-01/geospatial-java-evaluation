package com.br.sccon.api.controller.mapper;

import com.br.sccon.api.controller.dto.PersonRequestDTO;
import com.br.sccon.api.repository.entity.Person;
import org.springframework.stereotype.Component;

@Component
public class PersonMapper {

    // Converte o que vem da Web (DTO) para o que o Banco entende (Entity)
    public Person toEntity(PersonRequestDTO dto) {
        return new Person(
                dto.id(),
                dto.nome(),
                dto.dataDeNascimento(),
                dto.dataDeAdmissao()
        );
    }

    // Converte o que vem do Banco (Entity) para o que a Web deve ver (DTO)
    public PersonRequestDTO toDTO(Person person) {
        return new PersonRequestDTO(
                person.getId(),
                person.getNome(),
                person.getDataDeNascimento(),
                person.getDataDeAdmissao()
        );
    }
}