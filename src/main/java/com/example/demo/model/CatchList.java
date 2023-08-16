package com.example.demo.model;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CatchList extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "catchlist_pokemons",
            joinColumns = @JoinColumn(name = "catchlist_id"),
            inverseJoinColumns = @JoinColumn(name = "pokemon_id"))
    private List<Pokemon> pokemons = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
