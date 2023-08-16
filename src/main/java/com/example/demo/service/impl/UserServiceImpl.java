package com.example.demo.service.impl;

import com.example.demo.Constants;
import com.example.demo.dto.UserDto;
import com.example.demo.mapper.PokemonMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.*;
import com.example.demo.repository.PokemonRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.RoleService;
import com.example.demo.service.UserService;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.context.ApplicationContext;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
@RequiredArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PokemonRepository pokemonRepository;

    @Transactional
    @Override
    public User createNewUser(User user) {
        Objects.requireNonNull(user,"user is missing");
        WishList wishList = new WishList();
        CatchList catchList = new CatchList();
        user.setWishList(wishList);
        user.setCatchList(catchList);
        wishList.setUser(user);
        catchList.setUser(user);
        return userRepository.save(user);
    }
    @Transactional
    @Override
    public User createNewUser(User user,String password, Boolean isAdmin) {
        Objects.requireNonNull(user,"user is missing");
        WishList wishList = new WishList();
        CatchList catchList = new CatchList();
        user.setWishList(wishList);
        user.setCatchList(catchList);
        wishList.setUser(user);
        catchList.setUser(user);
        user.setPassword(passwordEncoder.encode(password));
        Role role;
        if(isAdmin){
            role = roleService.findByName(Constants.Roles.ADMIN);
        }else {
            role = roleService.findByName(Constants.Roles.USER);
        }
        user.setRoles(List.of(role));
        role.getUsers().add(user);
        return userRepository.save(user);
    }

    @Override
    public User findById(Long userId)  {
        //final UserRepository userRepository = applicationContext.getBean(UserRepository.class);
        Objects.requireNonNull(userId,"id is missing");
        return userRepository.findById(userId).orElseThrow(()-> new RuntimeException("No such user by id"));

    }
    @Override
    public List<User> findAllUsers(Integer page, Integer size) {
        //final UserRepository userRepository = applicationContext.getBean(UserRepository.class);
        return userRepository.findAll(PageRequest.of(page,size)).stream().toList();

    }
    @Override
    public List<User> findByUserName(String name, Integer page, Integer size) {
        //final UserRepository userRepository = applicationContext.getBean(UserRepository.class);
        if(StringUtils.isBlank(name)){
            if(Objects.nonNull(page)&&Objects.nonNull(size)){
                return userRepository.findAll(PageRequest.of(page,size)).stream().toList();
            }
            return userRepository.findAll();
        }
        if(Objects.nonNull(page)&&Objects.nonNull(size)){
            return userRepository.findUserByUsernameContainingIgnoreCase(name, PageRequest.of(page,size));
        }

        return userRepository.findUserByUsernameContainingIgnoreCase(name);
    }
    @Override
    public void addRolesToUser(Long userId, List<String> roleNames) {
        //final UserRepository userRepository = applicationContext.getBean(UserRepository.class);
        //final RoleRepository roleRepository = applicationContext.getBean(RoleRepository.class);
        User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("No such user by id"));
        List<Role> roles = roleNames.stream()
                .map(roleName -> roleRepository.findByName(roleName).orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
                .collect(Collectors.toList());
        user.getRoles().addAll(roles);
        userRepository.save(user);
    }
    @Override
    public Optional<User> findByUsername(String username) {
        // final UserRepository userRepository = applicationContext.getBean(UserRepository.class);
        return userRepository.findUserByUsername(username);
    }
    @Transactional
    @Override
    public void checkAndCreateAdminUser() {
        // final UserRepository userRepository = applicationContext.getBean(UserRepository.class);
        final boolean existsAdmin = userRepository.existsUserByUsername("sysadmin");
        final boolean existsUser = userRepository.existsUserByUsername("sysuser");
        if(!existsAdmin){
            Role role = roleService.findByName(Constants.Roles.ADMIN);

            final User user = User.builder().username("sysadmin")
                    .enabled(Boolean.TRUE).password(passwordEncoder.encode("Admin123"))
                    .roles(List.of(role)).build();
            role.getUsers().add(user);
            createNewUser(user);
        }
        if(!existsUser){
            Role role = roleService.findByName(Constants.Roles.USER);

            final User user = User.builder().username("sysuser")
                    .enabled(Boolean.FALSE).password(passwordEncoder.encode("User123"))
                    .roles(List.of(role)).build();
            role.getUsers().add(user);
            createNewUser(user);
        }
    }

    @Override
    public void deleteUserByUsername(String name) {
        User user = userRepository.findUserByUsername(name)
                .orElseThrow(() -> new RuntimeException("User with username " + name + " not found"));

       userRepository.delete(user);
    }
    @Override
    public void deleteUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with id " + id + " not found"));

        userRepository.delete(user);
    }

    @Override
    public UserDto updateUser(Long id, UserDto userdto) {
        User existingPokemon = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingPokemon.setUsername(userdto.getUsername());
        //existingPokemon.setPassword(userdto.getPassword());
        //existingPokemon.setRoles(userdto.getIsAdmin() ? List.of(roleService.findByName(Constants.Roles.ADMIN)) : List.of());
        //existingPokemon.setEnabled(true);

        User updatedPokemon = userRepository.save(existingPokemon);
        return UserMapper.mapToDto(updatedPokemon);
    }

    @Override
    public void addPokemonsToWishlist(Long userId, List<String> pokNames) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        WishList wishlist = user.getWishList();
        List<Pokemon> existingPokemons = wishlist.getPokemons();

        List<Pokemon> pokemonsToAdd = pokemonRepository.findByNameIn(pokNames);
        for (Pokemon pokemonToAdd : pokemonsToAdd) {
            // Check if the Pokemon with this name is already in the wishlist
            boolean isAlreadyInWishlist = existingPokemons.stream()
                    .anyMatch(existingPokemon -> existingPokemon.getName().equals(pokemonToAdd.getName()));

            // If not already in the wishlist, add it
            if (!isAlreadyInWishlist) {
                wishlist.getPokemons().add(pokemonToAdd);
            }
        }
        userRepository.save(user);
    }
    @Override
    public void addPokemonsToCatchlist(Long userId, List<String> pokNames) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        CatchList wishlist = user.getCatchList();
        List<Pokemon> existingPokemons = wishlist.getPokemons();

        List<Pokemon> pokemonsToAdd = pokemonRepository.findByNameIn(pokNames);
        for (Pokemon pokemonToAdd : pokemonsToAdd) {
            // Check if the Pokemon with this name is already in the wishlist
            boolean isAlreadyInWishlist = existingPokemons.stream()
                    .anyMatch(existingPokemon -> existingPokemon.getName().equals(pokemonToAdd.getName()));

            // If not already in the wishlist, add it
            if (!isAlreadyInWishlist) {
                wishlist.getPokemons().add(pokemonToAdd);
            }
        }
        userRepository.save(user);
    }

    @Override
    public List<Pokemon> listWishList(String username) {
        Optional<User> optionalUser = userRepository.findUserByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            WishList wishList = user.getWishList();
            return wishList.getPokemons();
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Pokemon> listCatchList(String username) {
        Optional<User> optionalUser = userRepository.findByUsernameWithWishList(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            CatchList wishList = user.getCatchList();
            return wishList.getPokemons();
        } else {
            return Collections.emptyList();
        }
    }
    @Override
    public void removePokemonsFromWishlist(Long userId, List<String> pokNames) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        WishList wishlist = user.getWishList();
        List<Pokemon> pokemons = wishlist.getPokemons();
        pokemons.removeIf(pokemon -> pokNames.contains(pokemon.getName()));
        wishlist.setPokemons(pokemons);
        userRepository.save(user);
    }
    @Override
    public void removePokemonsFromCatchlist(Long userId, List<String> pokNames) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        CatchList wishlist = user.getCatchList();
        List<Pokemon> pokemons = wishlist.getPokemons();
        pokemons.removeIf(pokemon -> pokNames.contains(pokemon.getName()));
        wishlist.setPokemons(pokemons);
        userRepository.save(user);
    }
}
