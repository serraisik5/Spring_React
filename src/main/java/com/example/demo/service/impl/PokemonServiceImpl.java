package com.example.demo.service.impl;

import com.example.demo.Constants;
import com.example.demo.dto.PokemonDto;
import com.example.demo.mapper.PokemonMapper;
import com.example.demo.model.Pokemon;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.PokemonRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.PokemonService;
import com.example.demo.service.UserService;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class PokemonServiceImpl implements PokemonService {
    private final PokemonRepository pokemonRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    @Override
    public Optional<Pokemon> findByName(String name) {
        return pokemonRepository.findPokemonByName(name);
    }
    @Transactional
    @Override
    public Pokemon createNewPokemon(Pokemon pok) {
        Objects.requireNonNull(pok,"pok is missing");
        return pokemonRepository.save(pok);
    }

    @Override
    public Pokemon findById(Long pokId) {
        Objects.requireNonNull(pokId,"id is missing");
        return pokemonRepository.findById(pokId).orElseThrow(()-> new RuntimeException("No such pok by id"));
    }

    @Override
    public List<Pokemon> findByName(String name, Integer page, Integer size) {
        if(StringUtils.isBlank(name)){
            if(Objects.nonNull(page)&&Objects.nonNull(size)){
                return pokemonRepository.findAll(PageRequest.of(page,size)).stream().toList();
            }
            return pokemonRepository.findAll();
        }
        if(Objects.nonNull(page)&&Objects.nonNull(size)){
            return pokemonRepository.findPokemonByNameContainingIgnoreCase(name, PageRequest.of(page,size));
        }

        return pokemonRepository.findPokemonByNameContainingIgnoreCase(name);
    }

    @Override
    public List<Pokemon> findAllPokemons(Integer page, Integer size) {
        return pokemonRepository.findAll(PageRequest.of(page,size)).stream().toList();
    }
    @Transactional
    @Override
    public void checkAndCreatePokemons() {

        final boolean existsUser = pokemonRepository.existsPokemonByName("serrapok");
        final boolean existsUser2 = pokemonRepository.existsPokemonByName("selenpok");
        final boolean existsUser3 = pokemonRepository.existsPokemonByName("fatihpok");
        if (!existsUser && !existsUser2 && !existsUser3){
            final Pokemon pok = Pokemon.builder().name("serrapok").type("fire").id(5545L)
                    .imageUrl("http://localhost:8080/images/001.png").height(23.3).weight(55.5).power("Strong")
                    .gender("female").build();
            final Pokemon pok2 = Pokemon.builder().name("fatihpok").type("water").height(23.3).weight(55.5).power("Strong")
                    .gender("male").id(3333L)
                    .imageUrl("http://localhost:8080/images/002.png").build();
            final Pokemon pok3 = Pokemon.builder().name("selenpok").type("air").height(23.3).weight(55.5).power("Weak")
                    .gender("female")
                    .imageUrl("http://localhost:8080/images/004.png").id(1212L).build();

            createNewPokemon(pok);
            createNewPokemon(pok2);
            createNewPokemon(pok3);
            userService.addPokemonsToCatchlist(1L,List.of(pok.getName(),pok2.getName()));
            userService.addPokemonsToWishlist(1L,List.of(pok3.getName()));
        }


    }
    @Override
    public void deletePokemon(Long pokId) {
        pokemonRepository.deleteById(pokId);
    }
    @Override
    public void deletePokemonByName(String name) {

        Pokemon pokemon = pokemonRepository.findPokemonByName(name)
                .orElseThrow(() -> new RuntimeException("Pokemon with name " + name + " not found"));

        List<User> users = userRepository.findAllByWishList_PokemonsContains(pokemon);
        for(User user: users){
            user.getWishList().getPokemons().remove(pokemon);
        }
        users = userRepository.findAllByCatchList_PokemonsContains(pokemon);
        for(User user: users){
            user.getCatchList().getPokemons().remove(pokemon);
        }
        userRepository.saveAll(users);
        pokemonRepository.delete(pokemon);
    }
    @Override
    public PokemonDto updatePokemon(Long id, PokemonDto pokemonDto) {

        Pokemon existingPokemon = pokemonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pokemon not found"));

        existingPokemon.setName(pokemonDto.getName());
        existingPokemon.setType(pokemonDto.getType());
        //existingPokemon.setType(pokemonDto.getImageUrl());
        existingPokemon.setPower(pokemonDto.getPower());
        existingPokemon.setGender(pokemonDto.getGender());
        existingPokemon.setHeight(pokemonDto.getHeight());
        existingPokemon.setWeight(pokemonDto.getWeight());

        Pokemon updatedPokemon = pokemonRepository.save(existingPokemon);
        return PokemonMapper.mapToDto(updatedPokemon);
    }


}
