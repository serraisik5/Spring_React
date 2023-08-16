package com.example.demo.mapper;


import com.example.demo.Constants;
import com.example.demo.dto.PokemonDto;
import com.example.demo.dto.UserDto;
import com.example.demo.model.Pokemon;
import com.example.demo.model.User;
import com.example.demo.utils.CollectionUtils;
import lombok.experimental.UtilityClass;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@UtilityClass
public class PokemonMapper {
    public static final PokemonDto mapToDto(Pokemon user){
        final PokemonDto pokemonDto = new PokemonDto() ;
        if (Objects. isNull (user)) {
            return pokemonDto;}
        pokemonDto.setId(user.getId());
        pokemonDto.setName(user.getName()); ;
        pokemonDto.setType(user.getType());
        pokemonDto.setImageUrl(user.getImageUrl());
        pokemonDto.setGender(user.getGender());
        pokemonDto.setHeight(user.getHeight());
        pokemonDto.setWeight(user.getWeight());
        pokemonDto.setPower(user.getPower());

        return pokemonDto;

    }
    public static final List<PokemonDto> mapToDto(List<Pokemon> poklist){
        if(CollectionUtils.isEmpty(poklist)){
            return Collections.emptyList();
        }
        return poklist.stream().map(user -> mapToDto(user)).toList();


    }
}