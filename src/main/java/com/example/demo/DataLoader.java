package com.example.demo;

import com.example.demo.service.PokemonService;
import com.example.demo.service.RoleService;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class DataLoader implements ApplicationRunner { //command line runner
    private final RoleService roleService;
    private final UserService userService;
    private final PokemonService pokService;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        // uygulama ayağ kalkarken burda kontrol yapılabilir
        roleService.checkAndCreateRoles(List.of(Constants.Roles.ADMIN, Constants.Roles.USER));
        userService.checkAndCreateAdminUser();
        pokService.checkAndCreatePokemons();
    }
}
