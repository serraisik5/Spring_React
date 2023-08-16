package com.example.demo.repository;

import com.example.demo.model.Pokemon;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PokemonRepository extends JpaRepository<Pokemon, Long> {
    Optional<Pokemon> findPokemonByName(String name);
    List<Pokemon> findByNameIn(List<String> pokNames);

    Boolean existsPokemonByName(String username);
    List<Pokemon> findPokemonByNameContainingIgnoreCase(String name, Pageable page);

    List<Pokemon> findPokemonByNameContainingIgnoreCase(String name);
}
