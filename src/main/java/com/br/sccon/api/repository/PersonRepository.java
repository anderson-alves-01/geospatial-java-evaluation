package com.br.sccon.api.repository;

import com.br.sccon.api.repository.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    List<Person> findAllByOrderByNomeAsc();
}