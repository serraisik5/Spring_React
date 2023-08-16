package com.example.demo.repository;
import com.example.demo.model.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User,Long>  {

    Optional<User> findUserByUsername(String username);
    Boolean existsUserByUsername(String username);
    List<User> findUserByUsernameContainingIgnoreCase(String username, Pageable page);

    List<User> findUserByUsernameContainingIgnoreCase(String name);
    @Query(value = """
 from User u join u.roles r where u.username = :username and r.name = :roleName
            """)
    List<User> findUserByUsernameAndRoles(String username,String roleName);

    @Query("SELECT u FROM User u JOIN FETCH u.wishList WHERE u.username = :username")
    Optional<User> findByUsernameWithWishList(String username);
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.wishList WHERE u.id = :id")
    Optional<User> findByIdWithWishList(Long id);

    List<User> findAllByWishList_PokemonsContains(Pokemon pokemon);
    List<User> findAllByCatchList_PokemonsContains(Pokemon pokemon);

}
