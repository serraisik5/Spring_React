import { Table,Button } from 'antd';
import React, { useState, useEffect } from 'react';
import UserService from '../service/UserService';
import GrassIcon from "../icons/grass.png";
import WaterIcon from "../icons/water.png";
import FireIcon from "../icons/fire.png";
import AirIcon from "../icons/air.png";
import PokemonService from '../service/PokemonService';
import "../designs/Catchlist.css";
import PokemonImage from './PokemonImage';
import "../designs/Flash.css";

const Catchlist = ({ catchlist, userId, allpokemon, onUpdatePokemon , onPokemonClick}) => {
    const [CatchlistData, setCatchlistData] = useState(catchlist);
    const [newPokemonName, setNewPokemonName] = useState('');
    const [allPokemon, setAllPokemon] = useState(allpokemon);
    const [error, setError] = useState(null);


    useEffect(() => {
        const fetchCatchlist = async () => {
          try {
            const user = await UserService.getUserById(userId);
            const Catchlist = await UserService.getUserCatchlist(user.payload.username);
            const allpoks = await PokemonService.listPokemons();
            setCatchlistData(Catchlist.payload);
            setAllPokemon(allpoks.payload);
            if (onUpdatePokemon) {
              onUpdatePokemon();
            }
          } catch (error) {
            console.error('Failed to fetch Catchlist or allpoks:', error);
          }
        };
    
        fetchCatchlist();
      }, [userId]);

      useEffect(() => {
        setCatchlistData(catchlist);
      }, [catchlist]);
      useEffect(() => {
        setAllPokemon(allpokemon);
      }, [allpokemon]);

    const deletePokemon = (pokName) => {
        UserService.deletePokemonFromCatchlist(userId, pokName)
          .then(() => {
            const updatedCatchlist = CatchlistData.filter((item) => item.name !== pokName);
            setCatchlistData(updatedCatchlist);
          })
          .catch((error) => {
            console.log(`Failed to delete Pokemon with name: ${pokName}. Error: ${error}`);
          });
      };
      const addPokemon = () => {
        console.log("inside add pok")
        if (newPokemonName) {
            if (allPokemon.some((pokemon) => pokemon.name === newPokemonName)) {
                 UserService.addPokemonToCatchlist(userId, newPokemonName)
                    .then(() => {
                         UserService.getUserById(userId)
                         
                 .then(user => {
                     UserService.getUserCatchlist(user.payload.username) 
                    .then(updatedCatchlist => {
                      if (onUpdatePokemon) {
                        onUpdatePokemon();
                      }
                      setCatchlistData(updatedCatchlist.payload);
                      setNewPokemonName('');
                      setError(null);
                    });
                    

                })
            })
            .catch((error) => {
              console.log(`Failed to add Pokemon with name: ${newPokemonName}. Error: ${error}`);
            });
        } else {
            setError(`No Pokémon found with the name: ${newPokemonName}`);
            setTimeout(() => {
                setError(null);
              }, 3000);
          }
        }
      };
  const columns = [
    {
      title: 'Image',
      dataIndex: 'imageUrl', 
      render: (imageName) => <PokemonImage imageName={imageName} />,
      width: "20%",
    },
    
    {
      title: 'Name',
      dataIndex: 'name',
      render: (name, pokemon) => (
        <span
          onClick={() => {
            onPokemonClick(pokemon);
            const element = document.getElementById(`pokemon-${name}`);
            element.classList.add('pokemon-flash');
            setTimeout(() => {
              element.classList.remove('pokemon-flash');
            }, 500); // Remove the class after 300 milliseconds
          }}
          id={`pokemon-${name}`}
        >
          {name}
        </span>
      ),
    },
    {
      title: 'Type',
      dataIndex: 'type',
      render: (type) => {
        let icon;
        switch (type) {
          case 'fire':
              icon = <img src={FireIcon} alt="fire" />;
              break;
          case 'air':
              icon = <img src={AirIcon} alt="air" />;
              break;
          case 'water':
              icon = <img src={WaterIcon} alt="water" />;
              break;
          case 'grass':
              icon = <img src={GrassIcon} alt="grass" />;
              break;
          default:
              icon = null;
              break;
        }
        return (
          <span>
            {icon} {type}
          </span>
        );
      },
    },
    {
        title: 'Remove',
        key: 'action',
        render: (text, record) => (
          <Button onClick={() => deletePokemon(record.name)}>Remove</Button>
        ),
    },
  ];

  return (
    <div className='catchlist-container'>
      <h2>Your Catchlist</h2>
      <div className="search-container">
        
        <input
          list="pokemon-names-c"
          type="text"
          value={newPokemonName}
          onChange={(e) => setNewPokemonName(e.target.value)}
          placeholder="Add Pokemon to Catchlist"
        />
    
    <datalist id="pokemon-names-c">
  {allPokemon
    .filter((pokemon) => {
      const isNotInCatchlist = !CatchlistData.some((wish) => wish.name === pokemon.name);
      return isNotInCatchlist;
    })
    .map((pokemon) => (
      <option key={pokemon.id} value={pokemon.name} />
    ))}
</datalist>
        <Button onClick={addPokemon}>Add Pokemon</Button>
        {error && <p style={{ color: 'red' }}>{error}</p>}
      </div>
      <Table columns={columns} dataSource={CatchlistData} rowKey="id" />
    </div>
  );
}

export default Catchlist;
