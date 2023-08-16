package com.example.demo.service;

import com.example.demo.dto.PokemonDto;
import com.example.demo.model.Pokemon;
import com.example.demo.model.User;

import java.util.List;
import java.util.Optional;

public interface PokemonService {

    Optional<Pokemon> findByName(String name);
    Pokemon createNewPokemon(Pokemon pok);
    Pokemon findById(Long pokId);
    List<Pokemon> findByName(String name, Integer page, Integer size);
    List<Pokemon> findAllPokemons(Integer page, Integer size);
    void deletePokemon(Long pokId);
    void deletePokemonByName(String name);
    void checkAndCreatePokemons();
    PokemonDto updatePokemon(Long id, PokemonDto pokemonDto);



}
