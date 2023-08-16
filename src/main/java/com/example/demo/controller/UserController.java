package com.example.demo.controller;

import com.example.demo.service.*;
import com.example.demo.model.*;
import com.example.demo.dto.*;
import com.example.demo.mapper.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RequestMapping("/users")
//@CrossOrigin(origins = "http://localhost:5173")
//@RequiredArgsConstructor
@RestController
public class UserController {
    //private final List<User> users = getUsers();
    private final UserService userService;

    // lomboksuz constructor
    @Autowired
    public UserController(@Qualifier("userServiceImpl") UserService userService){
        this.userService = userService;
    }
    @Operation(summary = "get users by parameters")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiResponses(value={@ApiResponse(responseCode = "200",description = "Found users"),@ApiResponse(responseCode = "404",description = "no users found")})
    @GetMapping()
    public BaseResponse<List<UserDto> >searchUsers(@RequestParam(name = "name",required = false) String name,
                                                   @RequestParam(value= "page",required = false)Integer page,
                                                   @RequestParam(value="size",required = false)Integer size){
        List<User> users = userService.findByUserName(name,page,size);
        if(Objects.isNull(name)){
            return new BaseResponse<>(UserMapper.mapToDto(users));
        }

        return new BaseResponse<>(UserMapper.mapToDto(users));
    }
    @Operation(summary = "get users by id")
    @GetMapping("/{userId}")
    public BaseResponse<UserDto>  getUserDetails(@PathVariable("userId") Long userId) {
        User user = userService.findById(userId);
        return new BaseResponse<>(UserMapper.mapToDto(user));
    }
    @Operation(summary = "save user")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public BaseResponse<UserDto>  saveUser(@Valid @RequestBody RequestNewUser requestNewUser){
        final User newuser = User.builder()
               .username(requestNewUser.getUsername()).password(requestNewUser.getPassword()).enabled(true)
                .build();
        userService.createNewUser(newuser,requestNewUser.getPassword(),requestNewUser.getIsAdmin());
        return new BaseResponse<>(UserMapper.mapToDto(newuser));
    }
    @PutMapping("/{userId}/roles")
    public String addRolesToUser(@PathVariable Long userId, @RequestBody List<String> roleNames) {
        userService.addRolesToUser(userId, roleNames);
        return "Roles added successfully";
    }
    @PutMapping("/{userId}/wishlist")
    public String addWishToUser(@PathVariable Long userId, @RequestBody List<String> pokNames) {
        userService.addPokemonsToWishlist(userId,pokNames);
        userService.removePokemonsFromCatchlist(userId,pokNames);
        return "Poks added to wishlist successfully";
    }
    @PutMapping("/{userId}/catchlist")
    public String addCatchToUser(@PathVariable Long userId, @RequestBody List<String> pokNames) {
        userService.addPokemonsToCatchlist(userId,pokNames);
        userService.removePokemonsFromWishlist(userId,pokNames);
        return "Poks added to catch list successfully";
    }
    @GetMapping("/wishlist")
    public BaseResponse<List<PokemonDto>> listWishList(@RequestParam(name = "username",required = true) String name){
        List<Pokemon> poks = userService.listWishList(name);
        if(Objects.isNull(name)){
            return new BaseResponse<>(PokemonMapper.mapToDto(poks));
        }

        return new BaseResponse<>(PokemonMapper.mapToDto(poks));
    }
    @GetMapping("/catchlist")
    public BaseResponse<List<PokemonDto>> listCatchList(@RequestParam(name = "username",required = true) String name){
        List<Pokemon> poks = userService.listCatchList(name);
        if(Objects.isNull(name)){
            return new BaseResponse<>(PokemonMapper.mapToDto(poks));
        }

        return new BaseResponse<>(PokemonMapper.mapToDto(poks));
    }

    @DeleteMapping("/{userId}/wishlist")
    public String removePokFromWishlist(@PathVariable Long userId, @RequestBody List<String> pokNames) {
        userService.removePokemonsFromWishlist(userId, pokNames);
        return "Poks removed from wishlist successfully";
    }
    @DeleteMapping("/{userId}/catchlist")
    public String removePokFromCatchlist(@PathVariable Long userId, @RequestBody List<String> pokNames) {
        userService.removePokemonsFromCatchlist(userId, pokNames);
        return "Poks removed from catchlist successfully";
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/name/{name}")
    public String deleteUser(@PathVariable("name") String name) {
        userService.deleteUserByUsername(name);
        return "User deleted";
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/id/{userId}")
    public String deleteUser(@PathVariable("userId") Long id) {
        userService.deleteUserById(id);
        return "User deleted";
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public BaseResponse<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto pokemonDto) {
        UserDto updatedPokemonDto = userService.updateUser(id, pokemonDto);
        return new BaseResponse<>(updatedPokemonDto);
    }

}


