package com.br.sccon.api.service;

import com.br.sccon.api.exception.BusinessException;
import com.br.sccon.api.exception.ResourceNotFoundException;
import com.br.sccon.api.repository.entity.Person;
import com.br.sccon.api.repository.entity.SalaryConfig;
import com.br.sccon.api.repository.PersonRepository;
import com.br.sccon.api.repository.SalaryConfigRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private SalaryConfigRepository configRepository;

    @InjectMocks
    private PersonService personService;

    private Person person;
    private SalaryConfig config;

    @BeforeEach
    void setUp() {
        person = new Person(1L, "Teste", LocalDate.now().minusYears(20), LocalDate.now().minusYears(2));
        config = new SalaryConfig(1L, new BigDecimal("1000.00"), new BigDecimal("1300.00"), new BigDecimal("1.10"), new BigDecimal("100.00"));
    }

    @Test
    @DisplayName("Deve calcular salário corretamente após 2 anos de empresa")
    void shouldCalculateSalaryCorrectly() {
        // Arrange
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        when(configRepository.findById(1L)).thenReturn(Optional.of(config));

        // Act
        // Ano 0: 1000
        // Ano 1: (1000 * 1.10) + 100 = 1200
        // Ano 2: (1200 * 1.10) + 100 = 1420
        BigDecimal result = personService.calculateSalary(1L, "full");

        // Assert
        assertEquals(new BigDecimal("1420.00"), result);
    }

    @Test
    @DisplayName("Deve lançar BusinessException ao tentar salvar ID duplicado")
    void shouldThrowExceptionWhenIdExists() {
        when(personRepository.existsById(1L)).thenReturn(true);

        assertThrows(BusinessException.class, () -> personService.save(person));
        verify(personRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando ID não existir")
    void shouldThrowNotFoundException() {
        when(personRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> personService.findById(99L));
    }

    @Test
    @DisplayName("Deve calcular idade em anos corretamente")
    void shouldCalculateAgeInYears() {
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));

        long age = personService.calculateAge(1L, "years");

        assertEquals(20, age);
    }

    @Test
    @DisplayName("Deve atualizar apenas o nome da pessoa mantendo as datas originais (PATCH)")
    void shouldUpdateOnlyNameWhenOtherFieldsAreNull() {
        // Arrange: Dados originais no banco
        Long id = 1L;
        LocalDate dataNasc = LocalDate.of(1995, 5, 10);
        LocalDate dataAdm = LocalDate.of(2022, 1, 1);
        Person personOriginal = new Person(id, "Nome Antigo", dataNasc, dataAdm);

        Person partialUpdate = new Person();
        partialUpdate.setNome("Nome Novo");

        when(personRepository.findById(id)).thenReturn(Optional.of(personOriginal));
        when(personRepository.save(any(Person.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Person result = personService.partialUpdate(id, partialUpdate);

        // Assert
        assertNotNull(result);
        assertEquals("Nome Novo", result.getNome());
        assertEquals(dataNasc, result.getDataDeNascimento());
        assertEquals(dataAdm, result.getDataDeAdmissao());

        verify(personRepository, times(1)).save(personOriginal);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao tentar dar PATCH em ID inexistente")
    void shouldThrowExceptionOnPatchWhenIdNotFound() {
        // Arrange
        Long idInexistente = 99L;
        Person partialUpdate = new Person();
        partialUpdate.setNome("Qualquer Nome");

        when(personRepository.findById(idInexistente)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () ->
                personService.partialUpdate(idInexistente, partialUpdate)
        );

        verify(personRepository, never()).save(any());
    }
}