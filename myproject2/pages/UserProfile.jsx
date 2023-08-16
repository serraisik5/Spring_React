import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import UserService from '../service/UserService';
import Wishlist from './WishList';
import Catchlist from './CatchList';
import "../designs/UserProfile.css";
import PokeballIcon from "../icons/pokeball.png";
import PokemonService from '../service/PokemonService';
import PokemonIcon from "../icons/pokemon.png";
import PokemonImage from './PokemonImage';

const UserProfile = ({wishlist,catchlist,allpokemon, onUpdatePokemon, onPokemonClick}) => {
  const [user, setUser] = useState(null);
  const [userId, setUserId] = useState(null);
  //const [wishlist, setWishlist] = useState(wishlist);
  //const [catchlist, setCatchlist] = useState([]);
  const location = useLocation();
  const username = location.state ? location.state.username : null; 
  
  //console.log(user.id)
  useEffect(() => {
    if (username) {
        UserService.getUserByUsername(username)
            .then(data => {
                if (data && data.payload && data.payload.length > 0) {
                    const user = data.payload[0];
                    console.log("User:", user);
                    setUser(user);
                    setUserId(user.id); 
                }
            })
            .catch(error => {
                console.log(error);
            });
        UserService.getUserWishlist(username)
            .then(data => {
              if (data && data.payload) {
                setWishlist(data.payload);
                if (onUpdatePokemon) {
                  onUpdatePokemon();
                }
              }
            })
            .catch(error => {
              console.log(error);
            });
        PokemonService.listPokemons()
          .then(data => {
             if (data && data.payload) {
               setAllPokemons(data.payload);
               if (onUpdatePokemon) {
                onUpdatePokemon();
              }
             }
        })
           .catch(error => {
          console.log(error);
        });
        UserService.getUserCatchlist(username)
          .then(data => {
          if (data && data.payload) {
            setCatchlist(data.payload);
            }
            if (onUpdatePokemon) {
              onUpdatePokemon();
            }
           })
          .catch(error => {
            console.log(error);
            });

    }
}, [username]);

  return (
    <div className="user-profile-container">
      <div className="user-info">
      <img src={PokemonIcon} alt="Pokeball" className="pokeball-icon" />
        {user ? (
          <div>
            <h2>Welcome, {user.username}!</h2>
            <p> {user.isAdmin ? 'Poxedex Admin' : 'Poxedex Trainer'}</p>
          </div>
        ) : (
          <p>Loading user information...</p>
        )}
      </div>
      {user && (
      <div className="lists-container">
        <Wishlist wishlist={wishlist} userId={userId} allpokemon={allpokemon} 
        onPokemonClick={onPokemonClick} 
        onUpdatePokemon={onUpdatePokemon} />
        <Catchlist catchlist={catchlist} userId={userId} allpokemon={allpokemon} 
        onPokemonClick={onPokemonClick} 
        onUpdatePokemon={onUpdatePokemon} />
      </div>
    )}
    </div>
  );
};

export default UserProfile;

