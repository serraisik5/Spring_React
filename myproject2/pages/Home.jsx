import React, { useState, useEffect } from 'react';
import UserProfile from './UserProfile';
import Pokemons from './Pokemons';
import Users from './Users';
import { useLocation, useNavigate} from 'react-router-dom';
import UserService from '../service/UserService';
import '../designs/Home.css';
import SearchIcon from "../icons/search.png"
import PokemonService from '../service/PokemonService';
import AuthService from '../service/AuthService';
import PokemonInfo from './PokemonInfo';

const Home = () => {
  const navigate = useNavigate();
  const [user, setUser] = useState(null);
  const [admin, setAdmin] = useState(null);
  const [wishlist, setWishlist] = useState([]);
  const [catchlist, setCatchlist] = useState([]);
  const [AllPokemon, setAllPokemon] = useState([]);
  const [showPokemons, setShowPokemons] = useState(false);
  const [showUsers, setShowUsers] = useState(false);
  const [selectedPokemon, setSelectedPokemon] = useState(null);

  const location = useLocation();
  const username = location.state ? location.state.username : null;

  const handlePokemonClick = (pokemon) => {
    setSelectedPokemon(pokemon);
  };
  
  useEffect(() => {
    if (username) {
        UserService.getUserByUsername(username)
            .then(data => {
                if (data && data.payload && data.payload.length > 0) {
                    const user = data.payload[0];
                    console.log("User:", user.isAdmin);
                    setUser(user);
                    setAdmin(user.isAdmin)
                }
            })
            
    UserService.getUserWishlist(username).then((fetchedWishlist) => {
      setWishlist(fetchedWishlist.payload);
    });
    UserService.getUserCatchlist(username).then((fetchedWishlist)=> {
        setCatchlist(fetchedWishlist.payload);
    }).catch(error => {
        console.log(error);
    });
    }
  }, [username]);

  const handleUpdatePokemon = () => {
    console.log("handle update pokemon called")

      UserService.getUserWishlist(username).then((fetchedWishlist) => {
        setWishlist(fetchedWishlist.payload);
    });
      UserService.getUserCatchlist(username).then((fetchedCatchlist)=> {
        setCatchlist(fetchedCatchlist.payload);
    })
      PokemonService.listPokemons().then((pokemons) => {
        setAllPokemon(pokemons.payload);
    });
  
  };
  const handleLogout = async () => {
    const result = await AuthService.logout();
    if (result) {
      navigate('/login'); 
    } else {
      console.log('Failed to logout'); 
    }
  };

  const handleSearchPokemons = () => {
    setShowPokemons(true);
  };

  const handleSearchUsers = () => {
    setShowUsers(true);
  };

  return (
    <div className="home-container">
      <div className="sidebar">
        <UserProfile wishlist={wishlist} catchlist={catchlist} allpokemon={AllPokemon} 
        onPokemonClick={handlePokemonClick} 
        onUpdatePokemon={handleUpdatePokemon} />
      </div>
      <div className="content">
        <button className="search-button" onClick={handleSearchPokemons}>
          <img className="search-icon" src={SearchIcon} alt="search" /> Search Pokemons
        </button>
        {admin && (
        <button className="search-button" onClick={handleSearchUsers}>
          <img className="search-icon" src={SearchIcon} alt="search" /> Search Users
        </button>
        
        )}
        </div>
        <div>
                        <PokemonInfo selectedPokemon={selectedPokemon} />
      
            </div>
        <div className="logout-button-container">
        <button className="logout-button" onClick={handleLogout}>Logout</button>
      </div>
      {showPokemons && <div className="modal">
        <Pokemons onUpdatePokemon={handleUpdatePokemon} />
        <div className="close-button-container">
        <button onClick={() => setShowPokemons(false)}>Close</button></div>
        </div> }
      {showUsers && <div className="modal"><Users />
      <div className="close-button-container">
        <button onClick={() => setShowUsers(false)}>Close</button></div>
        </div>
      }
    </div>
  );
  
  
  
};

export default Home;
