package com.example.demo.service;

import com.example.demo.dto.UserDto;
import com.example.demo.model.Pokemon;
import com.example.demo.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);
    void addRolesToUser(Long userId, List<String> roleNames);
    void addPokemonsToWishlist(Long id, List<String> pokNames);
    void addPokemonsToCatchlist(Long id, List<String> pokNames);
    List<Pokemon> listWishList(String username);
    List<Pokemon> listCatchList(String username);
    void removePokemonsFromWishlist(Long userId, List<String> pokNames);
    void removePokemonsFromCatchlist(Long userId, List<String> pokNames);
    User createNewUser(User user, String password,Boolean isAdmin);
    User createNewUser(User user);
    User findById(Long userId);
    List<User> findByUserName(String username, Integer page, Integer size);
    List<User> findAllUsers(Integer page, Integer size);

    void checkAndCreateAdminUser();
    void deleteUserByUsername(String username);
    void deleteUserById(Long id);
    UserDto updateUser(Long id, UserDto userdto);


}
