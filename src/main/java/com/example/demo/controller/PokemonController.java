package com.example.demo.controller;


import com.example.demo.service.*;
import com.example.demo.model.*;
import com.example.demo.dto.*;
import com.example.demo.mapper.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

@RequestMapping("/poks")
//@CrossOrigin(origins = "http://localhost:5173")
//@RequiredArgsConstructor
@RestController
public class PokemonController {

    private PokemonService pokService;
    @Autowired
    public PokemonController(@Qualifier("pokemonServiceImpl") PokemonService pokemonService){
        this.pokService = pokemonService;
    }

    @GetMapping()
    public BaseResponse<List<PokemonDto> >searchPokemons(@RequestParam(name = "name",required = false) String name,
                                                   @RequestParam(value= "page",required = false)Integer page,
                                                   @RequestParam(value="size",required = false)Integer size){
        List<Pokemon> pokemons = pokService.findByName(name,page,size);
        if(Objects.isNull(name)){
            return new BaseResponse<>(PokemonMapper.mapToDto(pokemons));
        }

        return new BaseResponse<>(PokemonMapper.mapToDto(pokemons));
    }

    @GetMapping("/{pokId}")
    public BaseResponse<PokemonDto> getPokemanDetails(@PathVariable("pokId") Long pokId) {
        Pokemon user = pokService.findById(pokId);
        return new BaseResponse<>(PokemonMapper.mapToDto(user));
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiIgnore // This will ignore the API in Swagger UI
    @ApiOperation(value = "Save Pokémon", notes = "This endpoint is used to save Pokémon along with an associated image")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse<PokemonDto> savePokemon(@RequestPart("file") MultipartFile file, @RequestPart("pokemon") @Valid RequestNewPokemon request) {
        String imageName;
        String imageNameWithoutExtension;

        try {
            imageName = file.getOriginalFilename(); // or create a unique name
            imageNameWithoutExtension = imageName != null ? imageName.substring(0, imageName.lastIndexOf('.')) : null;
            Path path = Paths.get("src/main/resources/static/images/" + imageName);
            Files.write(path, file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            return new BaseResponse<>((long) HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error saving image");
        }


        final Pokemon newpok = Pokemon.builder()
                .name(request.getName())
                .id(request.getId())
                .type(request.getType())
                .imageUrl(imageNameWithoutExtension)
                .gender(request.getGender()).height(request.getHeight()).weight(request.getWeight()).power(request.getPower())
                .build();

        pokService.createNewPokemon(newpok);
        return new BaseResponse<>(PokemonMapper.mapToDto(newpok));
    }



    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{pokId}")
    public String deletePokemon(@PathVariable("pokId") Long pokId) {
        pokService.deletePokemon(pokId);
        return "Pokemon deleted";
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/name/{pokName}")
    public String deletePokemon(@PathVariable("pokName") String pokname) {

        pokService.deletePokemonByName(pokname);
        return "Pokemon deleted";
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public BaseResponse<PokemonDto> updatePokemon(@PathVariable Long id, @RequestBody PokemonDto pokemonDto) {
        PokemonDto updatedPokemonDto = pokService.updatePokemon(id, pokemonDto);
        return new BaseResponse<>(updatedPokemonDto);
    }
    @GetMapping("/images/{imageName}")
    public ResponseEntity<Resource> serveImage(@PathVariable String imageName) throws MalformedURLException {
        Resource imageResource = new UrlResource("file:src/main/resources/static/images/" + imageName + ".png");
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(imageResource);
    }
    /**
    @ApiIgnore
    @ApiOperation("Save or Update Author.")
    @RequestMapping(value = { "/saveOrUpdateAuthors.json" }, method = RequestMethod.POST)
    public BaseResponse<Object> saveOrUpdateAuthors(HttpServletRequest servletRequest,
                                                    @ApiParam(value = "Image Content") @RequestParam(value = "imageContent") MultipartFile imageContent,
                                                    @ApiParam(value = "ID if 0 save,if bigger 0 update") @RequestParam(value = "id") long id,
                                                    @ApiParam(value = "Name") @RequestParam(value = "fname") String fName,


*/


}